package school.sptech.projetotfg.repository

import school.sptech.projetotfg.domain.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Int> {
    fun findByEmail(email: String): Usuario?
}