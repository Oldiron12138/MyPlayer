package com.example.myplayer.data.repository

import com.example.myplayer.api.MyPlayerService
import com.example.myplayer.data.db.InfoDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InfoRepository @Inject constructor(
    private val infoDao: InfoDao,
    private val androidTvMdsService: MyPlayerService
) {
    fun infoDetail() = infoDao.getInfos()
//    suspend fun infoDetail(): LiveData<List<InfoResponse>> {
//        val seriesDetailResponse = MutableLiveData<List<InfoResponse>>()
//        return try {
//            val response =
//                androidTvMdsService.infoDetail()
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
}