package com.joaquin.josuna_inventory.features.auth.domain.usecases

import com.joaquin.josuna_inventory.features.auth.domain.entities.User
import com.joaquin.josuna_inventory.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<User> {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Todos los campos son obligatorios"))
        }
        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        return repository.register(name.trim(), email.trim(), password.trim())
    }
}
