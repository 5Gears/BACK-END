package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.GeminiServiceImplementacao
import com.fivegears.fivegears_backend.domain.service.impl.ProjetoServiceImplementacao
import com.fivegears.fivegears_backend.dto.MensagemRequest
import com.fivegears.fivegears_backend.dto.UsuarioAlocadoDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assistente")
class AssistenteController(
    private val geminiService: GeminiServiceImplementacao,
    private val projetoService: ProjetoServiceImplementacao
) {

    @PostMapping
    fun conversar(@RequestBody request: MensagemRequest): ResponseEntity<Any> {
        val projeto = projetoService.buscarPorNome(request.nomeProjeto ?: "")
            ?: return ResponseEntity.badRequest().body(mapOf("erro" to "Projeto não encontrado"))

        val filtro = geminiService.gerarFiltro(request.mensagem)
        val usuariosFiltrados = geminiService.buscarUsuarios(filtro)

        return ResponseEntity.ok(
            mapOf(
                "projeto" to projeto.nome,
                "idProjeto" to projeto.id,
                "usuarios" to usuariosFiltrados
            )
        )
    }
}
