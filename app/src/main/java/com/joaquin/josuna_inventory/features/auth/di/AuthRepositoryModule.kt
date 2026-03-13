package com.joaquin.josuna_inventory.features.auth.di

import com.joaquin.josuna_inventory.features.auth.data.repository.AuthRepositoryImpl
import com.joaquin.josuna_inventory.features.auth.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository
}
