package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.CargoDTO
import com.fivegears.fivegears_backend.entity.Cargo
import com.fivegears.fivegears_backend.entity.EscoCargo
import com.fivegears.fivegears_backend.entity.enum.OrigemCargo

object CargoMapper {

    fun toDTO(entity: Cargo): CargoDTO = CargoDTO(
        idCargo = entity.idCargo,
        nome = entity.nome,
        descricao = entity.descricao,
        senioridade = entity.senioridade,
        origem = entity.origem.name, // converte enum -> String
        idEscoCargo = entity.escoCargo?.idEscoCargo
    )

    fun toEntity(dto: CargoDTO): Cargo = Cargo(
        idCargo = dto.idCargo,
        nome = dto.nome,
        descricao = dto.descricao,
        senioridade = dto.senioridade!!,
        origem = dto.origem?.let { OrigemCargo.valueOf(it) } ?: OrigemCargo.INTERNO, // String -> enum
        escoCargo = dto.idEscoCargo?.let { EscoCargo(it) } // cria apenas com o ID
    )
}
