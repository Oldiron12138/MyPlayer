package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.RegisterResponse
import com.example.myplayer.data.repository.LoginRepository
import com.example.myplayer.data.repository.RegRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    private val regRepository: RegRepository
) : ViewModel() {
    var loginRes: LiveData<LoginResponse>? = null
    var loginRes2: LiveData<String>? = null
    suspend fun register(name: String, pwd: String, accid: String): LiveData<LoginResponse>? {
        loginRes = regRepository.infoDetail(name, pwd,accid)
        return loginRes
    }

}