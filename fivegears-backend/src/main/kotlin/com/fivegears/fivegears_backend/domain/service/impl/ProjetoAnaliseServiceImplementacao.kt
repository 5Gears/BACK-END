package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.ProjetoAnaliseRepository
import com.fivegears.fivegears_backend.dto.AnalisePDFResponseDTO
import com.fivegears.fivegears_backend.entity.ProjetoAnalise
import com.fivegears.fivegears_backend.entity.enum.StatusAnalise
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

@Service
class ProjetoAnaliseServiceImplementacao(
    private val projetoAnaliseRepository: ProjetoAnaliseRepository,
    @Value("\${gemini.api.key}") private val apiKey: String
) {

    fun analisarPDF(file: MultipartFile): AnalisePDFResponseDTO {
        val texto = extrairTexto(file)
        val dadosIA = chamarGemini(texto)

        val descricao = dadosIA.optString("descricao", "Sem descrição detectada")
        val competencias = when (val comp = dadosIA.opt("competencias")) {
            is org.json.JSONArray -> (0 until comp.length()).joinToString(", ") { comp.getString(it) }
            is String -> comp
            else -> null
        }

        val analise = ProjetoAnalise(
            descricaoExtraida = descricao,
            competenciasRequeridas = competencias
        )

        projetoAnaliseRepository.save(analise)

        return AnalisePDFResponseDTO(
            draftId = analise.id.toString(),
            descricao = descricao,
            competencias = competencias
        )
    }

    fun buscarPorId(id: UUID): ProjetoAnalise =
        projetoAnaliseRepository.findByIdAndStatus(id, StatusAnalise.ATIVO)
            .orElseThrow { IllegalArgumentException("Análise não encontrada ou já utilizada") }

    fun marcarUsada(id: UUID) {
        val analise = buscarPorId(id)
        analise.status = StatusAnalise.USADO
        projetoAnaliseRepository.save(analise)
    }

    // ======================================
    // Utilitários internos
    // ======================================

    private fun extrairTexto(file: MultipartFile): String =
        PDDocument.load(file.inputStream).use { doc -> PDFTextStripper().getText(doc) }

    private fun chamarGemini(texto: String): JSONObject {
        val prompt = """
            Analise o texto abaixo e extraia os seguintes dados:
            - descricao: resumo do projeto
            - competencias: lista de competências requeridas

            Retorne APENAS um JSON válido no formato:
            {
              "descricao": "...",
              "competencias": ["...", "..."]
            }

            Texto:
            $texto
        """.trimIndent()

        val body = """{"contents":[{"parts":[{"text": ${JSONObject.quote(prompt)}}]}]}"""

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build()

        val client = HttpClient.newHttpClient()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val jsonResponse = JSONObject(response.body())
        val text = jsonResponse
            .getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")
            .replace("```json", "")
            .replace("```", "")
            .trim()

        return JSONObject(text)
    }
}
