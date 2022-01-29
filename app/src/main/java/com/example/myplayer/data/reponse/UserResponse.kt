package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("num") val num: String,
    @field:SerializedName("coin") val coin: Int,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("token") val token: String,
    @field:SerializedName("card1") val card1: Long,
    @field:SerializedName("card2") val card2: Long
)