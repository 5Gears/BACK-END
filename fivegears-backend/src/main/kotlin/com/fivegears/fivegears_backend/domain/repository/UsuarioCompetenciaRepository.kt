package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.UsuarioCompetencia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioCompetenciaRepository : JpaRepository<UsuarioCompetencia, Int>