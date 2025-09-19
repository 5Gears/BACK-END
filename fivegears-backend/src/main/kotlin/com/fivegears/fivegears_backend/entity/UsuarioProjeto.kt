package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "usuario_projeto")
@IdClass(UsuarioProjetoId::class)
data class UsuarioProjeto(
    @Id
    @ManyToOne @JoinColumn(name = "id_projeto")
    val projeto: Projeto,

    @Id
    @ManyToOne @JoinColumn(name = "id_usuario")
    val usuario: Usuario,

    @ManyToOne @JoinColumn(name = "id_cargo")
    val cargo: Cargo,

    var horasAlocadas: Int? = 0,
    var horasPorDia: Int? = 0,
    var dataAlocacao: java.time.LocalDate? = java.time.LocalDate.now(),
    var dataSaida: java.time.LocalDate? = null
)


