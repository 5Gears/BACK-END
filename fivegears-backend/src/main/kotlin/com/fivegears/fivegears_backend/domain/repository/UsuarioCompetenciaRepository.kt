package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.Usuario
import com.fivegears.fivegears_backend.entity.UsuarioCompetencia
import com.fivegears.fivegears_backend.entity.UsuarioCompetenciaId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface UsuarioCompetenciaRepository : JpaRepository<UsuarioCompetencia, UsuarioCompetenciaId> {
    fun findByCompetenciaNome(nome: String): List<UsuarioCompetencia>
    fun findByUsuario(usuario: Usuario): List<UsuarioCompetencia>
}