package school.sptech.projetotfg.dto

data class UsuarioDTO(
    val nome: String,
    val email: String,
    val cpf: String,
    val telefone: String?,
    val idEmpresa: Long?
)
