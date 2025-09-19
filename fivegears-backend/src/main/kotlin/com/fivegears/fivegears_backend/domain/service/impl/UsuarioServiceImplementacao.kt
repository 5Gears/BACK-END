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

    override fun listarTodos(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = usuarioRepository.findAll().map { UsuarioMapper.toDTO(it) }
        return ResponseEntity.ok(usuarios)
    }

    override fun buscarPorId(id: Int): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioRepository.findById(id)
        return if (usuario.isPresent) {
            ResponseEntity.ok(UsuarioMapper.toDTO(usuario.get()))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun criar(usuarioDTO: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        val entity = UsuarioMapper.toEntity(usuarioDTO)
        val salvo = usuarioRepository.save(entity)
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(salvo))
    }

    override fun atualizar(id: Int, usuarioDTO: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioRepository.findById(id)
        return if (usuario.isPresent) {
            val atualizado = usuario.get().copy(
                nome = usuarioDTO.nome,
                email = usuarioDTO.email,
                telefone = usuarioDTO.telefone
            )
            val salvo = usuarioRepository.save(atualizado)
            ResponseEntity.ok(UsuarioMapper.toDTO(salvo))
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun deletar(id: Int): ResponseEntity<Void> {
        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
