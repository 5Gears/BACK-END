package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.TipoFeedback
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "feedback")
data class Feedback(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    val id: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_avaliador", nullable = false)
    val avaliador: Usuario,

    @ManyToOne
    @JoinColumn(name = "id_avaliado", nullable = false)
    val avaliado: Usuario,

    @Enumerated(EnumType.STRING)
    var tipo: TipoFeedback = TipoFeedback.COMPORTAMENTAL,

    var comentario: String? = null,

    var nota: Int? = null,

    @Column(name = "data_feedback")
    var dataFeedback: LocalDateTime = LocalDateTime.now()
)
