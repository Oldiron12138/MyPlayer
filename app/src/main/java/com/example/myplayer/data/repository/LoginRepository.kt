package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun infoDetail(name: String, pwd: String): LiveData<LoginResponse> {
        val seriesDetailResponse = MutableLiveData<LoginResponse>()
        return try {
            val response =
                androidTvMdsService.login(name, pwd, "登录")

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