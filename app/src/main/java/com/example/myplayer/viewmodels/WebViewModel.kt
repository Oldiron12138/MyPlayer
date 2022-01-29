package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myplayer.data.db.MoviesDao
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.MoviesResponse
import com.example.myplayer.data.reponse.UserResponse
import com.example.myplayer.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    val seriesDetail: LiveData<List<MoviesEntity>> =
        moviesRepository.seriesDetail().asLiveData()

    var loginRes: LiveData<LoginResponse>? = null
    suspend fun buy(id: Int, name: String, pwd: String, coin: Int): LiveData<LoginResponse>? {
        loginRes = moviesRepository.buy(id, name, pwd, coin)
        return loginRes
    }

    suspend fun user(num: String?, pwd: String?):  LiveData<UserResponse> {
        return moviesRepository.user(num, pwd)
    }

    suspend fun updataCard(id: Int, name: String, card1: Long, card2: Long):  LiveData<LoginResponse>? {
        return moviesRepository.buyCard(id,name,card1,card2)
    }

}