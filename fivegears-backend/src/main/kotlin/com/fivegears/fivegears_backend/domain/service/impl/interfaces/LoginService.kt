package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.entity.Sessao
import com.fivegears.fivegears_backend.entity.Usuario

interface LoginService {

    fun verificarPrimeiroAcesso(email: String): Boolean
    fun primeiroAcesso(email: String, senhaTemporaria: String, novaSenha: String)
    fun login(email: String, senha: String?): Usuario
    fun logout(usuarioId: Int)
    fun getSessaoAtiva(usuarioId: Int): Sessao?
}
