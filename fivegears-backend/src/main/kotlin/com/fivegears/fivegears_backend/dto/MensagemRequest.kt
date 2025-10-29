package com.fivegears.fivegears_backend.dto

data class MensagemRequest(
    val idProjeto: Int? = null,
    val nomeProjeto: String,
    val mensagem: String
)