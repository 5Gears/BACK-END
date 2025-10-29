package com.fivegears.fivegears_backend.dto

import java.math.BigDecimal
import java.time.LocalDate

data class ProjetoRequestDTO(
    val nome: String? = null,
    val descricao: String? = null,
    val tempoEstimadoHoras: Int? = null,
    val orcamento: BigDecimal? = null,
    val dataInicio: LocalDate? = null,
    val dataFim: LocalDate? = null,
    val clienteId: Int? = null,
    val responsavelId: Int? = null,
    val competenciasRequeridas: String? = null
)