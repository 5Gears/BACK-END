package com.fivegears.fivegears_backend.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

@Entity
@Table(name = "cliente")
data class Cliente(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    @Column(nullable = false, unique = true, length = 18)
    var cnpj: String,

    var emailResponsavel: String? = null,

    // Cliente é o lado principal da relação com Endereco
    @OneToMany(mappedBy = "cliente", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var enderecos: MutableList<Endereco> = mutableListOf()
) {
    constructor(id: Int) : this(id, "", "", null)
}
