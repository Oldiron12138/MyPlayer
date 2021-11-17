package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("num") val num: String,
    @field:SerializedName("coin") val coin: Int
)