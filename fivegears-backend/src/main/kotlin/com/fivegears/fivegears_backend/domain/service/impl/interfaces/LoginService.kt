package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.entity.Usuario

interface LoginService {
    fun login(email: String, senha: String): Usuario
    fun logout(usuarioId: Int)
}
