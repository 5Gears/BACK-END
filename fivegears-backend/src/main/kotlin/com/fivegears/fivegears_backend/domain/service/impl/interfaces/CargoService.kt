package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.dto.CargoDTO
import org.springframework.http.ResponseEntity

interface CargoService {
    fun listarTodos(): ResponseEntity<List<CargoDTO>>
    fun buscarPorId(id: Int): ResponseEntity<CargoDTO>
    fun buscarPorNome(nome: String): ResponseEntity<List<CargoDTO>>
    fun listarSugestoes(nome: String): ResponseEntity<List<CargoDTO>>
    fun criar(dto: CargoDTO): ResponseEntity<CargoDTO>
    fun atualizar(id: Int, dto: CargoDTO): ResponseEntity<CargoDTO>
    fun deletar(id: Int): ResponseEntity<Void>
}
