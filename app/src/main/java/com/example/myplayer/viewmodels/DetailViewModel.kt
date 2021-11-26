package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.db.MoviesEntity
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.repository.DetailRepository
import com.example.myplayer.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository
) : ViewModel() {

    val seriesDetail: LiveData<List<InfoEntity>> =
        detailRepository.seriesDetail().asLiveData()

    var loginRes: LiveData<LoginResponse>? = null
    suspend fun buy(id: Int, name: String, pwd: String, coin: Int): LiveData<LoginResponse>? {
        loginRes = detailRepository.buy(id, name, pwd, coin)
        return loginRes
    }

}