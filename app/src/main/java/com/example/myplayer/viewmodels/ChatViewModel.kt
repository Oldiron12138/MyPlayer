package com.example.myplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myplayer.data.db.ChatEntity
import com.example.myplayer.data.reponse.CityResponse
import com.example.myplayer.data.repository.ChatRepository
import com.example.myplayer.data.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    var infoCache: LiveData<List<ChatEntity>>? = null
    suspend fun chatHistory(): LiveData<List<ChatEntity>>? {
        if (infoCache == null) {
            infoCache = chatRepository.chatHistory().asLiveData()

        }
        return infoCache
    }

}