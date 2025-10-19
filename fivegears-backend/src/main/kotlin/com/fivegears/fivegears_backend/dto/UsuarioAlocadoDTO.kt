package com.fivegears.fivegears_backend.dto

data class UsuarioAlocadoDTO(
    val id: Int?,
    val nome: String,
    val email: String,
    val cargo: String?,
    val senioridade: String?,
    val valorHora: Double,
    val horasDisponiveis: Int,
    val projetosAtivos: List<String>,
    val softSkills: Map<String, Int>,
    val competencias: List<String>
)