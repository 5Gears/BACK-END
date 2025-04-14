package school.sptech.projetotfg.service

import org.springframework.stereotype.Service
import school.sptech.projetotfg.domain.Empresa
import school.sptech.projetotfg.repository.EmpresaRepository

@Service
class EmpresaService(private val repository: EmpresaRepository) {
    fun listar(): List<Empresa> = repository.findAll()
    fun buscarPorId(id: Long): Empresa? = repository.findById(id).orElse(null)
    fun salvar(empresa: Empresa): Empresa = repository.save(empresa)
    fun deletar(id: Long) = repository.deleteById(id)
}
