package com.example.myplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.api.TestServices
import com.example.myplayer.data.db.MoviesDao
import com.example.myplayer.data.reponse.MoviesResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val moviesDao: MoviesDao,
    private val androidTvMdsService: MyPlayerService,
    private val testServices: TestServices

) {
    fun seriesDetail() = moviesDao.getMovies()

//    suspend fun seriesDetail(): LiveData<List<MoviesResponse>> {
//        val seriesDetailResponse = MutableLiveData<List<MoviesResponse>>()
//        return try {
//            val response =
//                androidTvMdsService.seriesDetail()
//
//            // Post value to LiveData.
//            seriesDetailResponse.postValue(response)
//
//            // Return LiveData.
//            seriesDetailResponse
//        } catch (e: Exception) {
//            // Return LiveData.
//            seriesDetailResponse
//        }
//    }

    suspend fun devices(): String {
        return try {

            val response2 = testServices.device()
            // Return LiveData.
            response2
        } catch (e: Exception) {
            // Return LiveData.
            e.toString()
        }
    }
}