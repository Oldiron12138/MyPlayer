package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UserResponse
import com.example.myplayer.data.repository.LoginRepository
import com.example.myplayer.data.repository.ReleaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val releaseRepository: ReleaseRepository
) : ViewModel() {
    var loginRes: LiveData<LoginResponse>? = null
    suspend fun upload(name: RequestBody, pwd: MultipartBody.Part): LiveData<LoginResponse>? {
        loginRes = releaseRepository.uploadPhoto(name, pwd)
        return loginRes
    }
}