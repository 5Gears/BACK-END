package com.fivegears.fivegears_backend.domain.repository

import com.fivegears.fivegears_backend.entity.Usuario
import com.fivegears.fivegears_backend.entity.UsuarioSoftSkill
import com.fivegears.fivegears_backend.entity.SoftSkill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UsuarioSoftSkillRepository : JpaRepository<UsuarioSoftSkill, Int> {

    // Busca relação exata por IDs (para atualização)
    @Query("""
        SELECT u FROM UsuarioSoftSkill u 
        WHERE u.usuario.id = :idUsuario 
        AND u.softSkill.id = :idSoftSkill
    """)
    fun findByUsuarioIdAndSoftSkillId(
        idUsuario: Int,
        idSoftSkill: Int
    ): UsuarioSoftSkill?

    // (Opcional) busca pelo objeto
    fun findByUsuarioAndSoftSkill(usuario: Usuario, softSkill: SoftSkill): UsuarioSoftSkill?

    // (Opcional) busca todas as skills de um usuário
    fun findByUsuario(usuario: Usuario): List<UsuarioSoftSkill>
}
