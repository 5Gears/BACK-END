package school.sptech.projetotfg.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.Modifying
import org.springframework.transaction.annotation.Transactional
import school.sptech.projetotfg.domain.LoginEntity
import java.time.LocalDateTime

interface LoginRepository : JpaRepository<LoginEntity, Int> {
    fun findByUsuarioId(idUsuario: Long): LoginEntity?

    @Modifying
    @Transactional
    @Query("UPDATE LoginEntity l SET l.ultimoLogin = :date WHERE l.usuario.id = :idUsuario")
    fun updateUltimoLogin(idUsuario: Long, date: LocalDateTime)
}
