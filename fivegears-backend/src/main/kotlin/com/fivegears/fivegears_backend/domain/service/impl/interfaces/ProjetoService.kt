package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.entity.Projeto
import com.fivegears.fivegears_backend.entity.UsuarioProjeto

interface ProjetoService {

    fun listarTodos(): List<Projeto>

    fun buscarPorId(id: Int): Projeto

    fun criar(projeto: Projeto): Projeto

    fun atualizar(id: Int, projetoAtualizado: Projeto): Projeto

    fun deletar(id: Int)

    fun listarUsuariosDoProjeto(idProjeto: Int): List<UsuarioProjeto>

    fun adicionarUsuarioAoProjeto(idProjeto: Int, idUsuario: Int, idCargo: Int): UsuarioProjeto

    fun removerUsuarioDoProjeto(idProjeto: Int, idUsuario: Int)
}
