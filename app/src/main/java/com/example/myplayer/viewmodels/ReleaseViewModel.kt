package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myplayer.data.reponse.LoginResponse
import com.example.myplayer.data.reponse.UploadResult
import com.example.myplayer.data.reponse.UserResponse
import com.example.myplayer.data.repository.LoginRepository
import com.example.myplayer.data.repository.ReleaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ReleaseViewModel @Inject constructor(
    private val releaseRepository: ReleaseRepository
) : ViewModel() {
    var loginRes: LiveData<UploadResult>? = null
    suspend fun upload(file: List<MultipartBody.Part>): LiveData<UploadResult>? {
        loginRes = releaseRepository.uploadPhoto(file)
        return loginRes
    }

    suspend fun uploadJson(body: RequestBody): LiveData<UploadResult>? {
        loginRes = releaseRepository.uploadJson(body)
        return loginRes
    }
}