package com.fivegears.fivegears_backend.dto

data class CompetenciaDTO(
    val idCompetencia: Int,
    val nome: String,
    val descricao: String?,
    val tipo: String?,
    val categoria: String?,
    val nivelRecomendado: String
)