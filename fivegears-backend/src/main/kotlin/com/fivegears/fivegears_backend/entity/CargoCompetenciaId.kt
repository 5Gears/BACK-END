import com.fivegears.fivegears_backend.entity.Cargo
import com.fivegears.fivegears_backend.entity.Competencia
import com.fivegears.fivegears_backend.entity.enum.TipoRelacaoCompetencia
import jakarta.persistence.*

@Embeddable
data class CargoCompetenciaId(
    val idCargo: Int,
    val idCompetencia: Int
)

