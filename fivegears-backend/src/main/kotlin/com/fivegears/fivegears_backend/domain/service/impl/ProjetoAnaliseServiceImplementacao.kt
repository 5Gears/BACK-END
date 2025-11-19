package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.dto.AnalisePDFResponseDTO
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

@Service
class ProjetoAnaliseServiceImplementacao(
    @Value("\${gemini.api.key}") private val apiKey: String
) {

    fun analisarPDF(file: MultipartFile): AnalisePDFResponseDTO {
        val texto = extrairTexto(file)
        val dadosIA = chamarGemini(texto)

        val nomeProjeto = dadosIA.optString("nomeProjeto", null)
        val descricao = dadosIA.optString("descricao", null)

        val tempoEstimado = dadosIA.optInt("tempoEstimadoHoras", -1)
            .let { if (it >= 0) it else null }

        val orcamento = dadosIA.optDouble("orcamento", Double.NaN)
            .let { if (!it.isNaN()) it else null }

        val dataInicio = dadosIA.optString("dataInicio", null)
        val dataFim = dadosIA.optString("dataFim", null)
        val cliente = dadosIA.optString("cliente", null)

        val competencias = when (val comp = dadosIA.opt("competencias")) {
            is org.json.JSONArray ->
                if (comp.length() == 0) null
                else (0 until comp.length()).joinToString(", ") { comp.getString(it) }

            is String -> comp
            else -> null
        }

        return AnalisePDFResponseDTO(
            nomeProjeto = nomeProjeto,
            descricao = descricao,
            tempoEstimadoHoras = tempoEstimado,
            orcamento = orcamento,
            dataInicio = dataInicio,
            dataFim = dataFim,
            cliente = cliente,
            competencias = competencias
        )
    }

    private fun extrairTexto(file: MultipartFile): String =
        PDDocument.load(file.inputStream).use { doc ->
            PDFTextStripper().getText(doc)
        }

    private fun chamarGemini(texto: String): JSONObject {
        val prompt = """
            Você é um extrator de informações. Analise o texto do PDF e retorne APENAS um JSON válido (sem markdown, sem comentários, sem texto extra).

            Os campos do JSON DEVEM ter esta estrutura (NOMES EXATOS):

            {
              "nomeProjeto": "string ou null",
              "descricao": "string ou null",
              "tempoEstimadoHoras": número ou null,
              "orcamento": número ou null,
              "dataInicio": "YYYY-MM-DD ou null",
              "dataFim": "YYYY-MM-DD ou null",
              "cliente": "string ou null",
              "competencias": ["string", "string"] ou []
            }

            Regras:
            - Se não houver dado, retorne null ou array vazio.
            - Nunca inclua explicações.
            - Nunca inclua ```json.
            - Datas sempre em YYYY-MM-DD.

            TEXTO DO PDF:
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
