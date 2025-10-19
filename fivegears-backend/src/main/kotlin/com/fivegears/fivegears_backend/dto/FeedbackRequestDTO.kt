package com.fivegears.fivegears_backend.dto

data class FeedbackRequestDTO(
    val idUsuarioAvaliado: Int,
    val idProjeto: Int,
    val avaliacoes: List<AvaliacaoSoftSkillDTO>
)