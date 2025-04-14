package school.sptech.projetotfg.repository


import org.springframework.data.jpa.repository.JpaRepository
import school.sptech.projetotfg.domain.StatusUsuario

interface StatusUsuarioRepository : JpaRepository<StatusUsuario, Int> {
    fun findByUsuarioIdAndStatusAtual(idUsuario: Long, status: String): StatusUsuario?
    fun findTopByUsuarioIdOrderByDataEntradaDesc(idUsuario: Long): StatusUsuario?

}
