package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.EmpresaRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.EmpresaService
import com.fivegears.fivegears_backend.dto.EmpresaDTO
import com.fivegears.fivegears_backend.mapper.EmpresaMapper

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImplementacao(
    private val repository: EmpresaRepository
) : EmpresaService {

    override fun listarTodas(): ResponseEntity<List<EmpresaDTO>> =
        ResponseEntity.ok(repository.findAll().map { EmpresaMapper.toDTO(it) })

    override fun buscarPorId(id: Int): ResponseEntity<EmpresaDTO> =
        repository.findById(id)
            .map { ResponseEntity.ok(EmpresaMapper.toDTO(it)) }
            .orElse(ResponseEntity.notFound().build())

    override fun criar(dto: EmpresaDTO): ResponseEntity<EmpresaDTO> {
        val entity = EmpresaMapper.toEntity(dto)
        val saved = repository.save(entity)
        return ResponseEntity.ok(EmpresaMapper.toDTO(saved))
    }

    override fun atualizar(id: Int, dto: EmpresaDTO): ResponseEntity<EmpresaDTO> {
        val existente = repository.findById(id)
        return if (existente.isPresent) {
            val atualizado = existente.get().apply {
                nome = dto.nome
                fundador = dto.fundador
                cnpj = dto.cnpj
                enderecos.clear()
                enderecos.addAll(dto.enderecos.map { EmpresaMapper.toEntity(dto).enderecos }.flatten())
                enderecos.forEach { it.empresa = this }
            }
            ResponseEntity.ok(EmpresaMapper.toDTO(repository.save(atualizado)))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    override fun deletar(id: Int): ResponseEntity<Void> {
        return if (repository.existsById(id)) {
            repository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
