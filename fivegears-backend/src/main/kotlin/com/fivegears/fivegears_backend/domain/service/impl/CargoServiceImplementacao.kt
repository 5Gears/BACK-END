package com.fivegears.fivegears_backend.domain.service.impl

import com.fivegears.fivegears_backend.domain.repository.CargoRepository
import com.fivegears.fivegears_backend.domain.repository.EscoCargoRepository
import com.fivegears.fivegears_backend.domain.service.impl.interfaces.CargoService
import com.fivegears.fivegears_backend.dto.CargoDTO
import com.fivegears.fivegears_backend.entity.Cargo
import com.fivegears.fivegears_backend.entity.enum.OrigemCargo
import com.fivegears.fivegears_backend.mapper.CargoMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CargoServiceImplementacao(
    private val cargoRepository: CargoRepository,
    private val escoCargoRepository: EscoCargoRepository
) : CargoService {

    override fun listarTodos(): ResponseEntity<List<CargoDTO>> {
        val cargos = cargoRepository.findAll().map { CargoMapper.toDTO(it) }
        return ResponseEntity.ok(cargos)
    }

    override fun buscarPorId(id: Int): ResponseEntity<CargoDTO> {
        val cargo = cargoRepository.findById(id).orElseThrow { RuntimeException("Cargo não encontrado.") }
        return ResponseEntity.ok(CargoMapper.toDTO(cargo))
    }

    override fun buscarPorNome(nome: String): ResponseEntity<List<CargoDTO>> {
        val cargos = cargoRepository.findByNomeContainingIgnoreCase(nome).map { CargoMapper.toDTO(it) }
        return ResponseEntity.ok(cargos)
    }

    override fun listarSugestoes(nome: String): ResponseEntity<List<CargoDTO>> {
        // Busca tanto cargos internos quanto no catálogo ESCO
        val internos = cargoRepository.findByNomeContainingIgnoreCase(nome).map { CargoMapper.toDTO(it) }
        val esco = escoCargoRepository.findByNomeCargoContainingIgnoreCase(nome)
            .map { CargoDTO(nome = it.nomeCargo, origem = OrigemCargo.ESCO.name, idEscoCargo = it.idEscoCargo, senioridade = com.fivegears.fivegears_backend.entity.enum.Senioridade.JUNIOR) }

        val combinados = (internos + esco).distinctBy { it.nome }
        return ResponseEntity.ok(combinados)
    }

    override fun criar(dto: CargoDTO): ResponseEntity<CargoDTO> {
        if (cargoRepository.existsByNome(dto.nome))
            throw IllegalArgumentException("Já existe um cargo com este nome.")

        val cargo = CargoMapper.toEntity(dto)
        val salvo = cargoRepository.save(cargo)
        return ResponseEntity.ok(CargoMapper.toDTO(salvo))
    }

    override fun atualizar(id: Int, dto: CargoDTO): ResponseEntity<CargoDTO> {
        val existente = cargoRepository.findById(id).orElseThrow { RuntimeException("Cargo não encontrado.") }

        val atualizado = existente.copy(
            nome = dto.nome,
            descricao = dto.descricao,
            senioridade = dto.senioridade,
            origem = dto.origem?.let { OrigemCargo.valueOf(it) } ?: OrigemCargo.INTERNO
        )

        val salvo = cargoRepository.save(atualizado)
        return ResponseEntity.ok(CargoMapper.toDTO(salvo))
    }

    override fun deletar(id: Int): ResponseEntity<Void> {
        if (!cargoRepository.existsById(id))
            throw RuntimeException("Cargo não encontrado para exclusão.")
        cargoRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
