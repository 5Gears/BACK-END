package com.fivegears.fivegears_backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class GeminiConfig {
    @Value("\${gemini.api.key}")
    lateinit var apiKey: String

    val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"
}
