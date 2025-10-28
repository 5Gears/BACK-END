package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.*
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.ProjetoService
import com.fivegears.fivegears_backend.dto.ProjetoRequestDTO
import com.fivegears.fivegears_backend.dto.ProjetoResponseDTO
import com.fivegears.fivegears_backend.entity.UsuarioProjeto
import com.fivegears.fivegears_backend.entity.enum.StatusProjeto
import com.fivegears.fivegears_backend.mapper.ProjetoMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjetoServiceImplementacao(
    private val projetoRepository: ProjetoRepository,
    private val usuarioProjetoRepository: UsuarioProjetoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val cargoRepository: CargoRepository,
    private val clienteRepository: ClienteRepository
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
        val cliente = request.clienteId?.let {
            clienteRepository.findById(it).orElseThrow { RuntimeException("Cliente não encontrado") }
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

        var cliente = request.clienteId?.let {
            clienteRepository.findById(it).orElseThrow { RuntimeException("Cliente não encontrado") }
        }

        val responsavel = usuarioRepository.findById(request.responsavelId)
            .orElseThrow { RuntimeException("Responsável não encontrado") }

        projetoExistente.apply {
            nome = request.nome
            descricao = request.descricao
            tempoEstimadoHoras = request.tempoEstimadoHoras
            orcamento = request.orcamento
            dataInicio = request.dataInicio
            dataFim = request.dataFim
            cliente = cliente
            this.responsavel = responsavel
            competenciasRequeridas = request.competenciasRequeridas
        }

        val salvo = projetoRepository.save(projetoExistente)
        return ProjetoMapper.toDTO(salvo)
    }

    override fun deletar(id: Int) {
        val projeto = projetoRepository.findById(id)
            .orElseThrow { RuntimeException("Projeto com ID $id não encontrado") }
        projetoRepository.delete(projeto)
    }

    override fun listarUsuariosDoProjeto(idProjeto: Int): List<UsuarioProjeto> =
        usuarioProjetoRepository.findByProjetoId(idProjeto)

    override fun adicionarUsuarioAoProjeto(idProjeto: Int, idUsuario: Int): UsuarioProjeto {
        val projeto = projetoRepository.findById(idProjeto)
            .orElseThrow { RuntimeException("Projeto não encontrado") }

        if (projeto.status != StatusProjeto.EM_DESENVOLVIMENTO) {
            throw RuntimeException("Usuários só podem ser alocados em projetos em desenvolvimento.")
        }

        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { RuntimeException("Usuário não encontrado") }

        val usuarioProjeto = UsuarioProjeto(
            projeto = projeto,
            usuario = usuario,
            status = com.fivegears.fivegears_backend.entity.enum.StatusAlocacao.ALOCADO,
            horasPorDia = 8,
            horasAlocadas = 0,
            dataAlocacao = java.time.LocalDate.now()
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
        projeto.dataFim = java.time.LocalDate.now()

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
