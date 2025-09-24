package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.LoginRepository
import com.fivegears.fivegears_backend.domain.repository.SessaoRepository
import com.fivegears.fivegears_backend.domain.repository.StatusUsuarioRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.LoginService
import com.fivegears.fivegears_backend.entity.Sessao
import com.fivegears.fivegears_backend.entity.StatusUsuario
import com.fivegears.fivegears_backend.entity.Usuario
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class LoginServiceImplementacao(
    private val loginRepository: LoginRepository,
    private val usuarioRepository: UsuarioRepository,
    private val sessaoRepository: SessaoRepository,
    private val statusUsuarioRepository: StatusUsuarioRepository
) : LoginService {

    override fun login(email: String, senha: String): Usuario {
        // Busca o login pelo email do usuário
        val login = loginRepository.findAll().find { it.usuario.email == email }
            ?: throw RuntimeException("Usuário não encontrado")

        if (login.senha != senha) {
            throw RuntimeException("Senha incorreta")
        }

        // Busca o status ONLINE
        val onlineStatus = statusUsuarioRepository.findById(1)
            .orElseThrow { RuntimeException("Status ONLINE não encontrado") }

        // Busca o status OFFLINE (para possível logout)
        val offlineStatus = statusUsuarioRepository.findById(2)
            .orElseThrow { RuntimeException("Status OFFLINE não encontrado") }

        // Verifica se já existe uma sessão ativa
        val sessaoAtiva = sessaoRepository.findByLoginIdAndFimSessaoIsNull(login.id!!)

        // Se já estiver online e for FUNCIONARIO ou GERENTE, desconecta a sessão antiga
        sessaoAtiva?.let {
            val nivel = login.usuario.nivelPermissao?.nome
            if (nivel == "FUNCIONARIO" || nivel == "GERENTE") {
                it.status = offlineStatus
                it.fimSessao = LocalDateTime.now()
                sessaoRepository.save(it)
            } else {
                // Para outros níveis, podemos lançar exceção ou permitir múltiplas sessões
            }
        }

        // Cria uma nova sessão
        val novaSessao = Sessao(
            login = login,
            status = onlineStatus,
            token = UUID.randomUUID().toString(),
            inicioSessao = LocalDateTime.now()
        )
        sessaoRepository.save(novaSessao)

        return login.usuario
    }

    override fun logout(usuarioId: Int) {
        val usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow { RuntimeException("Usuário não encontrado") }

        val offlineStatus = statusUsuarioRepository.findById(2)
            .orElseThrow { RuntimeException("Status OFFLINE não encontrado") }

        // Encontra a sessão ativa do usuário (login)
        val sessaoAtiva = usuario.id?.let {
            sessaoRepository.findByLoginIdAndFimSessaoIsNull(
                usuarioRepository.findById(it).get().let { u ->
                    u.id?.let { loginId -> loginRepository.findAll().find { l -> l.usuario.id == loginId }!!.id!! }
                } ?: throw RuntimeException("Login não encontrado")
            )
        }

        sessaoAtiva?.let {
            it.status = offlineStatus
            it.fimSessao = LocalDateTime.now()
            sessaoRepository.save(it)
        }
    }
}
