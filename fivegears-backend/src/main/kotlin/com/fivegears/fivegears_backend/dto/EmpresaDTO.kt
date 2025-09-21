package com.fivegears.fivegears_backend.dto

data class EmpresaDTO(
    val id: Int? = null,
    val nome: String,
    val fundador: String? = null,
    val cnpj: String,
    val enderecos: List<EnderecoDTO> = emptyList()
)