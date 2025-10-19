package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.*
import com.fivegears.fivegears_backend.dto.FeedbackRequestDTO
import com.fivegears.fivegears_backend.entity.UsuarioSoftSkill
import com.fivegears.fivegears_backend.entity.UsuarioSoftSkillFeedback
import com.fivegears.fivegears_backend.entity.enum.NivelPermissaoEnum
import com.fivegears.fivegears_backend.entity.enum.StatusProjeto
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.SoftSkillFeedbackService
import org.springframework.stereotype.Service

@Service
class SoftSkillFeedbackServiceImplementacao(
    private val projetoRepository: ProjetoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val usuarioSoftSkillFeedbackRepository: UsuarioSoftSkillFeedbackRepository,
    private val usuarioSoftSkillRepository: UsuarioSoftSkillRepository,
    private val softSkillRepository: SoftSkillRepository
) : SoftSkillFeedbackService {

    override fun avaliarSoftSkills(idAvaliador: Int, feedback: FeedbackRequestDTO) {
        // 🔹 Verifica se o avaliador existe e é gerente
        val avaliador = usuarioRepository.findById(idAvaliador)
            .orElseThrow { IllegalArgumentException("Usuário avaliador não encontrado.") }

        if (avaliador.nivelPermissao?.nome != NivelPermissaoEnum.GERENTE) {
            throw IllegalAccessException("Somente gerentes podem realizar avaliações de soft skills.")
        }

        // 🔹 Verifica se o projeto foi concluído
        val projeto = projetoRepository.findById(feedback.idProjeto)
            .orElseThrow { IllegalArgumentException("Projeto não encontrado.") }

        if (projeto.status != StatusProjeto.CONCLUIDO) {
            throw IllegalStateException("O projeto ainda não foi concluído.")
        }

        // 🔹 Realiza avaliação das soft skills
        feedback.avaliacoes.forEach { avaliacao ->
            val skill = softSkillRepository.findById(avaliacao.idSoftSkill)
                .orElseThrow { IllegalArgumentException("Soft skill com ID ${avaliacao.idSoftSkill} não encontrada.") }

            val avaliado = usuarioRepository.findById(feedback.idUsuarioAvaliado)
                .orElseThrow { IllegalArgumentException("Usuário avaliado não encontrado.") }

            usuarioSoftSkillFeedbackRepository.save(
                UsuarioSoftSkillFeedback(
                    usuario = avaliado,
                    avaliador = avaliador,
                    softSkill = skill,
                    nivel = avaliacao.nivel,
                    comentario = avaliacao.comentario
                )
            )

            val existente = usuarioSoftSkillRepository.findByUsuarioIdAndSoftSkillId(
                feedback.idUsuarioAvaliado, avaliacao.idSoftSkill
            )

            if (existente != null) {
                existente.nivel = avaliacao.nivel
                existente.comentario = avaliacao.comentario ?: existente.comentario
                usuarioSoftSkillRepository.save(existente)
            } else {
                usuarioSoftSkillRepository.save(
                    UsuarioSoftSkill(
                        usuario = avaliado,
                        softSkill = skill,
                        nivel = avaliacao.nivel,
                        comentario = avaliacao.comentario
                    )
                )
            }
        }

        println("✅ Avaliação concluída por ${avaliador.nome} para o usuário ${feedback.idUsuarioAvaliado}")
    }
}
