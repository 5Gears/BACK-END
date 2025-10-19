package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.TipoRelacaoCompetencia
import jakarta.persistence.*

@Entity
@Table(name = "esco_competencia")
data class EscoCompetencia(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_esco_comp")
    val idEscoComp: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_esco_cargo", nullable = false)
    var escoCargo: EscoCargo,

    @Column(name = "nome_competencia", nullable = false)
    var nomeCompetencia: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_relacao")
    var tipoRelacao: TipoRelacaoCompetencia = TipoRelacaoCompetencia.REQUERIDA
)
