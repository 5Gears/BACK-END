package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.CargoCompetencia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CargoCompetenciaRepository : JpaRepository<CargoCompetencia, Int>
