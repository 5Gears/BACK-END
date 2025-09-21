package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.dto.EmpresaDTO
import org.springframework.http.ResponseEntity

interface EmpresaService {
    fun listarTodas(): ResponseEntity<List<EmpresaDTO>>
    fun buscarPorId(id: Int): ResponseEntity<EmpresaDTO>
    fun criar(dto: EmpresaDTO): ResponseEntity<EmpresaDTO>
    fun atualizar(id: Int, dto: EmpresaDTO): ResponseEntity<EmpresaDTO>
    fun deletar(id: Int): ResponseEntity<Void>
}