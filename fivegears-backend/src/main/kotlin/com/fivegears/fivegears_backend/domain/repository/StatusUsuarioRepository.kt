package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.StatusUsuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatusUsuarioRepository : JpaRepository<StatusUsuario, Int>