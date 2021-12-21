package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class PersonChat(
    @field:SerializedName("isMe") val isMe: Boolean,
    @field:SerializedName("content") val content: String
    )