package com.fivegears.fivegears_backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "usuario")
data class Usuario(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    val id: Int? = null,

    @Column(nullable = false)
    var nome: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(unique = true, length = 14)
    var cpf: String? = null,

    var telefone: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    var empresa: Empresa? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel")
    var nivelPermissao: NivelPermissao? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status")
    var statusUsuario: StatusUsuario? = null,

    @OneToOne(mappedBy = "usuario", cascade = [CascadeType.ALL])
    var login: Login? = null,

    @OneToMany(mappedBy = "usuario")
    val chamados: MutableList<ChamadoPipefy> = mutableListOf(),

    @OneToMany(mappedBy = "usuario")
    val auditoria: MutableList<Auditoria> = mutableListOf()
)
