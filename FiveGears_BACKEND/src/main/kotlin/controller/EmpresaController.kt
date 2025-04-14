package school.sptech.projetotfg.controller

import org.springframework.web.bind.annotation.*
import school.sptech.projetotfg.domain.Empresa
import school.sptech.projetotfg.service.EmpresaService

@RestController
@RequestMapping("/api/empresa")
class EmpresaController(private val service: EmpresaService) {

    @GetMapping
    fun listar(): List<Empresa> = service.listar()

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): Empresa? = service.buscarPorId(id)

    @PostMapping
    fun salvar(@RequestBody empresa: Empresa): Empresa = service.salvar(empresa)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long) = service.deletar(id)
}
