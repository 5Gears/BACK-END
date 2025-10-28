package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.ProjetoResponseDTO
import com.fivegears.fivegears_backend.entity.Projeto

object ProjetoMapper {

    fun toDTO(entity: Projeto): ProjetoResponseDTO =
        ProjetoResponseDTO(
            id = entity.id ?: 0,
            nome = entity.nome,
            descricao = entity.descricao,
            tempoEstimadoHoras = entity.tempoEstimadoHoras,
            orcamento = entity.orcamento,
            status = entity.status.name,
            dataInicio = entity.dataInicio,
            dataFim = entity.dataFim,
            clienteId = entity.cliente?.id,
            clienteNome = entity.cliente?.nome,
            responsavelId = entity.responsavel.id,
            responsavelNome = entity.responsavel.nome,
            competenciasRequeridas = entity.competenciasRequeridas
        )
}
