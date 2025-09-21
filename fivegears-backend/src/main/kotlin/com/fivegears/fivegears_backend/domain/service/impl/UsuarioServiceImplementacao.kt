package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.UsuarioRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.UsuarioService
import com.fivegears.fivegears_backend.dto.UsuarioDTO
import com.fivegears.fivegears_backend.mapper.UsuarioMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UsuarioServiceImplementacao(
    private val usuarioRepository: UsuarioRepository
) : UsuarioService {

    override fun listarTodos(): ResponseEntity<List<UsuarioDTO>> =
        ResponseEntity.ok(usuarioRepository.findAll().map { UsuarioMapper.toDTO(it) })

    override fun buscarPorId(id: Int): ResponseEntity<UsuarioDTO> =
        usuarioRepository.findById(id)
            .map { ResponseEntity.ok(UsuarioMapper.toDTO(it)) }
            .orElse(ResponseEntity.notFound().build())

    override fun criar(dto: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        val entity = UsuarioMapper.toEntity(dto)
        val saved = usuarioRepository.save(entity)
        return ResponseEntity.ok(UsuarioMapper.toDTO(saved))
    }

    override fun atualizar(id: Int, dto: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        val existente = usuarioRepository.findById(id)
        return if (existente.isPresent) {
            val usuario = existente.get().apply {
                nome = dto.nome
                email = dto.email
                cpf = dto.cpf
                telefone = dto.telefone
                area = dto.area
                cargaHoraria = dto.cargaHoraria
                valorHora = dto.valorHora
                empresa = dto.idEmpresa?.let { com.fivegears.fivegears_backend.entity.Empresa(it) }
                nivelPermissao = dto.idNivel?.let { com.fivegears.fivegears_backend.entity.NivelPermissao(it) }
                status = dto.idStatus?.let { com.fivegears.fivegears_backend.entity.StatusUsuario(it) }
            }
            ResponseEntity.ok(UsuarioMapper.toDTO(usuarioRepository.save(usuario)))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    override fun deletar(id: Int): ResponseEntity<Void> {
        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
