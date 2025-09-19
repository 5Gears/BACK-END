package com.fivegears.fivegears_backend.domain.service


import com.fivegears.fivegears_backend.dto.UsuarioDTO
import org.springframework.http.ResponseEntity

interface UsuarioService {
    fun listarTodos(): ResponseEntity<List<UsuarioDTO>>
    fun buscarPorId(id: Int): ResponseEntity<UsuarioDTO>
    fun criar(dto: UsuarioDTO): ResponseEntity<UsuarioDTO>
    fun atualizar(id: Int, dto: UsuarioDTO): ResponseEntity<UsuarioDTO>
    fun deletar(id: Int): ResponseEntity<Void>
}