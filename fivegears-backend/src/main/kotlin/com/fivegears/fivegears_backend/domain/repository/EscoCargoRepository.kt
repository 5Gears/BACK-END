package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.EscoCargo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EscoCargoRepository : JpaRepository<EscoCargo, Int> {
    fun findByNomeCargoContainingIgnoreCase(nome: String): List<EscoCargo>
}
