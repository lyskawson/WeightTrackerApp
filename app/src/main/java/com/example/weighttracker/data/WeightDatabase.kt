package com.example.weighttracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weighttracker.data.daos.WeightRecordDao
import com.example.weighttracker.data.entities.WeightRecord

@Database(entities = [WeightRecord::class], version = 1)
abstract class WeightDatabase : RoomDatabase(){
    abstract fun weightRecordDao(): WeightRecordDao

    companion object {
        @Volatile // indicates that the instance variable can be accessed by multiple threads
        private var instance: WeightDatabase? = null

        fun getDatabase(context: Context): WeightDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context.applicationContext,
                    klass= WeightDatabase::class.java,
                    name = "weight_database"
                ).build().also{
                    instance = it
                }
            }
        }
    }


}