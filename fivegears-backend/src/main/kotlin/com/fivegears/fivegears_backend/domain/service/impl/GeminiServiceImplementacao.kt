package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.config.GeminiConfig
import com.fivegears.fivegears_backend.dto.FiltroAlocacao
import com.fivegears.fivegears_backend.dto.UsuarioAlocadoDTO
import com.fivegears.fivegears_backend.entity.enum.NivelSoftSkill
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fivegears.fivegears_backend.domain.repository.*
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

    // Configura o RestTemplate com timeout de 10 segundos
    private val restTemplate: RestTemplate by lazy {
        val factory = HttpComponentsClientHttpRequestFactory().apply {
            setConnectTimeout(10_000)
            setReadTimeout(10_000)
        }
        RestTemplate(factory)
    }

    private val promptBase = """
        Você é um assistente do sistema FiveGears.
        Sua função é interpretar comandos do gerente e gerar filtros JSON
        para buscar usuários do banco.
        
         Regras:
        - Responda **apenas** com JSON puro.
        - Não adicione comentários ou texto fora do JSON.
        - Se não souber responder, retorne {"erro": "Consulta não encontrada"}.
    """.trimIndent()

    private val comandosPermitidos = listOf(
        "listar competencias do usuario",
        "listar softskills do usuario",
        "montar equipe com",
        "filtrar usuarios com"
    )

    private fun validarMensagem(mensagem: String): Boolean {
        val mensagemLower = mensagem.lowercase()
        return comandosPermitidos.any { mensagemLower.contains(it) }
    }

    //  Gera o filtro de busca com base na mensagem do gerente
    fun gerarFiltro(mensagem: String): FiltroAlocacao {
        log.info(" Solicitando filtro ao Gemini para comando: \"{}\"", mensagem)

        if (!validarMensagem(mensagem)) {
            log.warn("🚫 Comando inválido: '{}'", mensagem)
            return FiltroAlocacao()
        }

        val promptFinal = """
            $promptBase
            
            Comando: "$mensagem"
            
            Retorne um JSON no formato:
            {
              "competencias": ["Kotlin", "SQL"],
              "cargoMinimo": "PLENO",
              "horasDisponiveisMin": 20,
              "valorHoraMax": 150.0,
              "softSkills": ["Comunicação", "Trabalho em equipe"]
            }
        """.trimIndent()

        val body = """{ "contents": [{"parts": [{"text": "$promptFinal"}]}] }"""
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(body, headers)
        val url = "${config.baseUrl}?key=${config.apiKey}"

        return try {
            val response = restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)
            log.info(" Gemini retornou status {}", response.statusCode.value())


            // Parsing robusto
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

    //  Filtra os usuários de acordo com o filtro gerado
    fun buscarUsuarios(filtro: FiltroAlocacao): List<UsuarioAlocadoDTO> {
        log.info("🔎 Iniciando busca de usuários com filtro: {}", mapper.writeValueAsString(filtro))
        val usuarios = usuarioRepository.findAll()

        val filtrados = usuarios.filter { usuario ->
            val cargosUsuario = usuarioCargoRepository.findByUsuario(usuario)
            val atendeCargo = cargosUsuario.any { it.cargo.senioridade >= filtro.cargoMinimo }
            if (!atendeCargo) return@filter false

            val competenciasUsuario = usuarioCompetenciaRepository.findByUsuario(usuario)
                .map { it.competencia.nome }
            if (!filtro.competencias.all { competenciasUsuario.contains(it) }) return@filter false

            val softSkillsUsuario = usuarioSoftSkillRepository.findByUsuario(usuario)
                .associate { it.softSkill.nome to it.nivel.toEstrela() }
            if (!filtro.softSkills.all { softSkillsUsuario.containsKey(it) }) return@filter false

            if (usuario.valorHora > filtro.valorHoraMax) return@filter false

            val horasAtuais = usuarioProjetoRepository.findByUsuario(usuario)
                .filter { it.status.name == "ALOCADO" }
                .sumOf { it.horasPorDia }
            if ((40 - horasAtuais) < filtro.horasDisponiveisMin) return@filter false

            true
        }.map { usuario ->
            val cargos = usuarioCargoRepository.findByUsuario(usuario)
            val softSkills = usuarioSoftSkillRepository.findByUsuario(usuario)
                .associate { it.softSkill.nome to it.nivel.toEstrela() }
            val competencias = usuarioCompetenciaRepository.findByUsuario(usuario)
                .map { it.competencia.nome }
            val projetos = usuarioProjetoRepository.findByUsuario(usuario)
                .filter { it.status.name == "ALOCADO" }
                .map { it.projeto!!.nome }

            UsuarioAlocadoDTO(
                id = usuario.id,
                nome = usuario.nome,
                email = usuario.email,
                cargo = cargos.firstOrNull()?.cargo?.nome,
                senioridade = cargos.firstOrNull()?.cargo?.senioridade?.name,
                valorHora = usuario.valorHora,
                horasDisponiveis = 40 - usuarioProjetoRepository.findByUsuario(usuario)
                    .filter { it.status.name == "ALOCADO" }
                    .sumOf { it.horasPorDia },
                projetosAtivos = projetos,
                softSkills = softSkills,
                competencias = competencias
            )
        }

        log.info(" {} usuários encontrados pelo filtro.", filtrados.size)
        return filtrados
    }

    //  Conversão de nível de soft skill em "estrelas"
    private fun NivelSoftSkill.toEstrela(): Int = when (this) {
        NivelSoftSkill.HORRIVEL -> 0
        NivelSoftSkill.BAIXO -> 1
        NivelSoftSkill.MEDIO -> 3
        NivelSoftSkill.ALTO -> 4
        NivelSoftSkill.EXCELENTE -> 5
    }
}
