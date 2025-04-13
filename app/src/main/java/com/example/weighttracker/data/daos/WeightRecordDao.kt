package com.example.weighttracker.data.daos


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.weighttracker.data.entities.WeightRecord
import kotlinx.coroutines.flow.Flow

@Dao

interface WeightRecordDao{
    @Insert
    suspend fun insert(weightRecord: WeightRecord)

    @Update
    suspend fun update(weightRecord: WeightRecord)

    @Delete
    suspend fun delete(weightRecord: WeightRecord)

    @Query("SELECT * FROM weight_records")
    fun getRecords() : Flow<List<WeightRecord>>
}