package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.interfaces.CargoService
import com.fivegears.fivegears_backend.dto.CargoDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cargos")
class CargoController(
    private val cargoService: CargoService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<CargoDTO>> = cargoService.listarTodos()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<CargoDTO> = cargoService.buscarPorId(id)

    @GetMapping("/buscar")
    fun buscarPorNome(@RequestParam nome: String): ResponseEntity<List<CargoDTO>> = cargoService.buscarPorNome(nome)

    @PostMapping
    fun criar(@RequestBody dto: CargoDTO): ResponseEntity<CargoDTO> = cargoService.criar(dto)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Int, @RequestBody dto: CargoDTO): ResponseEntity<CargoDTO> =
        cargoService.atualizar(id, dto)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Int): ResponseEntity<Void> = cargoService.deletar(id)
}
