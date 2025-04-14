package school.sptech.projetotfg.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "login")
data class LoginEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    val idLogin: Int = 0,

    @Column(name = "id_usuario", nullable = false)
    val idUsuario: Int,

    @Column(nullable = false)
    val senha: String,

    @Column(name = "ultimo_login")
    val ultimoLogin: LocalDateTime? = null
)
