package school.sptech.projetotfg.service

import org.springframework.stereotype.Service
import school.sptech.projetotfg.domain.Login
import school.sptech.projetotfg.domain.StatusUsuario
import school.sptech.projetotfg.dto.UsuarioLoginDTO
import school.sptech.projetotfg.repository.LoginRepository
import school.sptech.projetotfg.repository.StatusUsuarioRepository
import school.sptech.projetotfg.repository.UsuarioRepository
import java.time.LocalDateTime

@Service
class AuthService(
    private val usuarioRepository: UsuarioRepository,
    private val loginRepository: LoginRepository,
    private val statusUsuarioRepository: StatusUsuarioRepository
) {

    fun login(login: Login): UsuarioLoginDTO {
        val usuario = usuarioRepository.findByEmail(login.email)
            ?: throw Exception("Usuário não encontrado.")

        val loginInfo = loginRepository.findByUsuarioId(usuario.id)
            ?: throw Exception("Login não encontrado.")

        if (login.senha != loginInfo.senha)
            throw Exception("Senha incorreta.")

        // Atualiza data de login
        loginRepository.updateUltimoLogin(usuario.id, LocalDateTime.now())

        // Cria novo status ONLINE
        val novoStatus = StatusUsuario(
            id = null,
            usuario = usuario,
            dataEntrada = LocalDateTime.now(),
            dataSaida = null,
            statusAtual = "ONLINE"
        )
        statusUsuarioRepository.save(novoStatus)

        return UsuarioLoginDTO(usuario.id, usuario.nome, usuario.email, status = novoStatus)
    }


    fun logout(idUsuario: Long) {
        val statusAtual = statusUsuarioRepository
            .findTopByUsuarioIdOrderByDataEntradaDesc(idUsuario)
            ?: throw Exception("Status não encontrado.")

        // Atualiza o status existente
        val statusAtualizado = statusAtual.copy(
            dataSaida = LocalDateTime.now(),
            statusAtual = "OFFLINE"
        )
        statusUsuarioRepository.save(statusAtualizado)
    }

}
