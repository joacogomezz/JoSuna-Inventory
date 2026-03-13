package com.joaquin.josuna_inventory.features.auth.domain.usecases

import com.joaquin.josuna_inventory.features.auth.domain.entities.User
import com.joaquin.josuna_inventory.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        return repository.loginWithGoogle(idToken)
    }
}
