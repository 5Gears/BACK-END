package com.fivegears.fivegears_backend.entity

import jakarta.persistence.Embeddable

@Embeddable
data class UsuarioCargoId(
    val idUsuario: Int? = 0,
    val idCargo: Int = 0
) : java.io.Serializable
