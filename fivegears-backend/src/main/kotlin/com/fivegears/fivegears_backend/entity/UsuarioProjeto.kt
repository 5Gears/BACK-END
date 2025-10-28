package com.fivegears.fivegears_backend.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "usuario_projeto")
@IdClass(UsuarioProjetoId::class)
data class UsuarioProjeto(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto", nullable = false)
    @JsonBackReference(value = "projeto-usuarioProjeto")
    var projeto: Projeto? = null,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference(value = "usuario-usuarioProjeto")
    var usuario: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo")
    var cargo: Cargo? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: com.fivegears.fivegears_backend.entity.enum.StatusAlocacao = com.fivegears.fivegears_backend.entity.enum.StatusAlocacao.ALOCADO,

    @Column(name = "horas_alocadas")
    var horasAlocadas: Int = 0,

    @Column(name = "horas_por_dia")
    var horasPorDia: Int = 0,

    @Column(name = "data_alocacao", nullable = false)
    var dataAlocacao: LocalDate? = null,

    @Column(name = "data_saida")
    var dataSaida: LocalDate? = null
) {
    @PrePersist
    fun prePersist() {
        if (dataAlocacao == null) {
            dataAlocacao = LocalDate.now()
        }
    }
}
