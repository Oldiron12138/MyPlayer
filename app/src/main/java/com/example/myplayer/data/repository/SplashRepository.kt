package com.example.myplayer.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.MainActivity
import com.example.myplayer.MainApplication
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.db.*
import com.example.myplayer.data.reponse.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashRepository @Inject constructor(
    private val androidTvMdsService: MyPlayerService,

) {
//    suspend fun citys(context: Context): Boolean {
//        val seriesDetailResponse = MutableLiveData<List<CityResponse>>()
//        return try {
//            val response =
//                androidTvMdsService.cityList()
//
//            val citys: MutableList<CityEntity> = mutableListOf()
//
//            // Add most popular text to genres.
//           // genres.add(CityEntity(0, 0, "MOST POPULAR"))
//
//            // Add data to genres.
//            repeat(response.size) { i ->
//                citys.add(CityEntity(response[i].title, response[i].city))
//            }
//
//            // Init DB.
//            val database = CityDatabase.getInstance(context)
//
//            // Before insert data to DB, delete the old data.
//            database.cityDao().deleteInfos()
//
//            // Insert genres to DB.
//            database.cityDao().insertCitys(citys)
//            true
//        } catch (e: Exception) {
//            // Return LiveData.
//            false
//        }
//    }

    suspend fun infos(context: Context): Boolean {
        val seriesDetailResponse = MutableLiveData<List<InfoResponse>>()
        return try {
            val response =
                androidTvMdsService.infoDetail("test")

            val infos: MutableList<InfoEntity> = mutableListOf()
            val sharedPref =
                MainApplication.applicationContext?.getSharedPreferences("UNLOCK", Context.MODE_PRIVATE)
            var unLock: MutableSet<String>? = mutableSetOf()
            unLock = sharedPref?.getStringSet("UNLOCK_INFO",null)

            var unLock1: MutableSet<String>? = mutableSetOf()
            if (unLock1 != null) {
                unLock1.clear()
            }
            if (unLock != null) {
                unLock!!.forEach {
                    unLock1!!.add(it)
                }
            }

            var Lock: Boolean
            if (unLock == null) {
                repeat(response.size) { i ->
                    infos.add(InfoEntity(response[i].num,response[i].title, response[i].city, response[i].desc, response[i].street, response[i].phone, response[i].price,response[i].url, true))
                    //infos.add(InfoEntity(response[i].title, response[i].city, response[i].desc, response[i].street, response[i].phone, response[i].price,response[i].url, true))
                }
            } else {
                repeat(response.size) { i ->
                    //android.util.Log.d("zwj" ,"sharepComp10 $i ${unLock.add(i.toString())}")
                    var flag: Boolean = unLock.add(i.toString())
                    if (flag) {
                        unLock.remove(i.toString())
                    }
                    infos.add(InfoEntity(response[i].num,response[i].title, response[i].city, response[i].desc, response[i].street, response[i].phone, response[i].price,response[i].url, flag))
                    //infos.add(InfoEntity(response[i].title, response[i].city, response[i].desc, response[i].street, response[i].phone, response[i].price,response[i].url, true))
                }
            }
            val database = InfoDatabase.getInstance(context)

            database.infoDao().deleteInfos()

            database.infoDao().insertInfos(infos)
            true
        } catch (e: Exception) {
            // Return LiveData.
            false
        }
    }

    suspend fun movies(context: Context): Boolean {
        val seriesDetailResponse = MutableLiveData<List<MoviesResponse>>()
        return try {
            val response =
                androidTvMdsService.seriesDetail()

            val infos: MutableList<MoviesEntity> = mutableListOf()

            repeat(response.size) { i ->
                infos.add(MoviesEntity(response[i].num, response[i].url, response[i].video, true))
            }

            // Init DB.
            val database = MoviesDatabase.getInstance(context)

            database.moviesDao().deleteMovies()

            database.moviesDao().insertMovies(infos)
            true
        } catch (e: Exception) {
            false
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