package com.fivegears.fivegears_backend.entity

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UsuarioCargoId(
    val idUsuario: Int? = null,
    val idCargo: Int? = null
) : Serializable
