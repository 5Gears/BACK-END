package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.interfaces.ProjetoService
import com.fivegears.fivegears_backend.dto.ProjetoRequestDTO
import com.fivegears.fivegears_backend.dto.ProjetoResponseDTO
import com.fivegears.fivegears_backend.entity.UsuarioProjeto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/projetos")
@Tag(name = "Projetos", description = "Gerenciamento de projetos e alocação de usuários")
class ProjetoController(
    private val projetoService: ProjetoService
) {

    @GetMapping
    @Operation(summary = "Listar todos os projetos")
    fun listarTodos(): ResponseEntity<List<ProjetoResponseDTO>> =
        ResponseEntity.ok(projetoService.listarTodos())

    @GetMapping("/gerente/{idResponsavel}")
    @Operation(summary = "Listar projetos de um gerente (responsável)")
    fun listarPorResponsavel(@PathVariable idResponsavel: Int): ResponseEntity<List<ProjetoResponseDTO>> =
        ResponseEntity.ok(projetoService.listarPorResponsavel(idResponsavel))


    @GetMapping("/{id}")
    @Operation(summary = "Buscar um projeto por ID")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<ProjetoResponseDTO> =
        ResponseEntity.ok(projetoService.buscarPorId(id))

    @PostMapping
    @Operation(summary = "Criar um novo projeto")
    fun criar(@RequestBody request: ProjetoRequestDTO): ResponseEntity<ProjetoResponseDTO> {
        val novoProjeto = projetoService.criar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProjeto)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um projeto existente")
    fun atualizar(@PathVariable id: Int, @RequestBody request: ProjetoRequestDTO): ResponseEntity<ProjetoResponseDTO> {
        val atualizado = projetoService.atualizar(id, request)
        return ResponseEntity.ok(atualizado)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um projeto")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> {
        projetoService.deletar(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{idProjeto}/usuarios")
    @Operation(summary = "Listar todos os usuários de um projeto")
    fun listarUsuariosDoProjeto(@PathVariable idProjeto: Int): ResponseEntity<List<UsuarioProjeto>> =
        ResponseEntity.ok(projetoService.listarUsuariosDoProjeto(idProjeto))

    @PostMapping("/{idProjeto}/usuarios/{idUsuario}")
    @Operation(summary = "Adicionar usuário a um projeto")
    fun adicionarUsuarioAoProjeto(
        @PathVariable idProjeto: Int,
        @PathVariable idUsuario: Int
    ): ResponseEntity<UsuarioProjeto> {
        val usuarioProjeto = projetoService.adicionarUsuarioAoProjeto(idProjeto, idUsuario)
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioProjeto)
    }

    @DeleteMapping("/{idProjeto}/usuarios/{idUsuario}")
    @Operation(summary = "Remover usuário de um projeto")
    fun removerUsuarioDoProjeto(
        @PathVariable idProjeto: Int,
        @PathVariable idUsuario: Int
    ): ResponseEntity<Void> {
        projetoService.removerUsuarioDoProjeto(idProjeto, idUsuario)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/aceitar")
    @Operation(summary = "Aceitar um projeto (muda status para EM_DESENVOLVIMENTO)")
    fun aceitarProjeto(@PathVariable id: Int): ResponseEntity<ProjetoResponseDTO> =
        ResponseEntity.ok(projetoService.aceitarProjeto(id))

    @PutMapping("/{id}/negar")
    @Operation(summary = "Negar um projeto (muda status para NEGADO)")
    fun negarProjeto(@PathVariable id: Int): ResponseEntity<ProjetoResponseDTO> =
        ResponseEntity.ok(projetoService.negarProjeto(id))

    @PutMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar um projeto (muda status para CONCLUIDO ou CANCELADO)")
    fun finalizarProjeto(
        @PathVariable id: Int,
        @RequestParam concluido: Boolean
    ): ResponseEntity<ProjetoResponseDTO> =
        ResponseEntity.ok(projetoService.finalizarProjeto(id, concluido))
}
