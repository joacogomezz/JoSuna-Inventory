package com.joaquin.josuna_inventory.features.profile.di

import com.joaquin.josuna_inventory.features.profile.data.repository.ProfileRepositoryImpl
import com.joaquin.josuna_inventory.features.profile.domain.repositories.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): ProfileRepository
}

