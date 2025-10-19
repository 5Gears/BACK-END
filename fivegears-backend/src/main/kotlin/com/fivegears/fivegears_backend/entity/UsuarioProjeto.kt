package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.StatusAlocacao
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "usuario_projeto")
@IdClass(UsuarioProjetoId::class)
data class UsuarioProjeto(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projeto", nullable = false)
    @JsonIgnore
    var projeto: Projeto? = null,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    var usuario: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo")
    var cargo: Cargo? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: StatusAlocacao = StatusAlocacao.ALOCADO,

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
