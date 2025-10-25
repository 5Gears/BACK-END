package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.CargoDTO
import com.fivegears.fivegears_backend.entity.Cargo
import com.fivegears.fivegears_backend.entity.enum.FonteCargo

object CargoMapper {

        fun toDTO(entity: Cargo): CargoDTO = CargoDTO(
            idCargo = entity.idCargo,
            nome = entity.nome,
            descricao = entity.descricao,
            fonte = entity.fonte.name
        )

        fun toEntity(dto: CargoDTO): Cargo = Cargo(
            idCargo = dto.idCargo,
            nome = dto.nome,
            descricao = dto.descricao,
            fonte = dto.fonte?.let { FonteCargo.valueOf(it) } ?: FonteCargo.INTERNO
        )
    }

