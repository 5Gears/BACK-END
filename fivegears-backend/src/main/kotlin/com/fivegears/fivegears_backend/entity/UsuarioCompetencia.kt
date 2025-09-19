package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "usuario_competencia")
@IdClass(UsuarioCompetenciaId::class)
data class UsuarioCompetencia(
    @Id
    @ManyToOne @JoinColumn(name = "id_usuario")
    val usuario: Usuario,

    @Id
    @ManyToOne @JoinColumn(name = "id_competencia")
    val competencia: Competencia
)


