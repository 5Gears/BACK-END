package com.fivegears.fivegears_backend.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ProjetoRequestDTO(
    val nome: String,
    val descricao: String? = null,
    val tempoEstimadoHoras: Int? = null,
    val orcamento: BigDecimal? = null,
    val dataInicio: LocalDate? = null,
    val dataFim: LocalDate? = null,
    val clienteId: Int?,
    val responsavelId: Int,
    val competenciasRequeridas: String? = null
)