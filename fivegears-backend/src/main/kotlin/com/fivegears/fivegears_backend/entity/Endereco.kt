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

    var numero: String?,
    var bairro: String?,
    var cidade: String?,
    var estado: String?,
    var cep: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa? = null
)
