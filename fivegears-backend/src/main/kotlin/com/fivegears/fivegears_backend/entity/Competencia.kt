package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.NivelCompetencia
import jakarta.persistence.*

@Entity
@Table(name = "competencia")
data class Competencia(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_competencia")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String? = null,

    var codigoEsco: String? = null,
    var tipo: String? = null,
    var categoria: String? = null,

    @Enumerated(EnumType.STRING)
    var nivelRecomendado: NivelCompetencia = NivelCompetencia.BASICO
)
