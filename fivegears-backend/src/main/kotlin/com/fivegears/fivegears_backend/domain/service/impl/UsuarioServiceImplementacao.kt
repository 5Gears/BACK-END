package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.*
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.UsuarioService
import com.fivegears.fivegears_backend.dto.UsuarioDTO
import com.fivegears.fivegears_backend.mapper.UsuarioMapper
import com.fivegears.fivegears_backend.util.HashUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UsuarioServiceImplementacao(
    private val usuarioRepository: UsuarioRepository,
    private val loginRepository: LoginRepository,
    private val cargoRepository: CargoRepository,
    private val usuarioCargoRepository: UsuarioCargoRepository,
    private val nivelPermissaoRepository: NivelPermissaoRepository
) : UsuarioService {

    override fun listarTodos(): ResponseEntity<List<UsuarioDTO>> =
        ResponseEntity.ok(usuarioRepository.findAll().map { UsuarioMapper.toDTO(it) })

    override fun buscarPorId(id: Int): ResponseEntity<UsuarioDTO> =
        usuarioRepository.findById(id)
            .map { ResponseEntity.ok(UsuarioMapper.toDTO(it)) }
            .orElse(ResponseEntity.notFound().build())

    override fun criar(dto: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        // Cria o usuário
        val usuario = UsuarioMapper.toEntity(dto)
        val savedUsuario = usuarioRepository.save(usuario)

        // Cria o login com senha padrão (hash do email)
        val login = com.fivegears.fivegears_backend.entity.Login(
            usuario = savedUsuario,
            senha = HashUtils.sha256(savedUsuario.email)
        )
        loginRepository.save(login)

        // Associa cargo se fornecido no DTO
        dto.idCargo?.let { cargoId ->
            val cargo = cargoRepository.findById(cargoId)
                .orElseThrow { RuntimeException("Cargo não encontrado: $cargoId") }

            val usuarioCargo = com.fivegears.fivegears_backend.entity.UsuarioCargo(
                id = com.fivegears.fivegears_backend.entity.UsuarioCargoId(
                    idUsuario = savedUsuario.id,
                    idCargo = cargo.idCargo
                ),
                usuario = savedUsuario,
                cargo = cargo
            )
            usuarioCargoRepository.save(usuarioCargo)
        }

        // Retorna DTO com idCargo incluído
        return ResponseEntity.ok(UsuarioMapper.toDTO(savedUsuario, dto.idCargo))
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

                nivelPermissao = dto.idNivel?.let { idNivel ->
                    nivelPermissaoRepository.findById(idNivel)
                        .orElseThrow { RuntimeException("Nível de permissão não encontrado: $idNivel") }
                }
            }

            val updatedUsuario = usuarioRepository.save(usuario)

            dto.idCargo?.let { cargoId ->
                val cargo = cargoRepository.findById(cargoId)
                    .orElseThrow { RuntimeException("Cargo não encontrado: $cargoId") }

                val usuarioCargoId = com.fivegears.fivegears_backend.entity.UsuarioCargoId(
                    idUsuario = updatedUsuario.id,
                    idCargo = cargo.idCargo
                )

                val usuarioCargo = com.fivegears.fivegears_backend.entity.UsuarioCargo(
                    id = usuarioCargoId,
                    usuario = updatedUsuario,
                    cargo = cargo
                )
                usuarioCargoRepository.save(usuarioCargo)
            }

            ResponseEntity.ok(UsuarioMapper.toDTO(updatedUsuario, dto.idCargo))
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
