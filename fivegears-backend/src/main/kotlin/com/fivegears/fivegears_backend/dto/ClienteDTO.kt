package com.fivegears.fivegears_backend.dto

data class ClienteDTO(
    val id: Int? = null,
    val nome: String,
    val cnpj: String,
    val emailResponsavel: String? = null,
    val enderecos: List<EnderecoDTO> = emptyList()
)