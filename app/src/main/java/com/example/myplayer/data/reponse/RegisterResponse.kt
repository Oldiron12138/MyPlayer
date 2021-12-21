package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("code") val code: Int,
    @field:SerializedName("desc") val desc: String
)


