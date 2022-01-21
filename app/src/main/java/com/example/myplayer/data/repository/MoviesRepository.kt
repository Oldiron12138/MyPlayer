package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.db.MoviesDao
import com.example.myplayer.data.reponse.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val androidTvMdsService: MyPlayerService,

) {
    fun seriesDetail() = moviesDao.getMovies()

    suspend fun buy(id: Int, num: String, pwd: String, coin: Int): LiveData<LoginResponse>? {
        val seriesDetailResponse = MutableLiveData<LoginResponse>()
        return try {
            val response =
                androidTvMdsService.buy(id, num, pwd, coin)

            // Post value to LiveData.
            seriesDetailResponse.postValue(response)

            // Return LiveData.
            seriesDetailResponse
        } catch (e: Exception) {
            // Return LiveData.
            seriesDetailResponse
        }
    }
}