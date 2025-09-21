package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.interfaces.EmpresaService
import com.fivegears.fivegears_backend.dto.EmpresaDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(
    private val empresaService: EmpresaService
) {
    @GetMapping
    fun listarTodas(): ResponseEntity<List<EmpresaDTO>> =
        empresaService.listarTodas()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<EmpresaDTO> =
        empresaService.buscarPorId(id)

    @PostMapping
    fun criar(@RequestBody dto: EmpresaDTO): ResponseEntity<EmpresaDTO> =
        empresaService.criar(dto)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody dto: EmpresaDTO): ResponseEntity<EmpresaDTO> =
        empresaService.atualizar(id, dto)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> =
        empresaService.deletar(id)
}