package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.InfoResponse
import com.example.myplayer.data.reponse.MoviesResponse
import com.example.myplayer.data.repository.InfoRepository
import com.example.myplayer.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val infoRepository: InfoRepository
) : ViewModel() {
    val seriesDetail: LiveData<List<InfoEntity>> =
        infoRepository.infoDetail().asLiveData()

}