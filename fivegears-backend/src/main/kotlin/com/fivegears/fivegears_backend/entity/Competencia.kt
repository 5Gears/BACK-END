package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "competencia")
data class Competencia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_competencia")
    val idCompetencia: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String? = null,
    var tipo: String? = null,
    var categoria: String? = null,

)
