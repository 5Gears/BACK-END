import com.fivegears.fivegears_backend.domain.service.impl.GeminiServiceImplementacao
import com.fivegears.fivegears_backend.domain.service.impl.ProjetoServiceImplementacao
import com.fivegears.fivegears_backend.dto.MensagemRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/assistente/chatbot")
class AssistenteController(
    private val geminiService: GeminiServiceImplementacao,
    private val projetoService: ProjetoServiceImplementacao
) {

    @PostMapping("/validar-projeto")
    fun validarProjeto(@RequestBody request: MensagemRequest): ResponseEntity<Any> {
        val projeto = projetoService.buscarPorNome(request.nomeProjeto ?: "")
            ?: return ResponseEntity.badRequest().body(mapOf("erro" to "Projeto '${request.nomeProjeto}' não encontrado"))

        return ResponseEntity.ok(mapOf(
            "projeto" to projeto.nome,
            "idProjeto" to projeto.id
        ))
    }

    @PostMapping("/demandar-profissionais")
    fun demandarProfissionais(@RequestBody request: MensagemRequest): ResponseEntity<Any> {
        val projeto = when {
            request.idProjeto != null -> projetoService.buscarPorId(request.idProjeto)
            !request.nomeProjeto.isNullOrBlank() -> projetoService.buscarPorNome(request.nomeProjeto)
            else -> null
        } ?: return ResponseEntity.badRequest()
            .body(mapOf("erro" to "Projeto '${request.nomeProjeto}' não encontrado"))

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
