package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UploadResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleaseRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun uploadPhoto(file: MultipartBody.Part): LiveData<UploadResult> {
        val seriesDetailResponse = MutableLiveData<UploadResult>()
        return try {
            android.util.Log.d("zwj" ,"upload")
            val response =
                androidTvMdsService.uploadPhoto(file)

            // Post value to LiveData.
            seriesDetailResponse.postValue(response)

            // Return LiveData.
            seriesDetailResponse
        } catch (e: Exception) {
            // Return LiveData.
            seriesDetailResponse
        }
    }

    suspend fun uploadJson(body: RequestBody): LiveData<UploadResult> {
        val seriesDetailResponse = MutableLiveData<UploadResult>()
        return try {
            android.util.Log.d("zwj" ,"upload")
            val response =
                androidTvMdsService.uploadJson(body)

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