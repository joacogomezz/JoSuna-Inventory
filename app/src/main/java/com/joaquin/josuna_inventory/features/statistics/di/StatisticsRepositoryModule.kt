package com.joaquin.josuna_inventory.features.statistics.di

import com.joaquin.josuna_inventory.features.statistics.data.repository.StatisticsRepositoryImpl
import com.joaquin.josuna_inventory.features.statistics.domain.repositories.StatisticsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StatisticsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStatisticsRepository(
        impl: StatisticsRepositoryImpl
    ): StatisticsRepository
}

