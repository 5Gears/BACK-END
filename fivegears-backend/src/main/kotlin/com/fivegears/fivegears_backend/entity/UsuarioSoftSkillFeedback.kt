package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.NivelSoftSkill

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "usuario_soft_skill_feedback")
data class UsuarioSoftSkillFeedback(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_soft_skill", nullable = false)
    var softSkill: SoftSkill,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avaliador", nullable = false)
    var avaliador: Usuario,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var nivel: NivelSoftSkill,

    @Column(columnDefinition = "TEXT")
    var comentario: String? = null,

    @Column(name = "data_avaliacao")
    var dataAvaliacao: LocalDateTime? = LocalDateTime.now()
)
