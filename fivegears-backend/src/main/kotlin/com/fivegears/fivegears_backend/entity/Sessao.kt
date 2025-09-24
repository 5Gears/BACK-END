package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sessao")
data class Sessao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sessao")
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_login", nullable = false)
    val login: Login,

    @Column(nullable = false)
    var token: String,

    @ManyToOne
    @JoinColumn(name = "id_status", nullable = false)
    var status: StatusUsuario,

    @Column(name = "inicio_sessao")
    var inicioSessao: LocalDateTime = LocalDateTime.now(),

    @Column(name = "fim_sessao")
    var fimSessao: LocalDateTime? = null
)