package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "projeto")
data class Projeto(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projeto")
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    var cliente: Cliente? = null,

    @Column(nullable = false)
    var nome: String,

    var descricao: String? = null,
    @Column(name = "tempo_estimado_horas")
    var tempoEstimadoHoras: Int? = null,
    var orcamento: BigDecimal? = null,
    var status: String? = "EM_PLANEJAMENTO",
    var dataInicio: LocalDate? = null,
    var dataFim: LocalDate? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel", nullable = false)
    var responsavel: Usuario,

    @OneToMany(mappedBy = "projeto", cascade = [CascadeType.ALL], orphanRemoval = true)
    val usuarios: MutableList<UsuarioProjeto> = mutableListOf()
)
