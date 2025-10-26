package com.fivegears.fivegears_backend.domain.service.impl

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.io.FileInputStream

@Service
class EscoImportService(private val jdbc: JdbcTemplate) {

    fun importarPlanilha(caminhoArquivo: String) {
        val workbook = XSSFWorkbook(FileInputStream(caminhoArquivo))
        val sheet = workbook.getSheetAt(0)

        val cargosMap = mutableMapOf<String, Int>()
        val competenciasMap = mutableMapOf<String, Int>()
        val relacoes = mutableListOf<Triple<Int, Int, String>>()

        println("Lendo planilha: ${sheet.sheetName}")
        println("Total de linhas: ${sheet.lastRowNum}")

        for (i in 1..sheet.lastRowNum) {
            val linha = sheet.getRow(i) ?: continue
            val texto = getCellValue(linha.getCell(0))?.trim() ?: continue
            if (texto.isBlank()) continue

            val tipoRaw = texto.substringAfterLast(",", "").trim().lowercase()
            val tipoRelacao = if (tipoRaw == "essential") "REQUERIDA" else "RECOMENDADA"
            val cargoNome = texto.substringBefore(",", "").trim()
            val competenciaNome = texto.substringAfter(",", "").substringBeforeLast(",", "").trim()

            if (cargoNome.isBlank() || competenciaNome.isBlank()) continue

            val idCargo = cargosMap.getOrPut(cargoNome) {
                jdbc.query("SELECT id_cargo FROM cargo WHERE nome = ?", arrayOf(cargoNome)) { rs, _ -> rs.getInt(1) }
                    .firstOrNull() ?: run {
                    jdbc.update(
                        "INSERT INTO cargo (nome, fonte) VALUES (?, 'IMPORTADO')",
                        cargoNome
                    )
                    jdbc.query(
                        "SELECT id_cargo FROM cargo WHERE nome = ?",
                        arrayOf(cargoNome)
                    ) { rs, _ -> rs.getInt(1) }.first()
                }
            }

            val idCompetencia = competenciasMap.getOrPut(competenciaNome) {
                jdbc.query("SELECT id_competencia FROM competencia WHERE nome = ?", arrayOf(competenciaNome)) { rs, _ -> rs.getInt(1) }
                    .firstOrNull() ?: run {
                    jdbc.update("INSERT INTO competencia (nome) VALUES (?)", competenciaNome)
                    jdbc.query(
                        "SELECT id_competencia FROM competencia WHERE nome = ?",
                        arrayOf(competenciaNome)
                    ) { rs, _ -> rs.getInt(1) }.first()
                }
            }

            relacoes.add(Triple(idCargo, idCompetencia, tipoRelacao))
        }

        val sql = """
            INSERT INTO cargo_competencia (id_cargo, id_competencia, tipo_relacao)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE tipo_relacao = VALUES(tipo_relacao)
        """.trimIndent()

        relacoes.chunked(500).forEach { batch ->
            jdbc.batchUpdate(sql, batch.map { arrayOf(it.first, it.second, it.third) })
        }

        println("Importação concluída: ${cargosMap.size} cargos, ${competenciasMap.size} competências, ${relacoes.size} relações.")
        workbook.close()
    }

    private fun getCellValue(cell: Cell?): String? = when (cell?.cellType) {
        CellType.STRING -> cell.stringCellValue
        CellType.NUMERIC -> cell.numericCellValue.toString()
        CellType.BOOLEAN -> cell.booleanCellValue.toString()
        else -> cell?.toString()
    }
}
