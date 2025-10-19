package com.fivegears.fivegears_backend.dto

import com.fivegears.fivegears_backend.entity.enum.NivelSoftSkill

data class AvaliacaoSoftSkillDTO(
    val idSoftSkill: Int,
    val nivel: NivelSoftSkill,
    val comentario: String? = null
)