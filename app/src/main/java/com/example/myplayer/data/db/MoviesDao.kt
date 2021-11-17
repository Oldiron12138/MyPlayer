package com.example.myplayer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getMovies(): Flow<List<MoviesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(citys: List<MoviesEntity>)

    @Query("DELETE FROM movies")
    suspend fun deleteMovies()
}