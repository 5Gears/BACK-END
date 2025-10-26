package com.fivegears.fivegears_backend.dto


data class LoginResponseDTO(
    val id: Int,
    val nome: String,
    val email: String,
    val token: String,
    val primeiroAcesso: Boolean
)