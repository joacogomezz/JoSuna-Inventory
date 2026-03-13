package com.joaquin.josuna_inventory.features.auth.domain.repositories

import com.joaquin.josuna_inventory.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    suspend fun loginWithGoogle(idToken: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): User?
}
