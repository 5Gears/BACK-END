package com.fivegears.fivegears_backend.dto

data class EnderecoDTO(
    val id: Int? = null,
    val rua: String,
    val numero: String? = null,
    val bairro: String? = null,
    val cidade: String? = null,
    val estado: String? = null,
    val cep: String? = null
)