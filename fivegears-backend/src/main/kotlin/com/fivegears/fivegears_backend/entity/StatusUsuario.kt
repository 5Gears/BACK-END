package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "status_usuario")
data class StatusUsuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String? = null,
){
    constructor(id: Int) : this(id, "", null)
}
