package com.fivegears.fivegears_backend.entity

import com.fivegears.fivegears_backend.entity.enum.NivelPermissaoEnum
import jakarta.persistence.*

@Entity
@Table(name = "nivel_permissao")
data class NivelPermissao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val nome: NivelPermissaoEnum,

    var descricao: String? = null,

    @OneToMany(mappedBy = "nivelPermissao")
    val usuarios: MutableList<Usuario> = mutableListOf()
) {
    // Construtor auxiliar para permitir criar apenas com o ID
    constructor(id: Int?) : this(
        id = id,
        nome = NivelPermissaoEnum.FUNCIONARIO, // valor padrão temporário
        descricao = null
    )
}
