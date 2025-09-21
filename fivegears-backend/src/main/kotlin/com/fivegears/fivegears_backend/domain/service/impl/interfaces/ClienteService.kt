package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.dto.ClienteDTO
import org.springframework.http.ResponseEntity

interface ClienteService {
    fun listarTodos(): ResponseEntity<List<ClienteDTO>>
    fun buscarPorId(id: Int): ResponseEntity<ClienteDTO>
    fun criar(dto: ClienteDTO): ResponseEntity<ClienteDTO>
    fun atualizar(id: Int, dto: ClienteDTO): ResponseEntity<ClienteDTO>
    fun deletar(id: Int): ResponseEntity<Void>
}