package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "usuario_cargo")
data class UsuarioCargo(
    @EmbeddedId
    val id: UsuarioCargoId,

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    var usuario: Usuario,

    @ManyToOne
    @MapsId("idCargo")
    @JoinColumn(name = "id_cargo")
    var cargo: Cargo
)
