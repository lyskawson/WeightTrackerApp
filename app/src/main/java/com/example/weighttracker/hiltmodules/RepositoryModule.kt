package com.example.weighttracker.hiltmodules

import com.example.weighttracker.data.repos.WeightRecordRepository
import com.example.weighttracker.data.repos.WeightRecordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{
    @Binds
    abstract fun bindWeightRecordRepository(impl: WeightRecordRepositoryImpl): WeightRecordRepository




}