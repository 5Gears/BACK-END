package com.fivegears.fivegears_backend

import com.fivegears.fivegears_backend.domain.service.impl.EscoImportService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FiveGearsApplication {

	@Bean
	fun run(escoImportService: EscoImportService) = CommandLineRunner {
		escoImportService.importarPlanilha("C:\\Users\\julio\\Documents\\5Gears\\ESCO_cargos_competencias_limpo_corrigido.xlsx")

	}
}

fun main(args: Array<String>) {
	runApplication<FiveGearsApplication>(*args)
}

