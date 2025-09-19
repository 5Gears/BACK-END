package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.UsuarioService
import com.fivegears.fivegears_backend.dto.UsuarioDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuarios")

class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<UsuarioDTO>> {
        return usuarioService.listarTodos()
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<UsuarioDTO> {
        return usuarioService.buscarPorId(id)
    }

    @PostMapping
    fun criar(@RequestBody dto: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        return usuarioService.criar(dto)
    }

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody dto: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        return usuarioService.atualizar(id, dto)
    }

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        return usuarioService.deletar(id)
    }
}
