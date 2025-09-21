package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.ClienteDTO
import com.fivegears.fivegears_backend.dto.EnderecoDTO
import com.fivegears.fivegears_backend.entity.Cliente
import com.fivegears.fivegears_backend.entity.Endereco

object ClienteMapper {

    fun toDTO(entity: Cliente): ClienteDTO =
        ClienteDTO(
            id = entity.id,
            nome = entity.nome,
            cnpj = entity.cnpj,
            emailResponsavel = entity.emailResponsavel,
            enderecos = entity.enderecos.map { toEnderecoDTO(it) }
        )

    fun toEntity(dto: ClienteDTO): Cliente =
        Cliente(
            id = dto.id,
            nome = dto.nome,
            cnpj = dto.cnpj,
            emailResponsavel = dto.emailResponsavel,
            enderecos = dto.enderecos.map { toEnderecoEntity(it) }.toMutableList()
        ).apply {
            this.enderecos.forEach { it.cliente = this }
        }

    private fun toEnderecoDTO(entity: Endereco): EnderecoDTO =
        EnderecoDTO(
            id = entity.id,
            rua = entity.rua,
            numero = entity.numero,
            bairro = entity.bairro,
            cidade = entity.cidade,
            estado = entity.estado,
            cep = entity.cep
        )

    private fun toEnderecoEntity(dto: EnderecoDTO): Endereco =
        Endereco(
            id = dto.id,
            rua = dto.rua,
            numero = dto.numero,
            bairro = dto.bairro,
            cidade = dto.cidade,
            estado = dto.estado,
            cep = dto.cep
        )
}