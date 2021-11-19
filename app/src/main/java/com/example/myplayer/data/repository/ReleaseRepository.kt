package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleaseRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun uploadPhoto(name: RequestBody, pwd: MultipartBody.Part): LiveData<LoginResponse> {
        val seriesDetailResponse = MutableLiveData<LoginResponse>()
        return try {
            android.util.Log.d("zwj" ,"upload")
            val response =
                androidTvMdsService.uploadPhoto(name, pwd)

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