package school.sptech.projetotfg.service

import org.springframework.stereotype.Service
import school.sptech.projetotfg.domain.Usuario
import school.sptech.projetotfg.dto.UsuarioDTO
import school.sptech.projetotfg.repository.EmpresaRepository
import school.sptech.projetotfg.repository.UsuarioRepository

@Service
class UsuarioService(
    private val repository: UsuarioRepository,
    private val empresaRepository: EmpresaRepository
) {
    fun listar(): List<Usuario> = repository.findAll()

    fun buscarPorId(id: Long): Usuario? = repository.findById(id).orElse(null)

    fun salvar(dto: UsuarioDTO): Usuario {
        val empresa = dto.idEmpresa?.let { empresaRepository.findById(it).orElse(null) }
        val usuario = Usuario(
            nome = dto.nome,
            email = dto.email,
            cpf = dto.cpf,
            telefone = dto.telefone,
            empresa = empresa
        )
        return repository.save(usuario)
    }

    fun deletar(id: Long) = repository.deleteById(id)
}
