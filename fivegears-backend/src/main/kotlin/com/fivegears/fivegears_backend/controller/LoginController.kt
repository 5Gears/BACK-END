package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.repository.LoginRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.LoginService
import com.fivegears.fivegears_backend.dto.LoginRequestDTO
import com.fivegears.fivegears_backend.dto.LoginResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/login")
class LoginController(
    private val loginService: LoginService,
    private val loginRepository: LoginRepository
) {

    @PostMapping("/primeiro-acesso")
    fun primeiroAcesso(@RequestBody dto: Map<String, String>): ResponseEntity<Any> {
        return try {
            val email = dto["email"] ?: return ResponseEntity
                .badRequest()
                .body(mapOf("erro" to "E-mail não informado"))

            val novaSenha = dto["senha"] ?: return ResponseEntity
                .badRequest()
                .body(mapOf("erro" to "Nova senha não informada"))

            loginService.primeiroAcesso(email, novaSenha)
            ResponseEntity.ok(mapOf("mensagem" to "Senha alterada com sucesso!"))
        } catch (e: RuntimeException) {
            val status = when {
                e.message?.contains("já alterada") == true -> HttpStatus.CONFLICT
                e.message?.contains("não pode conter") == true -> HttpStatus.BAD_REQUEST
                e.message?.contains("não encontrado") == true -> HttpStatus.NOT_FOUND
                else -> HttpStatus.BAD_REQUEST
            }
            ResponseEntity.status(status).body(mapOf("erro" to e.message))
        }
    }

    @PostMapping
    fun login(@RequestBody dto: LoginRequestDTO): ResponseEntity<Any> {
        return try {
            val usuario = loginService.login(dto.email, dto.senha)
            val sessao = usuario.id?.let { loginService.getSessaoAtiva(it) }

            // Busca o login do usuário para verificar o primeiroAcesso
            val login = loginRepository.findByUsuarioEmail(dto.email)
                ?: throw RuntimeException("Login não encontrado para o usuário informado.")

            ResponseEntity.ok(
                LoginResponseDTO(
                    id = usuario.id!!,
                    nome = usuario.nome,
                    email = usuario.email,
                    token = sessao?.token ?: "",
                    primeiroAcesso = login.primeiroAcesso
                )
            )
        } catch (e: RuntimeException) {
            val status = when {
                e.message?.contains("não encontrado", ignoreCase = true) == true -> HttpStatus.NOT_FOUND
                e.message?.contains("incorreta", ignoreCase = true) == true -> HttpStatus.UNAUTHORIZED
                e.message?.contains("vazia", ignoreCase = true) == true -> HttpStatus.BAD_REQUEST
                e.message?.contains("primeiro acesso", ignoreCase = true) == true -> HttpStatus.UNAUTHORIZED
                else -> HttpStatus.BAD_REQUEST
            }
            ResponseEntity.status(status).body(mapOf("erro" to e.message))
        }
    }

    @PostMapping("/logout/{usuarioId}")
    fun logout(@PathVariable usuarioId: Int): ResponseEntity<Any> {
        return try {
            loginService.logout(usuarioId)
            ResponseEntity.ok(mapOf("mensagem" to "Logout realizado com sucesso"))
        } catch (e: RuntimeException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("erro" to e.message))
        }
    }
}
