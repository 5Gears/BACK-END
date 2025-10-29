package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.config.GeminiConfig
import com.fivegears.fivegears_backend.dto.FiltroAlocacao
import com.fivegears.fivegears_backend.dto.UsuarioAlocadoDTO
import com.fivegears.fivegears_backend.entity.enum.NivelSoftSkill
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fivegears.fivegears_backend.domain.repository.*
import com.fivegears.fivegears_backend.entity.enum.SenioridadeCargo
import org.slf4j.LoggerFactory
import org.springframework.http.*
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
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

    // Configuração do RestTemplate com timeout de 10s
    private val restTemplate: RestTemplate by lazy {
        val factory = HttpComponentsClientHttpRequestFactory().apply {
            setConnectTimeout(10_000)
            setReadTimeout(10_000)
        }
        RestTemplate(factory)
    }

    // Prompt aprimorado mantendo o fluxo original
    private val promptBase = """
Você é o SunnyBOT, assistente de alocação de profissionais do sistema FiveGears.

Sua função é interpretar mensagens de gerentes de projeto e gerar filtros objetivos
para buscar usuários no banco de dados.

Responda apenas com JSON puro, sem texto extra.
Estrutura esperada:
{
  "cargoNome": "Programador",
  "cargoMinimo": "JUNIOR",
  "competencias": ["Kotlin", "Spring Boot"],
  "softSkills": ["Proatividade", "Trabalho em equipe"],
  "horasDisponiveisMin": 20,
  "valorHoraMax": 120.0
}

Regras:
- "cargoNome" deve conter o nome do cargo solicitado (ex: "Programador", "Analista de Dados", "Designer").
- "cargoMinimo" deve conter uma das opções exatas: ESTAGIARIO, JUNIOR, PLENO, SENIOR.
- Extraia palavras-chave técnicas da mensagem (competências).
- Extraia soft skills se forem mencionadas.
- Se mencionar "prazo curto" ou "dedicação parcial", ajuste horasDisponiveisMin.
- Se mencionar orçamento, ajuste valorHoraMax.
- Se não entender a solicitação, retorne {"erro": "Consulta não encontrada"}.
""".trimIndent()

    // Comandos válidos reconhecidos
    private val comandosPermitidos = listOf(
        "montar equipe",
        "alocar profissionais",
        "filtrar usuarios",
        "listar competencias",
        "listar softskills"
    )

    private fun validarMensagem(mensagem: String): Boolean {
        val mensagemLower = mensagem.lowercase()
        return comandosPermitidos.any { mensagemLower.contains(it) }
    }

    // Gera o filtro de busca baseado na mensagem
    fun gerarFiltro(mensagem: String): FiltroAlocacao {
        log.info(" Solicitando filtro ao Gemini para comando: \"{}\"", mensagem)

        if (!validarMensagem(mensagem)) {
            log.warn(" Comando inválido: '{}'", mensagem)
            return FiltroAlocacao()
        }

        val promptFinal = """
            $promptBase

            Comando: "$mensagem"
        """.trimIndent()

        val body = """{ "contents": [{"parts": [{"text": "$promptFinal"}]}] }"""
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(body, headers)
        val url = "${config.baseUrl}?key=${config.apiKey}"

        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)
            log.info("Gemini retornou status {}", response.statusCode.value())

            val texto = try {
                val json: JsonNode = mapper.readTree(response.body)
                val partes = json["candidates"]?.get(0)?.path("content")?.path("parts")
                partes?.get(0)?.path("text")?.asText() ?: "{}"
            } catch (e: Exception) {
                log.error(" Erro ao processar resposta do Gemini: {}", e.message)
                "{}"
            }

            val filtro = mapper.readValue(texto, FiltroAlocacao::class.java)
            log.info(" Filtro gerado com sucesso: {}", mapper.writeValueAsString(filtro))
            filtro
        } catch (e: Exception) {
            log.error(" Erro ao chamar Gemini API: {}", e.message)
            FiltroAlocacao()
        }
    }

    fun buscarUsuarios(filtro: FiltroAlocacao): List<UsuarioAlocadoDTO?> {
        log.info("Iniciando busca de usuários com filtro: {}", mapper.writeValueAsString(filtro))

        val usuarios = usuarioRepository.findAll()

        val nivelFiltro = filtro.cargoMinimo
        val cargoDesejado = filtro.cargoNome?.trim()?.lowercase()

        val filtrados = usuarios.filter { usuario ->
            val cargosUsuario = usuarioCargoRepository.findByUsuario(usuario)
            val cargoAtual = cargosUsuario.firstOrNull() ?: return@filter false

            // 1️⃣ Verifica se o cargo combina
            if (cargoDesejado != null && !cargoAtual.cargo!!.nome.trim().lowercase().contains(cargoDesejado))
                return@filter false

            // 2️⃣ Verifica se a senioridade combina
            if (cargoAtual.senioridade != nivelFiltro) return@filter false

            // 3️⃣ Filtra competências
            val competenciasUsuario = usuarioCompetenciaRepository.findByUsuario(usuario)
                .map { it.competencia.nome.trim().lowercase() }
                .toSet()
            if (!filtro.competencias.all { it.trim().lowercase() in competenciasUsuario }) return@filter false

            // 4️⃣ Filtra soft skills
            val softSkillsUsuario = usuarioSoftSkillRepository.findByUsuario(usuario)
                .associate { it.softSkill.nome.trim().lowercase() to it.nivel.toEstrela() }
            if (!filtro.softSkills.all { it.trim().lowercase() in softSkillsUsuario.keys }) return@filter false

            // 5️⃣ Filtra valor/hora
            usuario.valorHora?.let { if (it > filtro.valorHoraMax) return@filter false }

            // 6️⃣ Filtra disponibilidade
            val horasAtuais = usuarioProjetoRepository.findByUsuario(usuario)
                .filter { it.status.name == "ALOCADO" }
                .sumOf { it.horasPorDia }
            val horasDisponiveis = (40 - horasAtuais).coerceAtLeast(0)
            horasDisponiveis >= filtro.horasDisponiveisMin
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
                    horasDisponiveis = (40 - usuarioProjetoRepository.findByUsuario(usuario)
                        .filter { it.status.name == "ALOCADO" }
                        .sumOf { it.horasPorDia }).coerceAtLeast(0),
                    projetosAtivos = projetosAtivos,
                    softSkills = usuarioSoftSkillRepository.findByUsuario(usuario)
                        .associate { it.softSkill.nome to it.nivel.toEstrela() },
                    competencias = usuarioCompetenciaRepository.findByUsuario(usuario)
                        .map { it.competencia.nome }
                )
            }
        }

        log.info("→ {} usuários encontrados com cargo '{}' e nível '{}'", filtrados.size, cargoDesejado, nivelFiltro)
        return filtrados
    }



    //  Conversão de nível de soft skill em “estrelas”
    private fun NivelSoftSkill.toEstrela(): Int = when (this) {
        NivelSoftSkill.HORRIVEL -> 0
        NivelSoftSkill.BAIXO -> 1
        NivelSoftSkill.MEDIO -> 3
        NivelSoftSkill.ALTO -> 4
        NivelSoftSkill.EXCELENTE -> 5
    }
}
