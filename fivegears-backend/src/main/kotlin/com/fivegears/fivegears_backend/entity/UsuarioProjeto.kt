package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "usuario_projeto")
@IdClass(UsuarioProjetoId::class)
data class UsuarioProjeto(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto")
    val projeto: Projeto,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    val usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo")
    val cargo: Cargo,

    @Column(name = "status")
    var status: String = "ALOCADO",

    @Column(name = "horas_alocadas")
    var horasAlocadas: Int? = 0,

    @Column(name = "horas_por_dia")
    var horasPorDia: Int? = 0,

    @Column(name = "data_alocacao")
    var dataAlocacao: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "data_saida")
    var dataSaida: LocalDateTime? = null
)
