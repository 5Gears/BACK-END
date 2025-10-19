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
        val competenciasBatch = mutableListOf<Triple<Int, String, String>>()

        println("🔍 Iniciando leitura da planilha ESCO: ${sheet.sheetName}")
        println("Total de linhas detectadas: ${sheet.lastRowNum}")

        var countPreview = 0

        for (rowIndex in 1..sheet.lastRowNum) {
            val row = sheet.getRow(rowIndex) ?: continue
            val rawText = getCellValue(row.getCell(0))?.trim() ?: continue
            if (rawText.isBlank()) continue

            val linha = rawText.replace("\"", "").trim()

            // Tipo de relação = último item depois da última vírgula
            val tipoRaw = linha.substringAfterLast(",", "").trim()
            val tipoRelacao = when (tipoRaw.lowercase()) {
                "essential" -> "REQUERIDA"
                "optional" -> "RECOMENDADA"
                else -> "RECOMENDADA"
            }

            // Cargo = antes da primeira vírgula
            val cargoNome = linha.substringBefore(",", "").trim()

            // Competência = entre a primeira e a última vírgula
            val competenciaNome = linha.substringAfter(",", "")
                .substringBeforeLast(",", "")
                .trim()

            if (cargoNome.isBlank() || competenciaNome.isBlank()) continue

            if (countPreview < 5) {
                println("➡️ [$rowIndex] Cargo: '$cargoNome' | Competência: '$competenciaNome' | Tipo: '$tipoRelacao'")
                countPreview++
            }

            val idCargo = cargosMap.getOrPut(cargoNome) {
                jdbc.query(
                    "SELECT id_esco_cargo FROM esco_cargo WHERE nome_cargo = ?",
                    arrayOf(cargoNome)
                ) { rs, _ -> rs.getInt("id_esco_cargo") }.firstOrNull()
                    ?: run {
                        jdbc.update("INSERT INTO esco_cargo (nome_cargo) VALUES (?)", cargoNome)
                        jdbc.query(
                            "SELECT id_esco_cargo FROM esco_cargo WHERE nome_cargo = ?",
                            arrayOf(cargoNome)
                        ) { rs, _ -> rs.getInt("id_esco_cargo") }.first()
                    }
            }

            competenciasBatch.add(Triple(idCargo, competenciaNome, tipoRelacao))
        }

        val sql = """
            INSERT INTO esco_competencia (id_esco_cargo, nome_competencia, tipo_relacao)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE tipo_relacao = VALUES(tipo_relacao)
        """.trimIndent()

        competenciasBatch.chunked(500).forEach { batch ->
            jdbc.batchUpdate(sql, batch.map { arrayOf(it.first, it.second, it.third) })
        }

        workbook.close()
        println("✅ Importação concluída com sucesso!")
        println("Cargos inseridos: ${cargosMap.size}")
        println("Relações processadas: ${competenciasBatch.size}")
    }

    private fun getCellValue(cell: Cell?): String? {
        if (cell == null) return null
        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> cell.numericCellValue.toString()
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            else -> cell.toString()
        }
    }
}
