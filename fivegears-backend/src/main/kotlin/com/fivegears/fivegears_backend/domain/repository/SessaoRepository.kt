package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.Sessao
import org.springframework.data.jpa.repository.JpaRepository

interface SessaoRepository : JpaRepository<Sessao, Int> {
    fun findByLoginIdAndFimSessaoIsNull(idLogin: Int): Sessao?
}