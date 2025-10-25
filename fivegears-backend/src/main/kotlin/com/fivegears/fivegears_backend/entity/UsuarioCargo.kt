package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.SenioridadeCargo
import jakarta.persistence.*
import java.time.LocalDate
import java.time.Period

@Entity
@Table(name = "usuario_cargo")
data class UsuarioCargo(

    @EmbeddedId
    val id: UsuarioCargoId,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    val usuario: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCargo")
    @JoinColumn(name = "id_cargo")
    val cargo: Cargo? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var senioridade: SenioridadeCargo = SenioridadeCargo.JUNIOR,

    @Column(name = "data_inicio", nullable = false)
    var dataInicio: LocalDate = LocalDate.now(),

    @Column(name = "ultima_atualizacao")
    var ultimaAtualizacao: LocalDate? = null
) {
    @get:Transient
    val experienciaAnos: Int
        get() = Period.between(dataInicio, LocalDate.now()).years

    @PrePersist
    fun aoCriar() {
        if (dataInicio == null) dataInicio = LocalDate.now()
        ultimaAtualizacao = LocalDate.now()
    }

    @PreUpdate
    fun aoAtualizar() {
        ultimaAtualizacao = LocalDate.now()
    }
}
