package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myplayer.data.db.InfoEntity
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UserResponse
import com.example.myplayer.data.repository.InfoRepository
import com.example.myplayer.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    var loginRes: LiveData<LoginResponse>? = null
    suspend fun login(name: String, pwd: String): LiveData<LoginResponse>? {
        loginRes = loginRepository.infoDetail(name, pwd)
        return loginRes
    }

    suspend fun user(num: String?, pwd: String?):  LiveData<UserResponse> {
        return loginRepository.user(num, pwd)
    }

}