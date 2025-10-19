package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.OrigemCargo
import com.fivegears.fivegears_backend.entity.enum.Senioridade
import jakarta.persistence.*

@Entity
@Table(name = "cargo")
data class Cargo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    val idCargo: Int? = null,

    @Column(nullable = false)
    val nome: String,

    val descricao: String? = null,

    @Enumerated(EnumType.STRING)
    val senioridade: Senioridade = Senioridade.JUNIOR,

    @Enumerated(EnumType.STRING)
    val origem: OrigemCargo = OrigemCargo.INTERNO,

    @ManyToOne
    @JoinColumn(name = "id_esco_cargo")
    val escoCargo: EscoCargo? = null,

    // Relações
    @OneToMany(mappedBy = "cargo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cargoCompetencias: MutableList<CargoCompetencia> = mutableListOf(),

    @OneToMany(mappedBy = "cargo", cascade = [CascadeType.ALL], orphanRemoval = true)
    val usuariosCargo: MutableList<UsuarioCargo> = mutableListOf()
)
