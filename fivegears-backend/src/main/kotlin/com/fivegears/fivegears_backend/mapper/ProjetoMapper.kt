package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.ProjetoRequestDTO
import com.fivegears.fivegears_backend.dto.ProjetoResponseDTO
import com.fivegears.fivegears_backend.entity.Cliente
import com.fivegears.fivegears_backend.entity.Projeto
import com.fivegears.fivegears_backend.entity.Usuario
import com.fivegears.fivegears_backend.entity.enum.StatusProjeto
import java.time.LocalDate

object ProjetoMapper {

    fun toDTO(entity: Projeto): ProjetoResponseDTO {
        val hoje = LocalDate.now()

        val atrasado = entity.dataFim?.let { dataFim ->
            dataFim.isBefore(hoje) &&
                    entity.status == StatusProjeto.EM_DESENVOLVIMENTO
        } ?: false

        return ProjetoResponseDTO(
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
            responsavelId = entity.responsavel?.id,
            responsavelNome = entity.responsavel?.nome,
            competenciasRequeridas = entity.competenciasRequeridas,
            atrasado = atrasado
        )
    }

    fun fromRequest(dto: ProjetoRequestDTO, cliente: Cliente?, responsavel: Usuario?): Projeto =
        Projeto(
            nome = dto.nome ?: throw RuntimeException("O nome do projeto é obrigatório."),
            descricao = dto.descricao,
            tempoEstimadoHoras = dto.tempoEstimadoHoras,
            orcamento = dto.orcamento,
            status = StatusProjeto.EM_PLANEJAMENTO,
            dataInicio = dto.dataInicio,
            dataFim = dto.dataFim,
            cliente = cliente,
            responsavel = responsavel
                ?: throw RuntimeException("O responsável pelo projeto é obrigatório."),
            competenciasRequeridas = dto.competenciasRequeridas?.takeIf { it.isNotBlank() }
        )
}
