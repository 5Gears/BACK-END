import com.fivegears.fivegears_backend.entity.Cargo
import com.fivegears.fivegears_backend.entity.Competencia
import com.fivegears.fivegears_backend.entity.enum.TipoRelacaoCompetencia
import jakarta.persistence.*

@Embeddable
data class CargoCompetenciaId(
    val idCargo: Int,
    val idCompetencia: Int
)

@Entity
@Table(name = "cargo_competencia")
data class CargoCompetencia(
    @EmbeddedId
    val id: CargoCompetenciaId,

    @ManyToOne
    @MapsId("idCargo")
    @JoinColumn(name = "id_cargo")
    val cargo: Cargo,

    @ManyToOne
    @MapsId("idCompetencia")
    @JoinColumn(name = "id_competencia")
    val competencia: Competencia,

    val peso: Int = 1,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_relacao")
    val tipoRelacao: TipoRelacaoCompetencia = TipoRelacaoCompetencia.REQUERIDA
)
