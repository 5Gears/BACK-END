package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.*
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.UsuarioService
import com.fivegears.fivegears_backend.dto.UsuarioDTO
import com.fivegears.fivegears_backend.entity.Empresa
import com.fivegears.fivegears_backend.entity.UsuarioCargo
import com.fivegears.fivegears_backend.entity.UsuarioCargoId
import com.fivegears.fivegears_backend.entity.enum.SenioridadeCargo
import com.fivegears.fivegears_backend.mapper.UsuarioMapper
import com.fivegears.fivegears_backend.util.HashUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UsuarioServiceImplementacao(
    private val usuarioRepository: UsuarioRepository,
    private val loginRepository: LoginRepository,
    private val cargoRepository: CargoRepository,
    private val usuarioCargoRepository: UsuarioCargoRepository,
    private val nivelPermissaoRepository: NivelPermissaoRepository
) : UsuarioService {

    override fun listarTodos(): ResponseEntity<List<UsuarioDTO>> {
        val usuarios = usuarioRepository.findAll()
        val dtos = usuarios.map { usuario ->
            val usuarioCargo = usuarioCargoRepository.findByUsuario(usuario).firstOrNull()
            UsuarioMapper.toDTO(usuario, usuarioCargo)
        }
        return ResponseEntity.ok(dtos)
    }

    override fun buscarPorId(id: Int): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioRepository.findById(id)
            .orElseThrow { RuntimeException("Usuário não encontrado") }
        val usuarioCargo = usuarioCargoRepository.findByUsuario(usuario).firstOrNull()
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario, usuarioCargo))
    }

    override fun buscarPorEmail(email: String): ResponseEntity<UsuarioDTO> {
        val usuario = usuarioRepository.findByEmail(email)
            ?: return ResponseEntity.notFound().build()

        val usuarioCargo = usuarioCargoRepository.findByUsuario(usuario).firstOrNull()
        return ResponseEntity.ok(UsuarioMapper.toDTO(usuario, usuarioCargo))
    }

    override fun criar(dto: UsuarioDTO): ResponseEntity<UsuarioDTO> {
        // Cria usuário base
        val usuario = UsuarioMapper.toEntity(dto)
        val savedUsuario = usuarioRepository.save(usuario)

        // Cria login com senha padrão (hash do e-mail)
        val login = com.fivegears.fivegears_backend.entity.Login(
            usuario = savedUsuario,
            senha = HashUtils.sha256(savedUsuario.email)
        )
        loginRepository.save(login)

        // Associa cargo pelo nome e define senioridade
        dto.cargoNome?.let { nome ->
            val cargo = cargoRepository.findByNome(nome)
                ?: throw RuntimeException("Cargo '$nome' não encontrado")

            val usuarioCargo = UsuarioCargo(
                id = UsuarioCargoId(
                    idUsuario = savedUsuario.id,
                    idCargo = cargo.idCargo
                ),
                usuario = savedUsuario,
                cargo = cargo,
                senioridade = dto.senioridade
                    ?.let { SenioridadeCargo.valueOf(it.uppercase()) }
                    ?: SenioridadeCargo.JUNIOR,
                dataInicio = LocalDate.now()
            )
            usuarioCargoRepository.save(usuarioCargo)
        }

        val usuarioCargo = usuarioCargoRepository.findByUsuario(savedUsuario).firstOrNull()
        return ResponseEntity.ok(UsuarioMapper.toDTO(savedUsuario, usuarioCargo))
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
                cargaHoraria = dto.cargaHoraria ?: 0
                valorHora = dto.valorHora
                empresa = dto.idEmpresa?.let { Empresa(it) }

                nivelPermissao = dto.idNivel?.let { idNivel ->
                    nivelPermissaoRepository.findById(idNivel)
                        .orElseThrow { RuntimeException("Nível de permissão não encontrado: $idNivel") }
                }
            }

            val updatedUsuario = usuarioRepository.save(usuario)

            dto.cargoNome?.let { nome ->
                val cargo = cargoRepository.findByNome(nome)
                    ?: throw RuntimeException("Cargo '$nome' não encontrado")

                val usuarioCargo = UsuarioCargo(
                    id = UsuarioCargoId(
                        idUsuario = updatedUsuario.id,
                        idCargo = cargo.idCargo
                    ),
                    usuario = updatedUsuario,
                    cargo = cargo,
                    senioridade = dto.senioridade
                        ?.let { SenioridadeCargo.valueOf(it.uppercase()) }
                        ?: SenioridadeCargo.JUNIOR
                )
                usuarioCargoRepository.save(usuarioCargo)
            }

            val usuarioCargo = usuarioCargoRepository.findByUsuario(updatedUsuario).firstOrNull()
            ResponseEntity.ok(UsuarioMapper.toDTO(updatedUsuario, usuarioCargo))
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
