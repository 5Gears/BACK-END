package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.ProjetoRequestDTO
import com.fivegears.fivegears_backend.dto.ProjetoResponseDTO
import com.fivegears.fivegears_backend.entity.Cliente
import com.fivegears.fivegears_backend.entity.Projeto
import com.fivegears.fivegears_backend.entity.Usuario
import com.fivegears.fivegears_backend.entity.enum.StatusProjeto

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

    fun fromRequest(dto: ProjetoRequestDTO, cliente: Cliente?, responsavel: Usuario): Projeto =
        Projeto(
            nome = dto.nome,
            descricao = dto.descricao,
            tempoEstimadoHoras = dto.tempoEstimadoHoras,
            orcamento = dto.orcamento,
            status = StatusProjeto.EM_PLANEJAMENTO,
            dataInicio = dto.dataInicio,
            dataFim = dto.dataFim,
            cliente = cliente,
            responsavel = responsavel,
            competenciasRequeridas = dto.competenciasRequeridas
        )
}
