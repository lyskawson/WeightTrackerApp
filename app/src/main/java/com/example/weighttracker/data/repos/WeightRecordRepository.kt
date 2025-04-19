package com.example.weighttracker.data.repos

import com.example.weighttracker.data.daos.WeightRecordDao
import com.example.weighttracker.data.entities.WeightRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WeightRecordRepository{
    suspend fun getRecords(): Flow<List<WeightRecord>>
    suspend fun insert(weightRecord: WeightRecord)
    suspend fun update(weightRecord: WeightRecord)
    suspend fun delete(weightRecord : WeightRecord)
}

class WeightRecordRepositoryImpl @Inject constructor(private val weightRecordDao: WeightRecordDao) : WeightRecordRepository {

    override suspend fun getRecords(): Flow<List<WeightRecord>> {
        return weightRecordDao.getRecords()
    }

    override suspend fun insert(weightRecord: WeightRecord) {
        weightRecordDao.insert(weightRecord)
    }

    override suspend fun update(weightRecord: WeightRecord) {
        weightRecordDao.update(weightRecord)
    }

    override suspend fun delete(weightRecord: WeightRecord) {
        weightRecordDao.delete(weightRecord)
    }
}

