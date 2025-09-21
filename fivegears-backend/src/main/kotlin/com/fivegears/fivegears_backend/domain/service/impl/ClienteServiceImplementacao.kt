package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.ClienteRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.ClienteService
import com.fivegears.fivegears_backend.dto.ClienteDTO
import com.fivegears.fivegears_backend.mapper.ClienteMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ClienteServiceImplementacao(
    private val repository: ClienteRepository
) : ClienteService {

    override fun listarTodos(): ResponseEntity<List<ClienteDTO>> =
        ResponseEntity.ok(repository.findAll().map { ClienteMapper.toDTO(it) })

    override fun buscarPorId(id: Int): ResponseEntity<ClienteDTO> =
        repository.findById(id)
            .map { ResponseEntity.ok(ClienteMapper.toDTO(it)) }
            .orElse(ResponseEntity.notFound().build())

    override fun criar(dto: ClienteDTO): ResponseEntity<ClienteDTO> {
        val entity = ClienteMapper.toEntity(dto)
        val saved = repository.save(entity)
        return ResponseEntity.ok(ClienteMapper.toDTO(saved))
    }

    override fun atualizar(id: Int, dto: ClienteDTO): ResponseEntity<ClienteDTO> {
        val existente = repository.findById(id)
        return if (existente.isPresent) {
            val atualizado = existente.get().apply {
                nome = dto.nome
                cnpj = dto.cnpj
                emailResponsavel = dto.emailResponsavel

                enderecos.clear()
                dto.enderecos.forEach {
                    val endereco = ClienteMapper.toEntity(dto).enderecos
                    endereco.forEach { e -> e.cliente = this }
                    enderecos.addAll(endereco)
                }
            }
            ResponseEntity.ok(ClienteMapper.toDTO(repository.save(atualizado)))
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