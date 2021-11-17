package com.example.myplayer.api

import com.example.myplayer.base.*
import com.example.myplayer.data.reponse.MoviesResponse
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.*
import java.util.concurrent.TimeUnit

interface TestServices {


    @GET("test/test3.json")
    suspend fun seriesDetail(): List<MoviesResponse>

    @Headers("{\"kind\":\"walletobjects#eventTicketClass\",\"id\":\"string\",\"reviewStatus\":\"string\",\"issuerName\":\"string\",\"eventName\":{\"kind\":\"walletobjects#localizedString\",\"translatedValues\":[{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}],\"defaultValue\":{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}},\"venue\":{\"kind\":\"walletobjects#eventVenue\",\"name\":{\"kind\":\"walletobjects#localizedString\",\"translatedValues\":[{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}],\"defaultValue\":{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}},\"address\":{\"kind\":\"walletobjects#localizedString\",\"translatedValues\":[{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}],\"defaultValue\":{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}}},\"dateTime\":{\"kind\":\"walletobjects#eventDateTime\",\"doorsOpenLabel\":\"string\",\"customDoorsOpenLabel\":{\"kind\":\"walletobjects#localizedString\",\"translatedValues\":[{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}],\"defaultValue\":{\"kind\":\"walletobjects#translatedString\",\"language\":\"string\",\"value\":\"string\"}},\"doorsOpen\":\"string\",\"start\":\"string\",\"end\":\"string\"}}")
    @POST("walletobjects/v1/eventTicketClass")
    suspend fun device(): String

    companion object {
        fun create(): TestServices {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logger)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL_FIRE_TV_APP)
                .client(client)

                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TestServices::class.java)
        }
    }
}