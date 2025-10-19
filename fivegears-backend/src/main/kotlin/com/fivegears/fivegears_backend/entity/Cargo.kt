package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.Senioridade
import jakarta.persistence.*

@Entity
@Table(name = "cargo")
data class Cargo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idCargo: Int = 0,

    var nome: String,
    var descricao: String? = null,

    @Enumerated(EnumType.STRING)
    val senioridade: Senioridade = Senioridade.JUNIOR
)

