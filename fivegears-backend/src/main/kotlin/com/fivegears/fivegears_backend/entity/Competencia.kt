package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.NivelCompetencia
import jakarta.persistence.*
@Entity
@Table(name = "competencia")
data class Competencia(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idCompetencia: Int = 0,  // PK interna

    val nome: String,
    val descricao: String? = null,
    val tipo: String? = null,
    val categoria: String? = null,

    @Enumerated(EnumType.STRING)
    val nivelRecomendado: NivelCompetencia = NivelCompetencia.BASICO
)
