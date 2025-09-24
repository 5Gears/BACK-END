package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "login")
data class Login(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_login")
    val id: Int? = null,

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    @Column(nullable = false)
    var senha: String,

    @OneToMany(mappedBy = "login", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val sessoes: List<Sessao> = mutableListOf()
)


