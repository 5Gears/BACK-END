package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.EmpresaDTO
import com.fivegears.fivegears_backend.dto.EnderecoDTO
import com.fivegears.fivegears_backend.entity.Empresa
import com.fivegears.fivegears_backend.entity.Endereco

object EmpresaMapper {

    fun toDTO(entity: Empresa): EmpresaDTO =
        EmpresaDTO(
            id = entity.id,
            nome = entity.nome,
            fundador = entity.fundador,
            cnpj = entity.cnpj,
            enderecos = entity.enderecos.map { toEnderecoDTO(it) }
        )

    fun toEntity(dto: EmpresaDTO): Empresa =
        Empresa(
            id = dto.id,
            nome = dto.nome,
            fundador = dto.fundador,
            cnpj = dto.cnpj,
            enderecos = dto.enderecos.map { toEnderecoEntity(it) }.toMutableList()
        ).apply {
            this.enderecos.forEach { it.empresa = this }
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