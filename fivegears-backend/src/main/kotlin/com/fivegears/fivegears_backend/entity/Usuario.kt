package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(unique = true)
    var cpf: String? = null,

    var telefone: String? = null,

    var area: String? = null,

    @Column(name = "carga_horaria")
    var cargaHoraria: Int = 0,

    @Column(name = "valor_hora")
    var valorHora: Double = 0.0,

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa? = null,

    @ManyToOne
    @JoinColumn(name = "id_nivel")
    var nivelPermissao: NivelPermissao? = null
)

