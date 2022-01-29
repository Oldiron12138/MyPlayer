package com.example.myplayer.api

import com.example.myplayer.base.*
import com.example.myplayer.data.reponse.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyPlayerService {

    /**
     */
    @GET("test/test3.json")
    suspend fun seriesDetail(): List<MoviesResponse>

    /**
     */
    @GET("test/test4.json")
    suspend fun infoDetail(): List<InfoResponse>

    @GET("test/city.json")
    suspend fun cityList(): List<CityResponse>

    @GET("test/circle.json")
    suspend fun circleList(): List<CircleResponse>

    //http://111.229.96.2:8080/QQQ/servlet/DengluServlet?name=15940850830&pwd=111111&denglu=%E7%99%BB%E5%BD%95
    @GET("QQQ/servlet/DengluServlet")
    suspend fun login(
        @Query("name") name: String,
        @Query("pwd") pwd: String,
        @Query("denglu") denglu: String
    ): LoginResponse

    @GET("QQQ/servlet/WebSocketTest")
    suspend fun register(
        @Query("name") name: String,
        @Query("pwd") pwd: String,
        @Query("accid") accid: String
    ): LoginResponse

    @GET("QQQ/servlet/Searchall")
    suspend fun userInfo(
        @Query("name") name: String?,
        @Query("pwd") pwd: String?
    ): UserResponse

    @GET("QQQ/servlet/UpdateServlet")
    suspend fun buy(
        @Query("id") id: Int,
        @Query("name") name: String,
        @Query("pwd") pwd: String,
        @Query("coin") coin: Int
    ): LoginResponse

    @GET("QQQ/servlet/UploadPhoto")
    suspend fun buyCard(
        @Query("id") id: Int,
        @Query("name") name: String,
        @Query("card1") card1: Long,
        @Query("card2") card2: Long
    ): LoginResponse


    @POST("walletobjects/v1/eventTicketClass")
    suspend fun device(): String

    @Multipart
    @POST("QQQ/servlet/UploadPhotoServlet")
    suspend fun uploadPhoto(@Part file: MultipartBody.Part): UploadResult

    @POST("QQQ/servlet/UploadJson")
    suspend fun uploadJson(@Body body: RequestBody): UploadResult

    companion object {
        fun create(): MyPlayerService {
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
                .baseUrl(BASE_URL_ANDROID_TV_APP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyPlayerService::class.java)
        }
    }
}