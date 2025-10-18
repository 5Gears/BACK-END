package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.NivelCompetencia
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "usuario_competencia")
@IdClass(UsuarioCompetenciaId::class)
data class UsuarioCompetencia(
    @Id
    @ManyToOne @JoinColumn(name = "id_usuario")
    val usuario: Usuario,

    @Id
    @ManyToOne @JoinColumn(name = "id_competencia")
    val competencia: Competencia,

    @Enumerated(EnumType.STRING)
    var nivel: NivelCompetencia = NivelCompetencia.BASICO,

    @Column(name = "experiencia_anos")
    var experienciaAnos: Int = 0,

    @Column(name = "ultima_utilizacao")
    var ultimaUtilizacao: LocalDate? = null
)
