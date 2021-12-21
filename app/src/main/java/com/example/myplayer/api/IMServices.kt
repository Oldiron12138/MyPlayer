package com.example.myplayer.api

import com.example.myplayer.base.*
import com.example.myplayer.base.BASE_URL_ANDROID_TV_APP
import com.example.myplayer.data.reponse.RegisterResponse
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit


interface IMServices {




    @POST("create.action")
    fun register(
        @Header("AppKey") apiKey: String?,
        @Header("Nonce") Nonce: String?,
        @Header("CurTime") CurTime: String?,
        @Header("CheckSum") CheckSum: String?,
        @Body body: RequestBody
    ): String

    companion object {
        fun create(): IMServices {

            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logger)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(IM_APP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IMServices::class.java)
        }
    }
}