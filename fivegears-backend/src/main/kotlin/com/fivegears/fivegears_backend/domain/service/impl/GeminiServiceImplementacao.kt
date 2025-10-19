package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.config.GeminiConfig
import com.fivegears.fivegears_backend.dto.FiltroAlocacao
import com.fivegears.fivegears_backend.dto.UsuarioAlocadoDTO
import com.fivegears.fivegears_backend.entity.enum.NivelSoftSkill
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fivegears.fivegears_backend.domain.repository.*
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

    private val restTemplate = RestTemplate()
    private val mapper = ObjectMapper()

    private val promptBase = """
        Você é um assistente de banco de dados FiveGears.
        Regras:
        1. Não invente informações.
        2. Retorne apenas filtros JSON para buscar usuários.
        3. Se não souber a resposta ou a consulta não for permitida, retorne {"erro": "Consulta não encontrada"}.
    """.trimIndent()

    private val comandosPermitidos = listOf(
        "listar competencias do usuario",
        "listar softskills do usuario",
        "montar equipe com"
    )

    private fun validarMensagem(mensagem: String): Boolean {
        val mensagemLower = mensagem.lowercase()
        return comandosPermitidos.any { mensagemLower.contains(it) }
    }

    // Gera filtro via Gemini (sugestão JSON)
    fun gerarFiltro(mensagem: String): FiltroAlocacao {
        if (!validarMensagem(mensagem)) return FiltroAlocacao()

        val promptFinal = """
            $promptBase
            Comando do gerente: "$mensagem"
            Responda apenas com JSON que contenha:
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
        val response = restTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)

        val texto = try {
            val json: JsonNode = mapper.readTree(response.body)
            json["candidates"]?.get(0)?.get("content")?.get("parts")?.get(0)?.asText() ?: "{}"
        } catch (e: Exception) {
            "{}"
        }

        return try {
            mapper.readValue(texto, FiltroAlocacao::class.java)
        } catch (e: Exception) {
            FiltroAlocacao()
        }
    }

    // Retorna usuários filtrados do banco conforme o JSON do Gemini
    fun buscarUsuarios(filtro: FiltroAlocacao): List<UsuarioAlocadoDTO> {
        val usuarios = usuarioRepository.findAll()

        return usuarios.filter { usuario ->
            // Cargo mínimo
            val cargosUsuario = usuarioCargoRepository.findByUsuario(usuario)
            val atendeCargo = cargosUsuario.any { it.cargo.senioridade >= filtro.cargoMinimo }
            if (!atendeCargo) return@filter false

            // Competências
            val competenciasUsuario = usuarioCompetenciaRepository.findByUsuario(usuario)
                .map { it.competencia.nome }
            if (!filtro.competencias.all { competenciasUsuario.contains(it) }) return@filter false

            // Soft skills
            val softSkillsUsuario = usuarioSoftSkillRepository.findByUsuario(usuario)
                .associate { it.softSkill.nome to it.nivel.toEstrela() }
            if (!filtro.softSkills.all { softSkillsUsuario.containsKey(it) }) return@filter false

            // Valor hora
            if (usuario.valorHora > filtro.valorHoraMax) return@filter false

            // Horas disponíveis
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
    }

    // Extensão para converter nível soft skill em estrelas
    private fun NivelSoftSkill.toEstrela(): Int = when (this) {
        NivelSoftSkill.HORRIVEL -> 0
        NivelSoftSkill.BAIXO -> 1
        NivelSoftSkill.MEDIO -> 3
        NivelSoftSkill.ALTO -> 4
        NivelSoftSkill.EXCELENTE -> 5
    }
}
