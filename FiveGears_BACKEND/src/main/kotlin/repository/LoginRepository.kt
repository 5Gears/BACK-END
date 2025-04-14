package school.sptech.projetotfg.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import school.sptech.projetotfg.domain.LoginEntity
import java.time.LocalDateTime

@Repository
interface LoginRepository : JpaRepository<LoginEntity, Long> {

    fun findByIdUsuario(idUsuario: Int): LoginEntity?

    @Modifying
    @Transactional
    @Query("UPDATE LoginEntity l SET l.ultimoLogin = :data WHERE l.idLogin = :id")
    fun updateUltimoLogin(@Param("id") id: Int, @Param("data") data: LocalDateTime)
}
