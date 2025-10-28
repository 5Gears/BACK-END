package com.fivegears.fivegears_backend.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "empresa")
data class Empresa(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var fundador: String? = null,

    @Column(nullable = false, unique = true, length = 64)
    var cnpj: String,

    @OneToMany(mappedBy = "empresa", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "empresa-endereco")
    var enderecos: MutableList<Endereco> = mutableListOf(),

    @OneToMany(mappedBy = "empresa", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference(value = "empresa-usuario")
    val usuarios: MutableList<Usuario> = mutableListOf()
) {
    constructor(id: Int) : this(id, "", null, "")
}
