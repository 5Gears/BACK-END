package com.fivegears.fivegears_backend.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "endereco")
data class Endereco(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    val id: Int? = null,

    @Column(nullable = false)
    var rua: String,

    var numero: String? = null,
    var bairro: String? = null,
    var cidade: String? = null,
    var estado: String? = null,
    var cep: String? = null,

    // Evita loop empresa → endereço → empresa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    @JsonBackReference
    var empresa: Empresa? = null,

    // Evita loop cliente → endereço → cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    @JsonBackReference
    var cliente: Cliente? = null
)
