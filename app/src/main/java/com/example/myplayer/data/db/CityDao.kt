package com.example.myplayer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM city")
    fun getCitys(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCitys(citys: List<CityEntity>)

    @Query("DELETE FROM city")
    suspend fun deleteInfos()
}