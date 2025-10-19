package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.Usuario
import com.fivegears.fivegears_backend.entity.UsuarioProjeto
import com.fivegears.fivegears_backend.entity.UsuarioProjetoId
import com.fivegears.fivegears_backend.entity.enum.StatusAlocacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UsuarioProjetoRepository : JpaRepository<UsuarioProjeto, UsuarioProjetoId> {

    // Busca todas as alocações de um usuário
    fun findByUsuario(usuario: Usuario): List<UsuarioProjeto>

    // Busca todas as alocações por ID do usuário
    fun findByUsuarioId(idUsuario: Int): List<UsuarioProjeto>

    // Busca todas as alocações por status (usando enum)
    fun findByStatus(status: StatusAlocacao): List<UsuarioProjeto>

    // Busca todas as alocações por ID do projeto
    fun findByProjetoId(idProjeto: Int): List<UsuarioProjeto>

    // Query customizada para buscar com usuário e projeto carregados (evita LazyInitializationException)
    @Query(
        "SELECT up FROM UsuarioProjeto up " +
                "JOIN FETCH up.usuario u " +
                "JOIN FETCH up.projeto p " +
                "WHERE p.id = :idProjeto"
    )
    fun findByProjetoIdWithUsuarioFetch(@Param("idProjeto") idProjeto: Int): List<UsuarioProjeto>

    // Query customizada para buscar todas as alocações de um usuário com projeto carregado
    @Query(
        "SELECT up FROM UsuarioProjeto up " +
                "JOIN FETCH up.projeto p " +
                "WHERE up.usuario.id = :idUsuario"
    )
    fun findByUsuarioIdWithProjetoFetch(@Param("idUsuario") idUsuario: Int): List<UsuarioProjeto>

    // Query customizada para buscar por status e carregar usuário/projeto
    @Query(
        "SELECT up FROM UsuarioProjeto up " +
                "JOIN FETCH up.usuario u " +
                "JOIN FETCH up.projeto p " +
                "WHERE up.status = :status"
    )
    fun findByStatusWithUsuarioProjetoFetch(@Param("status") status: StatusAlocacao): List<UsuarioProjeto>
}
