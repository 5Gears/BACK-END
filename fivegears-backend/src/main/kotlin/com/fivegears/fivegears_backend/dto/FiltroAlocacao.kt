package com.fivegears.fivegears_backend.dto

import com.fivegears.fivegears_backend.entity.enum.Senioridade

data class FiltroAlocacao(
    val competencias: List<String> = emptyList(),
    val cargoMinimo: Senioridade = Senioridade.ESTAGIARIO,
    val horasDisponiveisMin: Int = 0,
    val valorHoraMax: Double = Double.MAX_VALUE,
    val softSkills: List<String> = emptyList()
)
