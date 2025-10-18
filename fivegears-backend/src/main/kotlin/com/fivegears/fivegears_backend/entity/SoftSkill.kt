package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "soft_skill")
data class SoftSkill(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_soft_skill")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String? = null
)
