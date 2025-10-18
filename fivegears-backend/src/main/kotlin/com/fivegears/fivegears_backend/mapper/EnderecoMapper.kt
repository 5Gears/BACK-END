package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.EnderecoDTO
import com.fivegears.fivegears_backend.entity.Endereco

object EnderecoMapper {
    fun toDTO(entity: Endereco): EnderecoDTO = EnderecoDTO(
        id = entity.id,
        rua = entity.rua,
        numero = entity.numero,
        bairro = entity.bairro,
        cidade = entity.cidade,
        estado = entity.estado,
        cep = entity.cep
    )

    fun toEntity(dto: EnderecoDTO): Endereco = Endereco(
        id = dto.id,
        rua = dto.rua,
        numero = dto.numero,
        bairro = dto.bairro,
        cidade = dto.cidade,
        estado = dto.estado,
        cep = dto.cep
    )
}
