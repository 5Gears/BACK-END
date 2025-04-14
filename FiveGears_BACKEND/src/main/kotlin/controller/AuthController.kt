package school.sptech.projetotfg.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import school.sptech.projetotfg.domain.Login
import school.sptech.projetotfg.service.AuthService

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody login: Login): ResponseEntity<*> {
        return try {
            ResponseEntity.ok(authService.login(login))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/logout/{idUsuario}")
    fun logout(@PathVariable idUsuario: Long): ResponseEntity<*> {
        return try {
            authService.logout(idUsuario)
            ResponseEntity.ok("Logout realizado com sucesso")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
