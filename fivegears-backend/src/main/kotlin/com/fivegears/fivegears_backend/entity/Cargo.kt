package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.Senioridade
import jakarta.persistence.*

@Entity
@Table(name = "cargo")
data class Cargo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String? = null,

    @Enumerated(EnumType.STRING)
    var senioridade: Senioridade = Senioridade.JUNIOR
)
