package com.fivegears.fivegears_backend.dto

data class CargoDTO(
    val idCargo: Int,
    val nome: String,
    val descricao: String?,
    val senioridade: String
)