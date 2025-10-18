package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.ClienteDTO
import com.fivegears.fivegears_backend.entity.Cliente

object ClienteMapper {
    fun toDTO(entity: Cliente): ClienteDTO = ClienteDTO(
        id = entity.id,
        nome = entity.nome,
        cnpj = entity.cnpj,
        emailResponsavel = entity.emailResponsavel,
        enderecos = entity.enderecos.map { EnderecoMapper.toDTO(it) }
    )

    fun toEntity(dto: ClienteDTO): Cliente = Cliente(
        id = dto.id,
        nome = dto.nome,
        cnpj = dto.cnpj,
        emailResponsavel = dto.emailResponsavel,
        enderecos = dto.enderecos.map { EnderecoMapper.toEntity(it) }.toMutableList()
    ).apply { this.enderecos.forEach { it.cliente = this } }
}
