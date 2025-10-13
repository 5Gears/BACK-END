package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.UsuarioProjeto
import com.fivegears.fivegears_backend.entity.UsuarioProjetoId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioProjetoRepository : JpaRepository<UsuarioProjeto, UsuarioProjetoId> {

    fun findByProjetoId(idProjeto: Int): List<UsuarioProjeto>

    fun findByUsuarioId(idUsuario: Int): List<UsuarioProjeto>

    fun findByStatus(status: String): List<UsuarioProjeto>
}
