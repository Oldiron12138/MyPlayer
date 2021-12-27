package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myplayer.data.db.ChatEntity
import com.example.myplayer.data.reponse.CircleResponse
import com.example.myplayer.data.repository.ChatRepository
import com.example.myplayer.data.repository.CircleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CircleViewModel @Inject constructor(
    private val circleRepository: CircleRepository
) : ViewModel() {
    var infoCache: LiveData<List<CircleResponse>>? = null
    suspend fun circleDetail(): LiveData<List<CircleResponse>>? {
        if (infoCache == null) {
            infoCache = circleRepository.circlrDetail()

        }
        return infoCache
    }

}