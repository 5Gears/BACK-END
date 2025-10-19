package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.UsuarioCompetenciaRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioProjetoRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioSoftSkillRepository
import com.fivegears.fivegears_backend.entity.Usuario
import org.springframework.stereotype.Service

@Service
class AlocacaoService(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioCompetenciaRepository: UsuarioCompetenciaRepository,
    private val usuarioSoftSkillRepository: UsuarioSoftSkillRepository,
    private val usuarioProjetoRepository: UsuarioProjetoRepository
) {

    fun buscarUsuarios(competencias: List<String>, softSkills: List<String>, horasDisponiveisMin: Int): List<Usuario> {
        val todosUsuarios = usuarioRepository.findAll()

        return todosUsuarios.filter { usuario ->
            // Competências
            val usuarioComCompetencias = usuarioCompetenciaRepository.findAll()
                .filter { it.usuario.id == usuario.id }
                .map { it.competencia.nome }

            val temCompetencias = competencias.all { usuarioComCompetencias.contains(it) }

            // Soft skills
            val usuarioComSoftSkills = usuarioSoftSkillRepository.findAll()
                .filter { it.usuario.id == usuario.id }
                .map { it.softSkill.nome }

            val temSoftSkills = softSkills.all { usuarioComSoftSkills.contains(it) }

            // Horas disponíveis
            val horasAlocadas = usuarioProjetoRepository.findAll()
                .filter { it.usuario?.id == usuario.id }
                .sumOf { it.horasAlocadas }

            val horasDisponiveis = usuario.cargaHoraria - horasAlocadas
            val temHoras = horasDisponiveis >= horasDisponiveisMin

            temCompetencias && temSoftSkills && temHoras
        }
    }
}
