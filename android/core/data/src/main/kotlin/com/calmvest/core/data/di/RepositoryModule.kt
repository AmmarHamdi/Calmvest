package com.calmvest.core.data.di

import com.calmvest.core.data.fake.*
import com.calmvest.core.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// TODO: swap Fake* implementations with the real *RepositoryImpl classes in repository/ package
//       once a live backend is available.
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: FakeUserRepository): UserRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(impl: FakeGoalRepository): GoalRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: FakeTransactionRepository): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindPortfolioRepository(impl: FakePortfolioRepository): PortfolioRepository

    @Binds
    @Singleton
    abstract fun bindRoundUpRepository(impl: FakeRoundUpRepository): RoundUpRepository
}
