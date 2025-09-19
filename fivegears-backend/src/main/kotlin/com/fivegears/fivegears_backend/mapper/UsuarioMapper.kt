package com.fivegears.fivegears_backend.mapper

import com.fivegears.fivegears_backend.dto.UsuarioDTO
import com.fivegears.fivegears_backend.entity.Empresa
import com.fivegears.fivegears_backend.entity.NivelPermissao
import com.fivegears.fivegears_backend.entity.StatusUsuario
import com.fivegears.fivegears_backend.entity.Usuario

object UsuarioMapper {
    fun toDTO(entity: Usuario) = UsuarioDTO(
        id = entity.id,
        nome = entity.nome,
        email = entity.email,
        cpf = entity.cpf,
        telefone = entity.telefone,
        idEmpresa = entity.empresa?.id,
        idNivelPermissao = entity.nivelPermissao?.id,
        idStatusUsuario = entity.statusUsuario?.id
    )

    fun toEntity(dto: UsuarioDTO) = Usuario(
        id = dto.id,
        nome = dto.nome,
        email = dto.email,
        cpf = dto.cpf,
        telefone = dto.telefone,
        empresa = dto.idEmpresa?.let { Empresa(id = it) },
        nivelPermissao = dto.idNivelPermissao?.let { NivelPermissao(id = it) },
        statusUsuario = dto.idStatusUsuario?.let { StatusUsuario(id = it) }
    )
}