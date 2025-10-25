package com.fivegears.fivegears_backend.dto

import com.fivegears.fivegears_backend.entity.enum.SenioridadeCargo

data class CargoDTO(
    val idCargo: Int? = null,
    val nome: String,
    val descricao: String? = null,
    val fonte: String? = "INTERNO"
)
