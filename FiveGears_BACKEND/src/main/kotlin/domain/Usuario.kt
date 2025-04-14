package school.sptech.projetotfg.domain

import jakarta.persistence.*

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    val id: Long = 0,

    @Column(nullable = false)
    val nome: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val cpf: String,

    val telefone: String? = null,

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    val empresa: Empresa?
)
