package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.Cliente
import com.fivegears.fivegears_backend.entity.Projeto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjetoRepository : JpaRepository<Projeto, Int> {

    fun findByNomeContainingIgnoreCase(nome: String): List<Projeto>
    fun findByNomeIgnoreCase(nome: String): Projeto?
    fun findByStatus(status: String): List<Projeto>
}
