package com.fivegears.fivegears_backend.dto

data class UsuarioDTO(
    val id: Int? = null,
    val nome: String,
    val email: String,
    val cpf: String? = null,
    val telefone: String? = null,
    val idEmpresa: Int? = null,
    val idNivelPermissao: Int? = null,
    val idStatusUsuario: Int? = null
)
