package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.IMServices
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.util.CheckSumBuilder
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class RegRepository @Inject constructor(
    private val imService: IMServices,
    private val androidTvMdsService: MyPlayerService
) {

    suspend fun infoDetail(name: String, pwd: String, accid:String): LiveData<LoginResponse> {
        val seriesDetailResponse = MutableLiveData<LoginResponse>()
        return try {
            val response =
                androidTvMdsService.register(name, pwd,accid)

            // Post value to LiveData.
            seriesDetailResponse.postValue(response)

            // Return LiveData.
            seriesDetailResponse
        } catch (e: Exception) {
            // Return LiveData.
            seriesDetailResponse
        }
    }

    suspend fun registerIM(body: RequestBody): LiveData<String> {
        val seriesDetailResponse = MutableLiveData<String>()
        var appKey = "37e03c9b288eff731388d655b2284f0e"
        var appSecret = "87a5c9e4242f"
        var nonce = "115"
        var curTime: String = java.lang.String.valueOf(Date().getTime() / 1000L)
        var checkSum: String = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime)
        val body1: String = "zwj"
        android.util.Log.d("zwj " ,"checkSum $curTime")
        android.util.Log.d("zwj " ,"checkSum $checkSum")
        return try {
            android.util.Log.d("zwj", "5555")
            val response =
                imService.register(appKey, nonce, curTime, checkSum,body)
            android.util.Log.d("zwj", "5555 $response")

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