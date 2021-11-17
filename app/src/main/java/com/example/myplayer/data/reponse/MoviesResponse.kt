package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @field:SerializedName("num") val num: String,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("video") val video: String
)