package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.CargoDTO
import com.fivegears.fivegears_backend.entity.Cargo
import com.fivegears.fivegears_backend.entity.enum.Senioridade

object CargoMapper {

    fun toDTO(entity: Cargo): CargoDTO = CargoDTO(
        idCargo = entity.idCargo,
        nome = entity.nome,
        descricao = entity.descricao,
        senioridade = entity.senioridade.name
    )

    fun toEntity(dto: CargoDTO): Cargo = Cargo(
        idCargo = dto.idCargo ?: 0,
        nome = dto.nome,
        descricao = dto.descricao,
        senioridade = dto.senioridade?.let { Senioridade.valueOf(it) } ?: Senioridade.JUNIOR
    )
}
