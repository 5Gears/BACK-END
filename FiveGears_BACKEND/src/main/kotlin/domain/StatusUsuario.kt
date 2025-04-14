package school.sptech.projetotfg.domain
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "status_usuario")
data class StatusUsuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    val usuario: Usuario,

    @Column(name = "data_entrada")
    val dataEntrada: LocalDateTime,

    @Column(name = "data_saida")
    val dataSaida: LocalDateTime? = null,

    @Column(name = "status_atual")
    val statusAtual: String
)