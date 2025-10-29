package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.interfaces.UsuarioService
import com.fivegears.fivegears_backend.dto.UsuarioDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuarios")

class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<UsuarioDTO>> =
        usuarioService.listarTodos()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<UsuarioDTO> =
        usuarioService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody dto: UsuarioDTO): ResponseEntity<UsuarioDTO> =
        usuarioService.criar(dto)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody dto: UsuarioDTO): ResponseEntity<UsuarioDTO> =
        usuarioService.atualizar(id, dto)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> =
        usuarioService.deletar(id)

    @GetMapping("/buscar")
    fun buscarPorEmail(@RequestParam email: String): ResponseEntity<UsuarioDTO> =
        usuarioService.buscarPorEmail(email)
}
