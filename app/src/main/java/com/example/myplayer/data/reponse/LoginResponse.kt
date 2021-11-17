package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("resultData") val resultData: Boolean
)