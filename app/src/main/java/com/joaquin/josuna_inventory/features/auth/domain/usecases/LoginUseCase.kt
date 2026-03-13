package com.joaquin.josuna_inventory.features.auth.domain.usecases

import com.joaquin.josuna_inventory.features.auth.domain.entities.User
import com.joaquin.josuna_inventory.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Correo y contraseña son obligatorios"))
        }
        return repository.login(email.trim(), password.trim())
    }
}
