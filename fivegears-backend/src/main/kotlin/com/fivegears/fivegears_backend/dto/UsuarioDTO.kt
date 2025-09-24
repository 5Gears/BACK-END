package com.fivegears.fivegears_backend.dto

data class UsuarioDTO(
    val id: Int? = null,
    val nome: String,
    val email: String,
    val cpf: String? = null,
    val telefone: String? = null,
    val area: String? = null,
    val cargaHoraria: Int = 0,
    val valorHora: Double = 0.0,
    val idEmpresa: Int? = null,
    val idNivel: Int? = null
)
