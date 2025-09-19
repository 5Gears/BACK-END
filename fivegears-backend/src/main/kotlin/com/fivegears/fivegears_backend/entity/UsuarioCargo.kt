package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "usuario_cargo")
@IdClass(UsuarioCargoId::class)
data class UsuarioCargo(
    @Id
    @ManyToOne @JoinColumn(name = "id_usuario")
    val usuario: Usuario,

    @Id
    @ManyToOne @JoinColumn(name = "id_cargo")
    val cargo: Cargo
)

