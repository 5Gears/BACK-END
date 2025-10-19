package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.CompetenciaDTO
import com.fivegears.fivegears_backend.entity.Competencia

object CompetenciaMapper {

    fun toDTO(entity: Competencia): CompetenciaDTO = CompetenciaDTO(
        idCompetencia = entity.idCompetencia,
        nome = entity.nome,
        descricao = entity.descricao,
        tipo = entity.tipo,
        categoria = entity.categoria
    )

    fun toEntity(dto: CompetenciaDTO): Competencia = Competencia(
        idCompetencia = dto.idCompetencia,
        nome = dto.nome,
        descricao = dto.descricao,
        tipo = dto.tipo,
        categoria = dto.categoria
    )
}
