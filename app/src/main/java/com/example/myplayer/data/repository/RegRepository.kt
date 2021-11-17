package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun infoDetail(name: String, pwd: String): LiveData<LoginResponse> {
        val seriesDetailResponse = MutableLiveData<LoginResponse>()
        return try {
            val response =
                androidTvMdsService.register(name, pwd)

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