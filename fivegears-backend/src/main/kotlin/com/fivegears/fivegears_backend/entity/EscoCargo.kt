package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "esco_cargo")
data class EscoCargo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_esco_cargo")
    val idEscoCargo: Int? = null,

    @Column(name = "nome_cargo", nullable = false)
    val nomeCargo: String
) {
    constructor(idEscoCargo: Int?) : this(
        idEscoCargo = idEscoCargo,
        nomeCargo = ""
    )
}
