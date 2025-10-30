package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.ProjetoAnaliseServiceImplementacao
import com.fivegears.fivegears_backend.dto.AnalisePDFResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/projetos/pdf")
@Tag(name = "Projetos - IA PDF", description = "Leitura e análise de PDF para gerar dados estruturados")
class ProjetoPDFController(
    private val projetoAnaliseService: ProjetoAnaliseServiceImplementacao
) {

    @PostMapping("/analisar", consumes = ["multipart/form-data"])
    @Operation(summary = "Analisa um PDF e salva a descrição e competências extraídas")
    fun analisarPDF(@RequestParam("file") file: MultipartFile): ResponseEntity<AnalisePDFResponseDTO> {
        val resultado = projetoAnaliseService.analisarPDF(file)
        return ResponseEntity.ok(resultado)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma análise de PDF por ID (se ainda estiver ativa)")
    fun buscarAnalise(@PathVariable id: UUID): ResponseEntity<AnalisePDFResponseDTO> {
        val analise = projetoAnaliseService.buscarPorId(id)
        return ResponseEntity.ok(
            AnalisePDFResponseDTO(
                draftId = analise.id.toString(),
                descricao = analise.descricaoExtraida,
                competencias = analise.competenciasRequeridas
            )
        )
    }
}
