package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.LoginRepository
import com.fivegears.fivegears_backend.domain.repository.SessaoRepository
import com.fivegears.fivegears_backend.domain.repository.StatusUsuarioRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.LoginService
import com.fivegears.fivegears_backend.entity.Sessao
import com.fivegears.fivegears_backend.entity.Usuario
import com.fivegears.fivegears_backend.entity.enum.NivelPermissaoEnum
import com.fivegears.fivegears_backend.util.HashUtils
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

    override fun primeiroAcesso(email: String, novaSenha: String) {
        val login = loginRepository.findByUsuarioEmail(email)
            ?: throw RuntimeException("Usuário não encontrado")

        if (!login.primeiroAcesso) {
            throw RuntimeException("Usuário já realizou o primeiro acesso anteriormente.")
        }

        if (novaSenha.contains(login.usuario.email, ignoreCase = true)) {
            throw RuntimeException("A nova senha não pode conter o e-mail do usuário.")
        }

        // Criptografa e salva a nova senha
        login.senha = HashUtils.sha256(novaSenha)

        // Marca como concluído o primeiro acesso
        login.primeiroAcesso = false
        loginRepository.save(login)
    }

    override fun login(email: String, senha: String?): Usuario {
        if (senha.isNullOrBlank()) {
            throw RuntimeException("A senha não pode estar vazia")
        }

        val login = loginRepository.findByUsuarioEmail(email)
            ?: throw RuntimeException("Usuário não encontrado")

        // Verifica se ainda está no primeiro acesso
        if (login.primeiroAcesso) {
            throw RuntimeException("Usuário precisa realizar o primeiro acesso")
        }

        // Verifica a senha (hash local)
        val senhaHash = HashUtils.sha256(senha)
        if (login.senha != senhaHash) {
            throw RuntimeException("Senha incorreta")
        }

        val onlineStatus = statusUsuarioRepository.findById(1)
            .orElseThrow { RuntimeException("Status ONLINE não encontrado") }
        val offlineStatus = statusUsuarioRepository.findById(2)
            .orElseThrow { RuntimeException("Status OFFLINE não encontrado") }

        // Finaliza sessão anterior, se houver
        sessaoRepository.findByLoginIdAndFimSessaoIsNull(login.id!!)?.let {
            val nivel = login.usuario.nivelPermissao?.nome
            if (nivel == NivelPermissaoEnum.FUNCIONARIO || nivel == NivelPermissaoEnum.GERENTE) {
                it.status = offlineStatus
                it.fimSessao = LocalDateTime.now()
                sessaoRepository.save(it)
            }
        }

        // Cria nova sessão
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
        val sessaoAtiva = getSessaoAtiva(usuarioId)
            ?: throw RuntimeException("Sessão não encontrada")

        val offlineStatus = statusUsuarioRepository.findById(2)
            .orElseThrow { RuntimeException("Status OFFLINE não encontrado") }

        sessaoAtiva.status = offlineStatus
        sessaoAtiva.fimSessao = LocalDateTime.now()
        sessaoRepository.save(sessaoAtiva)
    }

    override fun getSessaoAtiva(usuarioId: Int): Sessao? {
        val login = loginRepository.findByUsuarioId(usuarioId) ?: return null
        return sessaoRepository.findByLoginIdAndFimSessaoIsNull(login.id!!)
    }
}
