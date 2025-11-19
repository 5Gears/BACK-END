package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.*
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.ProjetoService
import com.fivegears.fivegears_backend.dto.ProjetoRequestDTO
import com.fivegears.fivegears_backend.dto.ProjetoResponseDTO
import com.fivegears.fivegears_backend.entity.UsuarioProjeto
import com.fivegears.fivegears_backend.entity.enum.StatusAlocacao
import com.fivegears.fivegears_backend.entity.enum.StatusAnalise
import com.fivegears.fivegears_backend.entity.enum.StatusProjeto
import com.fivegears.fivegears_backend.mapper.ProjetoMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
@Transactional
class ProjetoServiceImplementacao(
    private val projetoRepository: ProjetoRepository,
    private val usuarioProjetoRepository: UsuarioProjetoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val cargoRepository: CargoRepository,
    private val clienteRepository: ClienteRepository,
    private val projetoAnaliseRepository: ProjetoAnaliseRepository
) : ProjetoService {

    override fun buscarPorNome(nome: String): ProjetoResponseDTO {
        val projeto = projetoRepository.findByNomeIgnoreCase(nome)
            ?: throw RuntimeException("Projeto com nome '$nome' não encontrado")
        return ProjetoMapper.toDTO(projeto)
    }

    override fun listarTodos(): List<ProjetoResponseDTO> =
        projetoRepository.findAll().map { ProjetoMapper.toDTO(it) }

    override fun buscarPorId(id: Int): ProjetoResponseDTO =
        projetoRepository.findById(id)
            .map { ProjetoMapper.toDTO(it) }
            .orElseThrow { RuntimeException("Projeto com ID $id não encontrado") }

    override fun criar(request: ProjetoRequestDTO): ProjetoResponseDTO {

        if (request.nome.isNullOrBlank()) {
            throw RuntimeException("O nome do projeto é obrigatório.")
        }

        if (request.responsavelId == null) {
            throw RuntimeException("O responsável pelo projeto é obrigatório na criação.")
        }

        // VALIDAR DATAS ================================================
        val hoje = LocalDate.now()

        if (request.dataInicio != null && request.dataInicio.isBefore(hoje)) {
            throw RuntimeException("A data de início não pode estar no passado.")
        }

        if (request.dataInicio != null && request.dataFim != null) {
            if (request.dataFim.isBefore(request.dataInicio)) {
                throw RuntimeException("A data de término não pode ser anterior à data de início.")
            }
        }
        // ===============================================================

        // RASCUNHO DE ANÁLISE (PDF)
        if (!request.draftId.isNullOrBlank()) {
            try {
                val draftUUID = UUID.fromString(request.draftId)
                val analise = projetoAnaliseRepository.findByIdAndStatus(draftUUID, StatusAnalise.ATIVO)
                    .orElseThrow { RuntimeException("Rascunho de análise inválido ou já utilizado.") }

                request.descricao = request.descricao ?: analise.descricaoExtraida
                request.competenciasRequeridas = request.competenciasRequeridas ?: analise.competenciasRequeridas

                analise.status = StatusAnalise.USADO
                projetoAnaliseRepository.save(analise)

            } catch (e: IllegalArgumentException) {
                throw RuntimeException("O ID de rascunho informado é inválido.")
            }
        }

        val cliente = request.clienteId?.let {
            clienteRepository.findById(it)
                .orElseThrow { RuntimeException("Cliente não encontrado") }
        }

        val responsavel = usuarioRepository.findById(request.responsavelId)
            .orElseThrow { RuntimeException("Responsável não encontrado") }

        val projeto = ProjetoMapper.fromRequest(request, cliente, responsavel)
        val salvo = projetoRepository.save(projeto)

        return ProjetoMapper.toDTO(salvo)
    }

    override fun atualizar(id: Int, request: ProjetoRequestDTO): ProjetoResponseDTO {
        val projetoExistente = projetoRepository.findById(id)
            .orElseThrow { RuntimeException("Projeto com ID $id não encontrado") }

        val cliente = request.clienteId?.let {
            clienteRepository.findById(it)
                .orElseThrow { RuntimeException("Cliente não encontrado") }
        }

        val responsavel = request.responsavelId?.let {
            usuarioRepository.findById(it)
                .orElseThrow { RuntimeException("Responsável não encontrado") }
        }

        // VALIDAR DATAS TAMBÉM EM ATUALIZAÇÃO ================================
        val hoje = LocalDate.now()

        if (request.dataInicio != null && request.dataInicio.isBefore(hoje)) {
            throw RuntimeException("A data de início não pode estar no passado.")
        }

        if (request.dataInicio != null && request.dataFim != null) {
            if (request.dataFim.isBefore(request.dataInicio)) {
                throw RuntimeException("A data de término não pode ser anterior à data de início.")
            }
        }
        // =====================================================================

        projetoExistente.apply {
            request.nome?.let { nome = it }
            request.descricao?.let { descricao = it }
            request.tempoEstimadoHoras?.let { tempoEstimadoHoras = it }
            request.orcamento?.let { orcamento = it }
            request.dataInicio?.let { dataInicio = it }
            request.dataFim?.let { dataFim = it }
            cliente?.let { this.cliente = it }
            responsavel?.let { this.responsavel = it }
            request.competenciasRequeridas?.let { competenciasRequeridas = it }
        }

        val salvo = projetoRepository.save(projetoExistente)
        return ProjetoMapper.toDTO(salvo)
    }

    // RESTANTE DO CÓDIGO PERMANECE IGUAL =====================================
    override fun deletar(id: Int) {
        val projeto = projetoRepository.findById(id)
            .orElseThrow { RuntimeException("Projeto com ID $id não encontrado") }
        projetoRepository.delete(projeto)
    }

    override fun listarUsuariosDoProjeto(idProjeto: Int): List<UsuarioProjeto> =
        usuarioProjetoRepository.findByProjetoId(idProjeto)

    override fun adicionarUsuarioAoProjeto(
        idProjeto: Int,
        idUsuario: Int,
        horasAlocadas: Int,
        horasPorDia: Int,
        dataInicio: LocalDate?,
        dataFim: LocalDate?
    ): UsuarioProjeto {
        val projeto = projetoRepository.findById(idProjeto)
            .orElseThrow { RuntimeException("Projeto não encontrado") }
        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { RuntimeException("Usuário não encontrado") }

        val usuarioProjeto = UsuarioProjeto(
            projeto = projeto,
            usuario = usuario,
            horasAlocadas = horasAlocadas,
            horasPorDia = horasPorDia,
            dataAlocacao = dataInicio ?: LocalDate.now(),
            dataSaida = dataFim,
            status = StatusAlocacao.ALOCADO
        )
        return usuarioProjetoRepository.save(usuarioProjeto)
    }

    override fun removerUsuarioDoProjeto(idProjeto: Int, idUsuario: Int) {
        val usuarioProjeto = usuarioProjetoRepository.findByProjetoId(idProjeto)
            .firstOrNull { it.usuario?.id == idUsuario }
            ?: throw RuntimeException("Usuário não está vinculado a este projeto")

        usuarioProjetoRepository.delete(usuarioProjeto)
    }

    override fun aceitarProjeto(id: Int): ProjetoResponseDTO {
        val projeto = projetoRepository.findById(id)
            .orElseThrow { RuntimeException("Projeto não encontrado") }

        if (projeto.status != StatusProjeto.EM_PLANEJAMENTO) {
            throw RuntimeException("Projeto não pode ser aceito pois não está em planejamento.")
        }

        projeto.status = StatusProjeto.EM_DESENVOLVIMENTO
        val salvo = projetoRepository.save(projeto)
        return ProjetoMapper.toDTO(salvo)
    }

    override fun negarProjeto(id: Int): ProjetoResponseDTO {
        val projeto = projetoRepository.findById(id)
            .orElseThrow { RuntimeException("Projeto não encontrado") }

        if (projeto.status != StatusProjeto.EM_PLANEJAMENTO) {
            throw RuntimeException("Projeto não pode ser negado pois não está em planejamento.")
        }

        projeto.status = StatusProjeto.NEGADO
        val salvo = projetoRepository.save(projeto)
        return ProjetoMapper.toDTO(salvo)
    }

    override fun finalizarProjeto(id: Int, concluido: Boolean): ProjetoResponseDTO {
        val projeto = projetoRepository.findById(id)
            .orElseThrow { RuntimeException("Projeto não encontrado") }

        if (projeto.status != StatusProjeto.EM_DESENVOLVIMENTO) {
            throw RuntimeException("Somente projetos em desenvolvimento podem ser finalizados.")
        }

        projeto.status = if (concluido) StatusProjeto.CONCLUIDO else StatusProjeto.CANCELADO
        projeto.dataFim = LocalDate.now()

        val salvo = projetoRepository.save(projeto)
        return ProjetoMapper.toDTO(salvo)
    }

    override fun listarPorResponsavel(idResponsavel: Int): List<ProjetoResponseDTO> {
        val projetos = projetoRepository.findByResponsavelId(idResponsavel)
        if (projetos.isEmpty()) {
            throw RuntimeException("Nenhum projeto encontrado para o responsável com ID $idResponsavel")
        }
        return projetos.map { ProjetoMapper.toDTO(it) }
    }
}
