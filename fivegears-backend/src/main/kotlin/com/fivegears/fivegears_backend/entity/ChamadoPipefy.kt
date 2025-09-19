package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "chamado_pipefy")
data class ChamadoPipefy(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chamado")
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    var usuario: Usuario? = null,

    var titulo: String?,
    var descricao: String?,
    var dataCriacao: java.time.LocalDateTime? = null,
    var status: String? = "ABERTO",
    var idPipefyCard: String?,
    var tipoChamado: String?
)
