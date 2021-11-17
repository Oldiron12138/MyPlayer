package com.example.myplayer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [InfoEntity::class], version = 1, exportSchema = false)
abstract class InfoDatabase : RoomDatabase() {

    abstract fun infoDao(): InfoDao

    companion object {
        @Volatile
        private var instance: InfoDatabase? = null

        fun getInstance(context: Context): InfoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): InfoDatabase {
            return Room.databaseBuilder(context, InfoDatabase::class.java, "info").build()
        }
    }
}