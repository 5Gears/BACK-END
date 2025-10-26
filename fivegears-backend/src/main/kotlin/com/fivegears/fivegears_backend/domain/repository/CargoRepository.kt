package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.Cargo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CargoRepository : JpaRepository<Cargo, Int> {
    fun findByNomeContainingIgnoreCase(nome: String): List<Cargo>
    fun findByNome(nome: String): Cargo?
    fun existsByNome(nome: String): Boolean
}
