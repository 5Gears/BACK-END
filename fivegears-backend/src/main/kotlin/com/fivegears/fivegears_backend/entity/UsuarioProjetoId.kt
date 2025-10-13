package com.fivegears.fivegears_backend.entity

import java.io.Serializable

data class UsuarioProjetoId(
    val projeto: Int? = null,
    val usuario: Int? = null
) : Serializable