package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.LoginRepository
import com.fivegears.fivegears_backend.domain.repository.UsuarioRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.UsuarioService
import com.fivegears.fivegears_backend.dto.UsuarioDTO
import com.fivegears.fivegears_backend.mapper.UsuarioMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UsuarioServiceImplementacao(
    private val usuarioRepository: UsuarioRepository,
    private val loginRepository: LoginRepository
) : UsuarioService {

    override fun listarTodos(): ResponseEntity<List<UsuarioDTO>> =
        ResponseEntity.ok(usuarioRepository.findAll().map { UsuarioMapper.toDTO(it) })

    override fun buscarPorId(id: Int): ResponseEntity<UsuarioDTO> =
        usuarioRepository.findById(id)
            .map { ResponseEntity.ok(UsuarioMapper.toDTO(it)) }
            .orElse(ResponseEntity.notFound().build())

    override fun criar(dto: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        // Cria o usuário
        val entity = UsuarioMapper.toEntity(dto)
        val savedUsuario = usuarioRepository.save(entity)

        // Cria o login com senha padrão
        val login = com.fivegears.fivegears_backend.entity.Login(
            usuario = savedUsuario,
            senha = savedUsuario.email
        )
        loginRepository.save(login)

        return ResponseEntity.ok(UsuarioMapper.toDTO(savedUsuario))
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
