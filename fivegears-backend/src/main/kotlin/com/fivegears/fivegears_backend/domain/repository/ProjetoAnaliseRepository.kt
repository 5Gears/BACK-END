package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.ProjetoAnalise
import com.fivegears.fivegears_backend.entity.enum.StatusAnalise
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProjetoAnaliseRepository : JpaRepository<ProjetoAnalise, UUID> {
    fun findByIdAndStatus(id: UUID, status: StatusAnalise): Optional<ProjetoAnalise>
}