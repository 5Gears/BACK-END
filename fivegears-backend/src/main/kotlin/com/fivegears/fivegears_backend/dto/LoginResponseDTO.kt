package com.fivegears.fivegears_backend.dto


data class LoginResponseDTO
    (
    val usuarioId: Int,
    val nome: String,
    val token: String
            )