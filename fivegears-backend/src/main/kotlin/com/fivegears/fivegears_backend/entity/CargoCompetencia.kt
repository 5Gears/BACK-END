package com.fivegears.fivegears_backend.entity

import CargoCompetenciaId
import com.fivegears.fivegears_backend.entity.enum.TipoRelacaoCompetencia
import jakarta.persistence.*

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


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_relacao")
    val tipoRelacao: TipoRelacaoCompetencia = TipoRelacaoCompetencia.REQUERIDA
)