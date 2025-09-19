package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "cliente")
data class Cliente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    @Column(nullable = false, unique = true, length = 18)
    var cnpj: String,

    var emailResponsavel: String? = null,

    @OneToMany(mappedBy = "cliente")
    val projetos: MutableList<Projeto> = mutableListOf()
)
