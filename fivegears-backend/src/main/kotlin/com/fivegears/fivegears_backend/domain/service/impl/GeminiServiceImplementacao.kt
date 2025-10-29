package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.config.GeminiConfig
import com.fivegears.fivegears_backend.domain.repository.*
import com.fivegears.fivegears_backend.dto.FiltroAlocacao
import com.fivegears.fivegears_backend.dto.UsuarioAlocadoDTO
import com.fivegears.fivegears_backend.entity.enum.NivelSoftSkill
import com.fivegears.fivegears_backend.entity.enum.SenioridadeCargo
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GeminiServiceImplementacao(
    private val config: GeminiConfig,
    private val usuarioRepository: UsuarioRepository,
    private val usuarioCargoRepository: UsuarioCargoRepository,
    private val usuarioCompetenciaRepository: UsuarioCompetenciaRepository,
    private val usuarioSoftSkillRepository: UsuarioSoftSkillRepository,
    private val usuarioProjetoRepository: UsuarioProjetoRepository
) {
    private val log = LoggerFactory.getLogger(GeminiServiceImplementacao::class.java)
    private val mapper = ObjectMapper()
    private val restTemplate = RestTemplate()

    // =========================================
    // 🧩 PROMPT BASE APRIMORADO
    // =========================================
    private val promptBase = """
        Você é o SunnyBOT, assistente de alocação da plataforma FiveGears.
        Sua função é entender frases humanas sobre alocação de profissionais e gerar um JSON com os seguintes campos:
        {
            "cargoNome": "Programador",
            "cargoMinimo": "JUNIOR",
            "competencias": ["Kotlin", "Spring Boot"],
            "softSkills": ["Proatividade", "Trabalho em equipe"],
            "horasDisponiveisMin": 20,
            "valorHoraMax": 120.0
        }

        Regras:
        - Normalize a senioridade para: ESTAGIARIO, JUNIOR, PLENO ou SENIOR.
        - Interprete variações como: "jr", "júnior", "pl", "pleno", "sr", "sênior".
        - Se não identificar cargo ou nível, diga explicitamente que não foi possível.
        - Retorne **somente o JSON**, sem comentários ou explicações.

        Exemplos:
        Entrada: "Preciso de programador júnior com Kotlin e Spring Boot"
        Saída: {"cargoNome":"Programador","cargoMinimo":"JUNIOR","competencias":["Kotlin","Spring Boot"],"softSkills":[],"horasDisponiveisMin":40,"valorHoraMax":120}

        Entrada: "Quero estagiário de design UI/UX"
        Saída: {"cargoNome":"Designer","cargoMinimo":"ESTAGIARIO","competencias":["UI/UX","Design"],"softSkills":[],"horasDisponiveisMin":20,"valorHoraMax":60}
    """.trimIndent()

    // =========================================
    // 🔹 GERA FILTRO A PARTIR DA MENSAGEM
    // =========================================
    fun gerarFiltro(mensagem: String): FiltroAlocacao {
        log.info("🧠 Solicitando filtro ao Gemini: '{}'", mensagem)

        val promptFinal = """
        $promptBase

        Mensagem do usuário: "$mensagem"
    """.trimIndent()

        // 🔒 Escapa aspas e remove quebras de linha para evitar JSON inválido
        val promptEscapado = promptFinal
            .replace("\"", "\\\"")
            .replace("\n", " ")
            .replace("\r", "")

        val body = """{"contents":[{"parts":[{"text":"$promptEscapado"}]}]}"""
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(body, headers)
        val url = "${config.baseUrl}?key=${config.apiKey}"

        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)

            if (response.statusCode != HttpStatus.OK) {
                log.error("❌ Gemini retornou código ${response.statusCode}: ${response.body}")
                throw RuntimeException("Falha na requisição ao Gemini (${response.statusCode})")
            }

            val textoOriginal = extrairTextoGemini(response.body ?: "{}")

            // 🧹 Remove blocos markdown e limpa o texto antes de parsear
            val textoLimpo = textoOriginal
                .replace("```json", "")
                .replace("```", "")
                .trim()

            log.info("📩 Resposta limpa do Gemini: {}", textoLimpo)

            val filtro = try {
                val parsed = mapper.readValue(textoLimpo, FiltroAlocacao::class.java)
                if (parsed.cargoNome.isNullOrBlank() || parsed.cargoMinimo == null) {
                    log.warn("⚠️ Filtro incompleto — aplicando fallback.")
                    gerarFallback(mensagem)
                } else parsed
            } catch (e: Exception) {
                log.error("⚠️ Falha ao interpretar JSON: {}", textoLimpo)
                gerarFallback(mensagem)
            }

            log.info("✅ Filtro final: {}", mapper.writeValueAsString(filtro))
            filtro

        } catch (e: Exception) {
            log.error("❌ Erro ao consultar Gemini: {}", e.message)
            gerarFallback(mensagem)
        }
    }

    private fun extrairTextoGemini(body: String): String {
        return try {
            val json: JsonNode = mapper.readTree(body)
            json["candidates"]?.get(0)
                ?.path("content")?.path("parts")?.get(0)?.path("text")
                ?.asText() ?: "{}"
        } catch (_: Exception) {
            "{}"
        }
    }

    // =========================================
    // ⚙️ FALLBACK RESTRITIVO
    // =========================================
    private fun gerarFallback(mensagem: String): FiltroAlocacao {
        val texto = mensagem.lowercase()

        val cargoNome = when {
            texto.contains("programador") -> "Programador"
            texto.contains("analista") -> "Analista"
            texto.contains("designer") -> "Designer"
            texto.contains("desenvolvedor") -> "Desenvolvedor"
            else -> null
        }

        val cargoMinimo = when {
            texto.contains("estagi") -> SenioridadeCargo.ESTAGIARIO
            texto.contains("jr") || texto.contains("júnior") -> SenioridadeCargo.JUNIOR
            texto.contains("pl") || texto.contains("pleno") -> SenioridadeCargo.PLENO
            texto.contains("sr") || texto.contains("sênior") || texto.contains("senior") -> SenioridadeCargo.SENIOR
            else -> null
        }

        if (cargoNome == null || cargoMinimo == null) {
            log.warn("❌ Mensagem vaga — nenhum cargo ou nível identificado.")
            throw IllegalArgumentException("Mensagem vaga demais — especifique cargo e nível.")
        }

        return FiltroAlocacao(
            cargoNome = cargoNome,
            cargoMinimo = cargoMinimo,
            competencias = emptyList(),
            softSkills = emptyList(),
            horasDisponiveisMin = 40,
            valorHoraMax = 120.0
        )
    }

    // =========================================
    // 🔍 BUSCAR USUÁRIOS FILTRADOS
    // =========================================
    fun buscarUsuarios(filtro: FiltroAlocacao): List<UsuarioAlocadoDTO?> {
        log.info("🚀 Buscando usuários com filtro: {}", mapper.writeValueAsString(filtro))

        val usuarios = usuarioRepository.findAll()
        val cargoDesejado = filtro.cargoNome?.trim()?.lowercase()
        val nivelSolicitado = filtro.cargoMinimo

        val filtrados = usuarios.filter { usuario ->
            val cargosUsuario = usuarioCargoRepository.findByUsuario(usuario)
            val cargoAtual = cargosUsuario.firstOrNull() ?: return@filter false

            if (!cargoAtual.cargo!!.nome.lowercase().contains(cargoDesejado ?: "")) return@filter false
            if (cargoAtual.senioridade != nivelSolicitado) return@filter false

            val projetosAtivos = usuarioProjetoRepository.findByUsuario(usuario)
                .filter { it.status.name == "ALOCADO" }
            if (projetosAtivos.isNotEmpty()) return@filter false

            true
        }.map { usuario ->
            val cargoAtual = usuarioCargoRepository.findByUsuario(usuario).firstOrNull()
            val projetosAtivos = usuarioProjetoRepository.findByUsuario(usuario)
                .filter { it.status.name == "ALOCADO" }
                .mapNotNull { it.projeto?.nome }

            usuario.id?.let {
                UsuarioAlocadoDTO(
                    id = it,
                    nome = usuario.nome,
                    email = usuario.email,
                    cargo = cargoAtual?.cargo?.nome,
                    idCargo = cargoAtual?.cargo?.idCargo,
                    senioridade = cargoAtual?.senioridade?.name,
                    valorHora = usuario.valorHora ?: 0.0,
                    horasDisponiveis = 40,
                    projetosAtivos = projetosAtivos,
                    softSkills = usuarioSoftSkillRepository.findByUsuario(usuario)
                        .associate { it.softSkill.nome to it.nivel.toEstrela() },
                    competencias = usuarioCompetenciaRepository.findByUsuario(usuario)
                        .map { it.competencia.nome }
                )
            }
        }

        log.info("✅ {} usuários encontrados para cargo '{}' nível '{}'",
            filtrados.size, cargoDesejado, nivelSolicitado)
        return filtrados
    }

    private fun NivelSoftSkill.toEstrela(): Int = when (this) {
        NivelSoftSkill.HORRIVEL -> 0
        NivelSoftSkill.BAIXO -> 1
        NivelSoftSkill.MEDIO -> 3
        NivelSoftSkill.ALTO -> 4
        NivelSoftSkill.EXCELENTE -> 5
    }
}
