package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.CircleResponse
import com.example.myplayer.data.reponse.CityResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CircleRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun circlrDetail(): LiveData<List<CircleResponse>> {
        val seriesDetailResponse = MutableLiveData<List<CircleResponse>>()
        return try {
            val response =
                androidTvMdsService.circleList()

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