package com.fivegears.fivegears_backend.entity

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UsuarioCompetenciaId(
    val idUsuario: Int? = null,
    val idCompetencia: Int? = null
) : Serializable
