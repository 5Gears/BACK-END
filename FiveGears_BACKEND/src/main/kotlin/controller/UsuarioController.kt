package school.sptech.projetotfg.controller

import org.springframework.web.bind.annotation.*
import school.sptech.projetotfg.domain.Usuario
import school.sptech.projetotfg.dto.UsuarioDTO
import school.sptech.projetotfg.service.UsuarioService

@RestController
@RequestMapping("/api/usuario")
class UsuarioController(private val service: UsuarioService) {

    @GetMapping
    fun listar(): List<Usuario> = service.listar()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): Usuario? = service.buscarPorId(id)

    @PostMapping
    fun salvar(@RequestBody dto: UsuarioDTO): Usuario = service.salvar(dto)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long) = service.deletar(id)
}
