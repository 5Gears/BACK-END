package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "nivel_permissao")
data class NivelPermissao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String? = null,

    @OneToMany(mappedBy = "nivelPermissao")
    val usuarios: MutableList<Usuario> = mutableListOf()
){
    constructor(id: Int) : this(id, "", null)
}
