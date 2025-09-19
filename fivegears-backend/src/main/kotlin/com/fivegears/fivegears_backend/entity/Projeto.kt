package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "projeto")
data class Projeto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projeto")
    val id: Int? = null,

    @ManyToOne @JoinColumn(name = "id_cliente")
    var cliente: Cliente? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String?,
    var tempoEstimadoHoras: Int?,
    var orcamento: java.math.BigDecimal?,
    var status: String? = "EM_PLANEJAMENTO",
    var dataInicio: java.time.LocalDate?,
    var dataFim: java.time.LocalDate?,

    @ManyToOne @JoinColumn(name = "id_responsavel", nullable = false)
    var responsavel: Usuario
)
