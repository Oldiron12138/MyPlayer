package com.example.myplayer.api

import com.example.myplayer.base.*
import com.example.myplayer.data.reponse.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
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

    //http://111.229.96.2:8080/QQQ/servlet/DengluServlet?name=15940850830&pwd=111111&denglu=%E7%99%BB%E5%BD%95
    @GET("QQQ/servlet/DengluServlet")
    suspend fun login(
        @Query("name") name: String,
        @Query("pwd") pwd: String,
        @Query("denglu") denglu: String
    ): LoginResponse

    @GET("QQQ/servlet/ZhuceServlet")
    suspend fun register(
        @Query("name") name: String,
        @Query("pwd") pwd: String
    ): LoginResponse

    @GET("QQQ/servlet/Searchall")
    suspend fun userInfo(
        @Query("name") name: String?,
        @Query("pwd") pwd: String?
    ): UserResponse

    @Headers("{\n" +
            "\t\"kind\": \"walletobjects#eventTicketClass\",\n" +
            "\t\"id\": \"string\",\n" +
            "\t\"reviewStatus\": \"string\",\n" +
            "\t\"issuerName\": \"string\",\n" +
            "\t\"eventName\": {\n" +
            "\t\t\"kind\": \"walletobjects#localizedString\",\n" +
            "\t\t\"translatedValues\": [{\n" +
            "\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\"value\": \"string\"\n" +
            "\t\t}],\n" +
            "\t\t\"defaultValue\": {\n" +
            "\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\"value\": \"string\"\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"venue\": {\n" +
            "\t\t\"kind\": \"walletobjects#eventVenue\",\n" +
            "\t\t\"name\": {\n" +
            "\t\t\t\"kind\": \"walletobjects#localizedString\",\n" +
            "\t\t\t\"translatedValues\": [{\n" +
            "\t\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\t\"value\": \"string\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"defaultValue\": {\n" +
            "\t\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\t\"value\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"address\": {\n" +
            "\t\t\t\"kind\": \"walletobjects#localizedString\",\n" +
            "\t\t\t\"translatedValues\": [{\n" +
            "\t\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\t\"value\": \"string\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"defaultValue\": {\n" +
            "\t\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\t\"value\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"dateTime\": {\n" +
            "\t\t\"kind\": \"walletobjects#eventDateTime\",\n" +
            "\t\t\"doorsOpenLabel\": \"string\",\n" +
            "\t\t\"customDoorsOpenLabel\": {\n" +
            "\t\t\t\"kind\": \"walletobjects#localizedString\",\n" +
            "\t\t\t\"translatedValues\": [{\n" +
            "\t\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\t\"value\": \"string\"\n" +
            "\t\t\t}],\n" +
            "\t\t\t\"defaultValue\": {\n" +
            "\t\t\t\t\"kind\": \"walletobjects#translatedString\",\n" +
            "\t\t\t\t\"language\": \"string\",\n" +
            "\t\t\t\t\"value\": \"string\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t\"doorsOpen\": \"string\",\n" +
            "\t\t\"start\": \"string\",\n" +
            "\t\t\"end\": \"string\"\n" +
            "\t}\n" +
            "}")
    @POST("walletobjects/v1/eventTicketClass")
    suspend fun device(): String

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