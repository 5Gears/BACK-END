package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.interfaces.LoginService
import com.fivegears.fivegears_backend.dto.LoginRequestDTO
import com.fivegears.fivegears_backend.dto.LoginResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/login")
class LoginController(
    private val loginService: LoginService
) {

    @PostMapping("/primeiro-acesso/{usuarioId}")
    fun primeiroAcesso(
        @PathVariable usuarioId: Int,
        @RequestBody dto: Map<String, String>
    ): ResponseEntity<Void> {
        val novaSenha = dto["senha"] ?: throw RuntimeException("Nova senha não informada")
        loginService.primeiroAcesso(usuarioId, novaSenha)
        return ResponseEntity.noContent().build()
    }

    @PostMapping
    fun login(@RequestBody dto: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val usuario = loginService.login(dto.email, dto.senha)
        val sessao = usuario.id?.let { loginService.getSessaoAtiva(it) }

        return ResponseEntity.ok(
            LoginResponseDTO(
                usuarioId = usuario.id!!,
                nome = usuario.nome,
                token = sessao?.token ?: ""
            )
        )
    }

    @PostMapping("/logout/{usuarioId}")
    fun logout(@PathVariable usuarioId: Int): ResponseEntity<Void> {
        loginService.logout(usuarioId)
        return ResponseEntity.noContent().build()
    }
}
