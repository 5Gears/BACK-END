package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.interfaces.ClienteService
import com.fivegears.fivegears_backend.dto.ClienteDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/clientes")
class ClienteController(
    private val clienteService: ClienteService
) {
    @GetMapping
    fun listarTodos(): ResponseEntity<List<ClienteDTO>> =
        clienteService.listarTodos()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<ClienteDTO> =
        clienteService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody dto: ClienteDTO): ResponseEntity<ClienteDTO> =
        clienteService.criar(dto)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody dto: ClienteDTO): ResponseEntity<ClienteDTO> =
        clienteService.atualizar(id, dto)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> =
        clienteService.deletar(id)
}