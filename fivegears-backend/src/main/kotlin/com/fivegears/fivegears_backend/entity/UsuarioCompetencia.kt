package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "usuario_competencia")
data class UsuarioCompetencia(
    @EmbeddedId
    val id: UsuarioCompetenciaId,

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    var usuario: Usuario,

    @ManyToOne
    @MapsId("idCompetencia")
    @JoinColumn(name = "id_competencia")
    var competencia: Competencia,

    @Column(name = "ultima_utilizacao")
    var ultimaUtilizacao: LocalDate? = null
)
