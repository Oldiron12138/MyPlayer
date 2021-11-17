package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.data.repository.CityRepository
import com.example.myplayer.data.repository.InfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val cityRepository: CityRepository
) : ViewModel() {
    var infoCache: LiveData<List<CityResponse>>? = null
    suspend fun cityAdd(): LiveData<List<CityResponse>>? {
        if (infoCache == null) {
            infoCache = cityRepository.infoDetail()

        }
        return infoCache
    }

}