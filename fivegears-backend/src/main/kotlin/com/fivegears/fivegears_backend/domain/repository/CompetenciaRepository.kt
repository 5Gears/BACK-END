package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.Competencia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompetenciaRepository : JpaRepository<Competencia, Int>