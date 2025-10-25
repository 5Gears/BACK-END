package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.FonteCargo
import jakarta.persistence.*

@Entity
@Table(name = "cargo")
data class Cargo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idCargo: Int? = null,

    @Column(nullable = false, unique = true)
    val nome: String,

    val descricao: String? = null,

    @Enumerated(EnumType.STRING)
    val fonte: FonteCargo = FonteCargo.INTERNO,

    // Relações
    @OneToMany(mappedBy = "cargo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cargoCompetencias: MutableList<CargoCompetencia> = mutableListOf(),

    @OneToMany(mappedBy = "cargo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val usuariosCargo: MutableList<UsuarioCargo> = mutableListOf()
)
