package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.LoginRepository
import com.fivegears.fivegears_backend.domain.repository.StatusUsuarioRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.LoginService
import com.fivegears.fivegears_backend.entity.StatusUsuario
import com.fivegears.fivegears_backend.entity.Usuario
import org.springframework.stereotype.Service

@Service
class LoginServiceImplementacao(
    private val loginRepository: LoginRepository,
    private val usuarioRepository: UsuarioRepository,
    private val statusUsuarioRepository: StatusUsuarioRepository
) : LoginService {

    override fun login(email: String, senha: String): Usuario {
        val login = loginRepository.findAll().find { it.usuario.email == email }
            ?: throw RuntimeException("Usuário não encontrado")

        if (login.senha != senha) {
            throw RuntimeException("Senha incorreta")
        }

        val onlineStatus = statusUsuarioRepository.findById(1)
            .orElseThrow { RuntimeException("Status ONLINE não encontrado") }

        login.usuario.statusUsuario = onlineStatus
        login.ultimoLogin = java.time.LocalDateTime.now()
        usuarioRepository.save(login.usuario)

        return login.usuario
    }

    override fun logout(usuarioId: Int) {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }

        val offlineStatus = statusUsuarioRepository.findById(2) // ID do OFFLINE
            .orElseThrow { RuntimeException("Status OFFLINE não encontrado") }

        usuario.statusUsuario = offlineStatus
        usuarioRepository.save(usuario)
    }
}
