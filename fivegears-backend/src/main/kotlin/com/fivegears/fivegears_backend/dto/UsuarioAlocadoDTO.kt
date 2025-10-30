package com.fivegears.fivegears_backend.dto

import java.math.BigDecimal

data class UsuarioAlocadoDTO(
    val id: Int,
    val nome: String,
    val email: String?,
    val cargo: String?,
    val idCargo: Int?,
    val senioridade: String?,
    val valorHora: BigDecimal,
    val horasDisponiveis: Int,
    val projetosAtivos: List<String>,
    val softSkills: Map<String, Int>,
    val competencias: List<String>
)