package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.reponse.InfoResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun infoDetail(): LiveData<List<CityResponse>> {
        val seriesDetailResponse = MutableLiveData<List<CityResponse>>()
        return try {
            val response =
                androidTvMdsService.cityList()

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