package com.fivegears.fivegears_backend.dto

import com.fivegears.fivegears_backend.entity.enum.SenioridadeCargo
import java.math.BigDecimal

data class FiltroAlocacao(
    val competencias: List<String> = emptyList(),
    val cargoNome: String? = null,
    val cargoMinimo: SenioridadeCargo = SenioridadeCargo.ESTAGIARIO,
    val horasDisponiveisMin: Int = 0,
    val valorHoraMax: BigDecimal,
    val softSkills: List<String> = emptyList()
)
