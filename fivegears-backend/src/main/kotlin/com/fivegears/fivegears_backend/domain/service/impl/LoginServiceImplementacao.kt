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

    /**
     * Verifica se o e-mail existe e retorna se é primeiro acesso.
     */
    override fun verificarPrimeiroAcesso(email: String): Boolean {
        val login = loginRepository.findByUsuarioEmail(email)
            ?: throw RuntimeException("Usuário não encontrado")

        return login.primeiroAcesso
    }

    override fun primeiroAcesso(email: String, senhaTemporaria: String, novaSenha: String) {

        val login = loginRepository.findByUsuarioEmail(email)
            ?: throw RuntimeException("Usuário não encontrado")

        if (!login.primeiroAcesso) {
            throw RuntimeException("Usuário já realizou o primeiro acesso anteriormente.")
        }

        // 👉 Valida a senha temporária (com hash)
        val hashTemporariaDigitada = HashUtils.sha256(senhaTemporaria)
        if (login.senha != hashTemporariaDigitada) {
            throw RuntimeException("Senha temporária inválida")
        }

        // 👉 Regras de segurança
        if (novaSenha.contains(login.usuario.email, ignoreCase = true)) {
            throw RuntimeException("A nova senha não pode conter o e-mail do usuário.")
        }

        // 👉 Salva nova senha com hash
        login.senha = HashUtils.sha256(novaSenha)
        login.primeiroAcesso = false

        loginRepository.save(login)
    }

    /**
     * Login normal
     */
    override fun login(email: String, senha: String?): Usuario {
        if (senha.isNullOrBlank()) {
            throw RuntimeException("A senha não pode estar vazia")
        }

        val login = loginRepository.findByUsuarioEmail(email)
            ?: throw RuntimeException("Usuário não encontrado")

        if (login.primeiroAcesso) {
            throw RuntimeException("Usuário precisa realizar o primeiro acesso")
        }

        val senhaHash = HashUtils.sha256(senha)
        if (login.senha != senhaHash) {
            throw RuntimeException("Senha incorreta")
        }

        // 👇 Sessão anterior
        val onlineStatus = statusUsuarioRepository.findById(1)
            .orElseThrow { RuntimeException("Status ONLINE não encontrado") }
        val offlineStatus = statusUsuarioRepository.findById(2)
            .orElseThrow { RuntimeException("Status OFFLINE não encontrado") }

        sessaoRepository.findByLoginIdAndFimSessaoIsNull(login.id!!)?.let {
            it.status = offlineStatus
            it.fimSessao = LocalDateTime.now()
            sessaoRepository.save(it)
        }

        // 👇 Nova sessão
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
