package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.NivelSoftSkill
import com.fivegears.fivegears_backend.entity.enum.FonteAvaliacao
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "usuario_soft_skill")
@IdClass(UsuarioSoftSkillId::class)
data class UsuarioSoftSkill(
    @Id
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    val usuario: Usuario,

    @Id
    @ManyToOne
    @JoinColumn(name = "id_soft_skill")
    val softSkill: SoftSkill,

    @Enumerated(EnumType.STRING)
    var nivel: NivelSoftSkill = NivelSoftSkill.MEDIO,

    @Column(name = "ultima_avaliacao")
    var ultimaAvaliacao: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    var fonteAvaliacao: FonteAvaliacao = FonteAvaliacao.GERENTE,

    var comentario: String? = null
)
