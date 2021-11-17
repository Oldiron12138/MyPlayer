package com.example.myplayer.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UserResponse
import com.example.myplayer.data.repository.CityRepository
import com.example.myplayer.data.repository.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository
) : ViewModel() {

//    suspend fun citys(context: Context): Boolean {
//        return splashRepository.citys(context)
//    }

    suspend fun infos(context: Context): Boolean {
        return splashRepository.infos(context)
    }

    suspend fun movies(context: Context): Boolean {
        return splashRepository.movies(context)
    }

    suspend fun user(num: String?, pwd: String?):  LiveData<UserResponse> {
        return splashRepository.user(num, pwd)
    }

}