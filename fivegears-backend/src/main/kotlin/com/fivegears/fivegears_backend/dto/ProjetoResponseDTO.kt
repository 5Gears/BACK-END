package com.fivegears.fivegears_backend.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ProjetoResponseDTO(
    val id: Int,
    val nome: String,
    val descricao: String?,
    val tempoEstimadoHoras: Int?,
    val orcamento: BigDecimal?,
    val status: String,
    val dataInicio: LocalDate?,
    val dataFim: LocalDate?,
    val clienteId: Int?,
    val clienteNome: String?,
    val responsavelId: Int?,
    val responsavelNome: String?,
    val competenciasRequeridas: String?
)