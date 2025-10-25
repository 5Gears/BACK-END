package com.fivegears.fivegears_backend.dto

import java.math.BigDecimal

data class UsuarioDTO(
    val idUsuario: Int? = null,
    val nome: String,
    val email: String,
    val cpf: String? = null,
    val telefone: String? = null,
    val area: String? = null,
    val cargaHoraria: Int? = null,
    val valorHora: Double? = null,
    val idEmpresa: Int? = null,
    val idNivel: Int? = null,
    val idCargo: Int? = null,
    val cargoNome: String? = null,
    val senioridade: String? = null,
    val experienciaAnos: Int? = null
)
