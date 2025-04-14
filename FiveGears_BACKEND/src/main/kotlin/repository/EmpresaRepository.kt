package school.sptech.projetotfg.repository

import school.sptech.projetotfg.domain.Empresa
import org.springframework.data.jpa.repository.JpaRepository

interface EmpresaRepository : JpaRepository<Empresa, Long>
