package com.fivegears.fivegears_backend.domain.service.impl.interfaces

import com.fivegears.fivegears_backend.dto.FeedbackRequestDTO

interface SoftSkillFeedbackService {
    fun avaliarSoftSkills(idAvaliador: Int, feedback: FeedbackRequestDTO)
}
