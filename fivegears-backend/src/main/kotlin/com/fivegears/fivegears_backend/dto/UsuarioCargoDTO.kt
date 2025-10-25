package com.fivegears.fivegears_backend.dto

data class UsuarioCargoDTO(
    val idUsuario: Int,
    val idCargo: Int,
    val nomeCargo: String,
    val senioridade: String,
    val experienciaAnos: Int
)