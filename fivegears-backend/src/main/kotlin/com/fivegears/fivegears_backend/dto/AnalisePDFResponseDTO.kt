package com.fivegears.fivegears_backend.dto

data class AnalisePDFResponseDTO(
    val draftId: String,
    val descricao: String,
    val competencias: String?
)