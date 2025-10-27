package com.fivegears.fivegears_backend

import com.fivegears.fivegears_backend.domain.service.impl.EscoImportService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@SpringBootApplication
class FiveGearsApplication(
	private val env: Environment
) {

	@Bean
	fun run(escoImportService: EscoImportService) = CommandLineRunner {

		val activeProfile = env.activeProfiles.firstOrNull() ?: "dev"

		when (activeProfile) {
			"dev" -> {
				val localPath = "C:\\Users\\julio\\Documents\\5Gears\\ESCO_cargos_competencias_limpo_corrigido.xlsx"
				println(" Ambiente DEV detectado. Importando planilha local: $localPath")
				escoImportService.importarPlanilha(localPath)
			}

			"prod" -> {
				val bucketUrl = env.getProperty("esco.bucket-url")
				if (bucketUrl.isNullOrBlank()) {
					println("  Nenhuma URL de bucket encontrada no application-prod.yml.")
				} else {
					println("☁️ Ambiente PROD detectado. Importando planilha do bucket: $bucketUrl")
					escoImportService.importarPlanilha(bucketUrl)
				}
			}

			else -> {
				println("Nenhum profile definido. Usando ambiente padrão (DEV).")
				val defaultPath = "C:\\Users\\julio\\Documents\\5Gears\\ESCO_cargos_competencias_limpo_corrigido.xlsx"
				escoImportService.importarPlanilha(defaultPath)
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<FiveGearsApplication>(*args)
}
