package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "endereco")
data class Endereco(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    val id: Int? = null,

    @Column(nullable = false)
    var rua: String,

    var numero: String? = null,
    var bairro: String? = null,
    var cidade: String? = null,
    var estado: String? = null,
    var cep: String? = null,

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa? = null,

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    var cliente: Cliente? = null
)
