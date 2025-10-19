package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.UsuarioSoftSkillFeedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioSoftSkillFeedbackRepository : JpaRepository<UsuarioSoftSkillFeedback, Int>