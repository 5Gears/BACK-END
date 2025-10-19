package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.interfaces.SoftSkillFeedbackService
import com.fivegears.fivegears_backend.dto.FeedbackRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feedbacks")
class FeedbackController(
    private val feedbackService: SoftSkillFeedbackService
) {

    @PostMapping("/avaliar")
    fun avaliarSoftSkills(
        @RequestParam idAvaliador: Int,
        @RequestBody feedback: FeedbackRequestDTO
    ): ResponseEntity<String> {
        feedbackService.avaliarSoftSkills(idAvaliador, feedback)
        return ResponseEntity.ok("✅ Feedback de soft skills registrado com sucesso!")
    }
}
