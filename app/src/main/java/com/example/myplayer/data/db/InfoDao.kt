package com.example.myplayer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoDao {

    @Query("SELECT * FROM info")
    fun getInfos(): Flow<List<InfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInfos(channels: List<InfoEntity>)

    @Query("DELETE FROM info")
    suspend fun deleteInfos()

    @Query("UPDATE info SET lock = :mLock WHERE title = :mNum")
    suspend fun updateTour(mNum: String, mLock: Boolean): Int
}