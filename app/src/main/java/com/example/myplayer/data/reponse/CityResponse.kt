package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @field:SerializedName("title") val title: String,
    @field:SerializedName("lists") val city: List<String>
)