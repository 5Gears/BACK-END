package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.ProjetoAnaliseServiceImplementacao
import com.fivegears.fivegears_backend.dto.AnalisePDFResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/projetos/pdf")
@Tag(name = "Projetos - IA PDF", description = "Leitura e análise de PDF para preencher automaticamente os campos do projeto")
class ProjetoPDFController(
    private val projetoAnaliseService: ProjetoAnaliseServiceImplementacao
) {

    @PostMapping("/analisar", consumes = ["multipart/form-data"])
    @Operation(summary = "Analisa um PDF e retorna dados estruturados para preencher o formulário de criação de projeto")
    fun analisarPDF(@RequestParam("file") file: MultipartFile): ResponseEntity<AnalisePDFResponseDTO> {
        val resultado = projetoAnaliseService.analisarPDF(file)
        return ResponseEntity.ok(resultado)
    }
}