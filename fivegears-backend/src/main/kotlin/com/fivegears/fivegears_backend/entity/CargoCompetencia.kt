package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.TipoRelacaoCompetencia
import jakarta.persistence.*

@Entity
@Table(name = "cargo_competencia")
@IdClass(CargoCompetenciaId::class)
data class CargoCompetencia(
    @Id
    @ManyToOne @JoinColumn(name = "id_cargo")
    val cargo: Cargo,

    @Id
    @ManyToOne @JoinColumn(name = "id_competencia")
    val competencia: Competencia,

    var peso: Int = 1,

    @Enumerated(EnumType.STRING)
    val tipoRelacao: TipoRelacaoCompetencia = TipoRelacaoCompetencia.RECOMENDADA
)


