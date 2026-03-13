package com.joaquin.josuna_inventory.features.auth.data.repository

import com.joaquin.josuna_inventory.features.auth.data.datasources.remote.FirebaseAuthDataSource
import com.joaquin.josuna_inventory.features.auth.domain.entities.User
import com.joaquin.josuna_inventory.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return dataSource.login(email, password)
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return dataSource.loginWithGoogle(idToken)
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        return dataSource.register(name, email, password)
    }

    override suspend fun logout() {
        dataSource.logout()
    }

    override fun getCurrentUser(): User? {
        return dataSource.getCurrentUser()
    }
}
