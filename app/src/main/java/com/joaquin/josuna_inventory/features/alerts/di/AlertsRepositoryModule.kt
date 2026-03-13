package com.joaquin.josuna_inventory.features.alerts.di

import com.joaquin.josuna_inventory.features.alerts.data.repository.AlertsRepositoryImpl
import com.joaquin.josuna_inventory.features.alerts.domain.repositories.AlertsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlertsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlertsRepository(
        impl: AlertsRepositoryImpl
    ): AlertsRepository
}

