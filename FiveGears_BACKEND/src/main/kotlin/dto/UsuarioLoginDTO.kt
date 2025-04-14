package school.sptech.projetotfg.dto

import school.sptech.projetotfg.domain.StatusUsuario


data class UsuarioLoginDTO(
    val id: Long,
    val nome: String,
    val email: String,
    val status: StatusUsuario
) {
}
