package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.StatusAnalise
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "projeto_analise")
class ProjetoAnalise(
    @Id
    @Column(name = "id_analise", columnDefinition = "BINARY(16)")
    var id: UUID = UUID.randomUUID(),

    @Lob
    @Column(name = "descricao_extraida", nullable = false)
    var descricaoExtraida: String,

    @Lob
    @Column(name = "competencias_requeridas")
    var competenciasRequeridas: String? = null,

    @Enumerated(EnumType.STRING)
    var status: StatusAnalise = StatusAnalise.ATIVO,

    var dataCriacao: LocalDateTime = LocalDateTime.now()
)


