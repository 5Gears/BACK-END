package school.sptech.projetotfg.domain

import jakarta.persistence.*

@Entity
@Table(name = "empresa")
data class Empresa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    val id: Long = 0,

    @Column(nullable = false)
    val nome: String
)
