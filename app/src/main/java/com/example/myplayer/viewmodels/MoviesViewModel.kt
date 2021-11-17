package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplayer.data.db.MoviesDao
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.MoviesResponse
import com.example.myplayer.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    val seriesDetail: LiveData<List<MoviesEntity>> =
        moviesRepository.seriesDetail().asLiveData()

    suspend fun devices(): String? {
        return moviesRepository.devices()
    }
}