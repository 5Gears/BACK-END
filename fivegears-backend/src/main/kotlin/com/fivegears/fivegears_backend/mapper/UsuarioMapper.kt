package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.UsuarioDTO
import com.fivegears.fivegears_backend.entity.Empresa
import com.fivegears.fivegears_backend.entity.NivelPermissao
import com.fivegears.fivegears_backend.entity.Usuario

object UsuarioMapper {

    fun toDTO(entity: Usuario, idCargo: Int? = null): UsuarioDTO = UsuarioDTO(
        idUsuario = entity.id,
        nome = entity.nome,
        email = entity.email,
        cpf = entity.cpf,
        telefone = entity.telefone,
        area = entity.area,
        idCargo = idCargo,
        cargaHoraria = entity.cargaHoraria,
        valorHora = entity.valorHora,
        idEmpresa = entity.empresa?.id,
        idNivel = entity.nivelPermissao?.id
    )

    fun toEntity(dto: UsuarioDTO): Usuario = Usuario(
        id = dto.idUsuario,
        nome = dto.nome,
        email = dto.email,
        cpf = dto.cpf,
        telefone = dto.telefone,
        area = dto.area,
        cargaHoraria = dto.cargaHoraria,
        valorHora = dto.valorHora
    ).apply {
        empresa = dto.idEmpresa?.let { Empresa(it) }
        nivelPermissao = dto.idNivel?.let { NivelPermissao(it) }
    }
}
