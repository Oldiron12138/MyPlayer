package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UserResponse
import com.example.myplayer.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BuyContentViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    var loginRes: LiveData<LoginResponse>? = null
    suspend fun buy(id: Int, name: String, pwd: String, coin: Int): LiveData<LoginResponse>? {
        loginRes = loginRepository.buy(id, name, pwd, coin)
        return loginRes
    }

}