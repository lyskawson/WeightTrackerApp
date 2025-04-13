package com.example.weighttracker.hiltmodules

import android.content.Context
import com.example.weighttracker.data.WeightDatabase
import com.example.weighttracker.data.daos.WeightRecordDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Provides
    fun provideWeightRecordDao(@ApplicationContext context: Context): WeightRecordDao {
        return WeightDatabase.getDatabase(context).weightRecordDao()
    }




}