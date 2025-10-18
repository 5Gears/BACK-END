package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.EmpresaDTO
import com.fivegears.fivegears_backend.entity.Empresa

object EmpresaMapper {
    fun toDTO(entity: Empresa): EmpresaDTO = EmpresaDTO(
        id = entity.id,
        nome = entity.nome,
        fundador = entity.fundador,
        cnpj = entity.cnpj,
        enderecos = entity.enderecos.map { EnderecoMapper.toDTO(it) }
    )

    fun toEntity(dto: EmpresaDTO): Empresa = Empresa(
        id = dto.id,
        nome = dto.nome,
        fundador = dto.fundador,
        cnpj = dto.cnpj,
        enderecos = dto.enderecos.map { EnderecoMapper.toEntity(it) }.toMutableList()
    ).apply { this.enderecos.forEach { it.empresa = this } }
}
