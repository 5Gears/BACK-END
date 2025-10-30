package com.fivegears.fivegears_backend.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(unique = true)
    var cpf: String? = null,

    var telefone: String? = null,
    var area: String? = null,

    @Column(name = "carga_horaria", nullable = false)
    var cargaHoraria: Int = 40,

    @Column(name = "valor_hora")
    var valorHora: BigDecimal? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    @JsonBackReference(value = "empresa-usuario")
    var empresa: Empresa? = null,

    @ManyToOne
    @JoinColumn(name = "id_nivel")
    var nivelPermissao: NivelPermissao? = null,

    @OneToMany(mappedBy = "usuario", cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonManagedReference(value = "usuario-usuarioProjeto")
    var projetos: MutableList<UsuarioProjeto> = mutableListOf()
)