package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "empresa")
data class Empresa(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var fundador: String? = null,

    @Column(nullable = false, unique = true, length = 18)
    var cnpj: String,

    @OneToMany(mappedBy = "empresa")
    val enderecos: MutableList<Endereco> = mutableListOf(),

    @OneToMany(mappedBy = "empresa")
    val usuarios: MutableList<Usuario> = mutableListOf()
){

    constructor(id: Int) : this(id, "", null, "")
}
