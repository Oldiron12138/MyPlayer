package com.example.myplayer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var instance: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): CityDatabase {
            return Room.databaseBuilder(context, CityDatabase::class.java, "city").build()
        }
    }
}