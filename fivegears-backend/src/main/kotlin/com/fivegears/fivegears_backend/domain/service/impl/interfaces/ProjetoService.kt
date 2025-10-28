package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.dto.ProjetoRequestDTO
import com.fivegears.fivegears_backend.dto.ProjetoResponseDTO
import com.fivegears.fivegears_backend.entity.UsuarioProjeto

interface ProjetoService {
    fun buscarPorNome(nome: String): ProjetoResponseDTO
    fun listarTodos(): List<ProjetoResponseDTO>
    fun buscarPorId(id: Int): ProjetoResponseDTO
    fun criar(request: ProjetoRequestDTO): ProjetoResponseDTO
    fun atualizar(id: Int, request: ProjetoRequestDTO): ProjetoResponseDTO
    fun deletar(id: Int)
    fun listarUsuariosDoProjeto(idProjeto: Int): List<UsuarioProjeto>
    fun adicionarUsuarioAoProjeto(idProjeto: Int, idUsuario: Int, idCargo: Int): UsuarioProjeto
    fun removerUsuarioDoProjeto(idProjeto: Int, idUsuario: Int)
    fun aceitarProjeto(id: Int): ProjetoResponseDTO
    fun negarProjeto(id: Int): ProjetoResponseDTO
    fun finalizarProjeto(id: Int, concluido: Boolean): ProjetoResponseDTO
}
