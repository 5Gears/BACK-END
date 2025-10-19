package com.fivegears.fivegears_backend.controller

import com.fivegears.fivegears_backend.domain.service.impl.GeminiServiceImplementacao
import com.fivegears.fivegears_backend.dto.MensagemRequest
import com.fivegears.fivegears_backend.dto.UsuarioAlocadoDTO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/assistente")
class AssistenteController(
    private val geminiService: GeminiServiceImplementacao
) {

    @PostMapping
    fun conversar(@RequestBody request: MensagemRequest): List<UsuarioAlocadoDTO> {
        //  Gerar filtro baseado na mensagem do gerente
        val filtro = geminiService.gerarFiltro(request.mensagem)

        //  Buscar usuários do banco com base no filtro
        val usuariosFiltrados = geminiService.buscarUsuarios(filtro)

        //  Retornar lista de usuários filtrados em JSON
        return usuariosFiltrados
    }
}
