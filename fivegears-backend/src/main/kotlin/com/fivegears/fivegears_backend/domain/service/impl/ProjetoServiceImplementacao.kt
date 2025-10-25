package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.CargoRepository
import com.fivegears.fivegears_backend.domain.repository.ProjetoRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioProjetoRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.ProjetoService
import com.fivegears.fivegears_backend.entity.*
import com.fivegears.fivegears_backend.entity.enum.StatusProjeto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjetoServiceImplementacao(
    private val projetoRepository: ProjetoRepository,
    private val usuarioProjetoRepository: UsuarioProjetoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val cargoRepository: CargoRepository
) : ProjetoService {

    override fun buscarPorNome(nome: String): Projeto =
        projetoRepository.findByNomeIgnoreCase(nome)
            ?: throw RuntimeException("Projeto com nome '$nome' não encontrado")
    override fun listarTodos(): List<Projeto> =
        projetoRepository.findAll()

    override fun buscarPorId(id: Int): Projeto =
        projetoRepository.findById(id)
            .orElseThrow { RuntimeException("Projeto com ID $id não encontrado") }

    override fun criar(projeto: Projeto): Projeto {
        val existente = projetoRepository.findByNomeIgnoreCase(projeto.nome)
        if (existente != null) {
            throw RuntimeException("Já existe um projeto cadastrado com o nome '${projeto.nome}'.")
        }

        return projetoRepository.save(projeto)
    }

    override fun atualizar(id: Int, projetoAtualizado: Projeto): Projeto {
        val projetoExistente = buscarPorId(id)
        projetoExistente.apply {
            nome = projetoAtualizado.nome
            descricao = projetoAtualizado.descricao
            tempoEstimadoHoras = projetoAtualizado.tempoEstimadoHoras
            orcamento = projetoAtualizado.orcamento
            status = projetoAtualizado.status
            dataInicio = projetoAtualizado.dataInicio
            dataFim = projetoAtualizado.dataFim
            cliente = projetoAtualizado.cliente
            responsavel = projetoAtualizado.responsavel
        }
        return projetoRepository.save(projetoExistente)
    }

    override fun deletar(id: Int) {
        val projeto = buscarPorId(id)
        projetoRepository.delete(projeto)
    }

    override fun listarUsuariosDoProjeto(idProjeto: Int): List<UsuarioProjeto> =
        usuarioProjetoRepository.findByProjetoId(idProjeto)

    override fun adicionarUsuarioAoProjeto(idProjeto: Int, idUsuario: Int, idCargo: Int): UsuarioProjeto {
        val projeto = projetoRepository.findById(idProjeto)
            .orElseThrow { RuntimeException("Projeto não encontrado") }

        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { RuntimeException("Usuário não encontrado") }

        val cargo = cargoRepository.findById(idCargo)
            .orElseThrow { RuntimeException("Cargo não encontrado") }

        val usuarioProjeto = UsuarioProjeto(
            projeto = projeto,
            usuario = usuario,
            cargo = cargo
        )

        return usuarioProjetoRepository.save(usuarioProjeto)
    }

    override fun removerUsuarioDoProjeto(idProjeto: Int, idUsuario: Int) {
        val usuarioProjeto = usuarioProjetoRepository.findByProjetoId(idProjeto)
            .firstOrNull { it.usuario?.id == idUsuario }
            ?: throw RuntimeException("Usuário não está vinculado a este projeto")

        usuarioProjetoRepository.delete(usuarioProjeto)
    }

    override fun aceitarProjeto(id: Int): Projeto {
        val projeto = buscarPorId(id)
        if (projeto.status != StatusProjeto.EM_PLANEJAMENTO) {
            throw RuntimeException("Projeto não pode ser aceito pois não está em planejamento.")
        }
        projeto.status = StatusProjeto.EM_DESENVOLVIMENTO
        return projetoRepository.save(projeto)
    }

    override fun negarProjeto(id: Int): Projeto {
        val projeto = buscarPorId(id)
        if (projeto.status != StatusProjeto.EM_PLANEJAMENTO) {
            throw RuntimeException("Projeto não pode ser negado pois não está em planejamento.")
        }
        projeto.status = StatusProjeto.NEGADO
        return projetoRepository.save(projeto)
    }

}
