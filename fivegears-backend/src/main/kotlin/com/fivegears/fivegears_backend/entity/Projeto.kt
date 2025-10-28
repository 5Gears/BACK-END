package com.fivegears.fivegears_backend.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fivegears.fivegears_backend.entity.enum.StatusProjeto
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

    @Enumerated(EnumType.STRING)
    var status: StatusProjeto = StatusProjeto.EM_PLANEJAMENTO,

    var dataInicio: LocalDate? = null,
    var dataFim: LocalDate? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsavel", nullable = false)
    var responsavel: Usuario,

    @OneToMany(mappedBy = "projeto", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "projeto-usuarioProjeto")
    val usuarios: MutableList<UsuarioProjeto> = mutableListOf(),

    var competenciasRequeridas: String? = null
)

