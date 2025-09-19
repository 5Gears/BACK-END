package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.AcaoAuditoria
import jakarta.persistence.*

@Entity
@Table(name = "auditoria")
data class Auditoria(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    val id: Int? = null,

    @Column(nullable = false)
    var tabelaAfetada: String,

    @Column(nullable = false)
    var idRegistro: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var acao: AcaoAuditoria,

    var valoresAnteriores: String?,
    var valoresNovos: String?,

    @ManyToOne @JoinColumn(name = "usuario_id")
    var usuario: Usuario,

    var dataEvento: java.time.LocalDateTime? = java.time.LocalDateTime.now()
)

