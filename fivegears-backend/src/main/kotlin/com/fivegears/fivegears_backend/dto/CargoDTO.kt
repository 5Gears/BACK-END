package com.fivegears.fivegears_backend.dto

import com.fivegears.fivegears_backend.entity.enum.Senioridade

data class CargoDTO(
    val idCargo: Int? = null,
    val nome: String,
    val descricao: String? = null,
    val senioridade: Senioridade,
    val fonte: String? = "INTERNO"
)
