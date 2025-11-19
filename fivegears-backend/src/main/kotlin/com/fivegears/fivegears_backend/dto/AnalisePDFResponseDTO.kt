package com.fivegears.fivegears_backend.dto

data class AnalisePDFResponseDTO(
    val nomeProjeto: String?,
    val descricao: String?,
    val tempoEstimadoHoras: Int?,
    val orcamento: Double?,
    val dataInicio: String?,
    val dataFim: String?,
    val cliente: String?,
    val competencias: String?
)
