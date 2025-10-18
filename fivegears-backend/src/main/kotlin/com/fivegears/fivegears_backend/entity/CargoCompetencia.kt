package com.fivegears.fivegears_backend.entity

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

    val peso: Int = 1,

    val tipoRelacao: String? = null
)


