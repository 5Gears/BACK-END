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

    @Column(nullable = false, unique = true, length = 18)
    var cnpj: String,

    // A empresa é o "lado principal" da relação com endereço e usuário
    @OneToMany(mappedBy = "empresa", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var enderecos: MutableList<Endereco> = mutableListOf(),

    @OneToMany(mappedBy = "empresa", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonManagedReference
    val usuarios: MutableList<Usuario> = mutableListOf()
) {
    constructor(id: Int) : this(id, "", null, "")
}
