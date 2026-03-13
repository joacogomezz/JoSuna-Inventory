package com.joaquin.josuna_inventory.features.inventory.di

import com.joaquin.josuna_inventory.features.inventory.data.repository.InventoryRepositoryImpl
import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InventoryRepositoryModule {
    @Binds
    abstract fun bindInventoryRepository(
        impl: InventoryRepositoryImpl
    ): InventoryRepository
}

