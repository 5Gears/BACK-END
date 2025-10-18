package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.ClienteRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.ClienteService
import com.fivegears.fivegears_backend.dto.ClienteDTO
import com.fivegears.fivegears_backend.mapper.ClienteMapper
import com.fivegears.fivegears_backend.mapper.EnderecoMapper
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
            val cliente = existente.get().apply {
                nome = dto.nome
                cnpj = dto.cnpj
                emailResponsavel = dto.emailResponsavel

                // Atualiza endereços corretamente
                enderecos.clear()
                val enderecosAtualizados = dto.enderecos.map { enderecoDTO ->
                    EnderecoMapper.toEntity(enderecoDTO).apply { this.empresa = empresa }
                }
                enderecos.addAll(enderecosAtualizados)
            }
            ResponseEntity.ok(ClienteMapper.toDTO(repository.save(cliente)))
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
