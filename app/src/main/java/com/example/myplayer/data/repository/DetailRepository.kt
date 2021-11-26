package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.db.InfoDao
import com.example.myplayer.data.db.MoviesDao
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.MoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepository @Inject constructor(
    private val infoDao: InfoDao,
    private val androidTvMdsService: MyPlayerService
) {
    fun seriesDetail() = infoDao.getInfos()

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