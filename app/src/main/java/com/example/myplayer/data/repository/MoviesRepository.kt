package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.db.MoviesDao
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UserResponse
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

    suspend fun buyCard(id: Int, num: String, card1: Long, card2: Long): LiveData<LoginResponse>? {
        val seriesDetailResponse = MutableLiveData<LoginResponse>()
        return try {
            val response =
                androidTvMdsService.buyCard(id, num, card1, card2)

            // Post value to LiveData.
            seriesDetailResponse.postValue(response)

            // Return LiveData.
            seriesDetailResponse
        } catch (e: Exception) {
            // Return LiveData.
            seriesDetailResponse
        }
    }

    suspend fun user(num: String?, pwd: String?): LiveData<UserResponse> {
        val seriesDetailResponse = MutableLiveData<UserResponse>()
        return try {
            val response =
                androidTvMdsService.userInfo(num, pwd)

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