package school.sptech.projetotfg.domain

import jakarta.persistence.*

@Entity
@Table(name = "login")
data class Login(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val senha: String
)
