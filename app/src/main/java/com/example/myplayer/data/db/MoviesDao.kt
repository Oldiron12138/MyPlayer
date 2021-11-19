package com.example.myplayer.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies")
    fun getMovies(): Flow<List<MoviesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(citys: List<MoviesEntity>)

    @Query("DELETE FROM movies")
    suspend fun deleteMovies()

    @Update
    suspend fun update(moviesEntity: MoviesEntity)


    @Query("UPDATE movies SET lock = :mLock WHERE num = :mNum")
    suspend fun updateTour(mNum: String, mLock: Boolean): Int
}