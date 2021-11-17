package com.example.myplayer.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MoviesEntity::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        @Volatile
        private var instance: MoviesDatabase? = null

        fun getInstance(context: Context): MoviesDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MoviesDatabase {
            return Room.databaseBuilder(context, MoviesDatabase::class.java, "movies").build()
        }
    }
}