package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class CircleResponse(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("content") val content: String,
    @field:SerializedName("isPhoto") val isPhoto: Boolean,
    @field:SerializedName("photo") val photo: List<CirclePhotoResponse>?,
    @field:SerializedName("video") val video: String?,
    @field:SerializedName("thumbnail") val thumbnail: String?

)
data class CirclePhotoResponse(
    @field:SerializedName("photoDetail") val photoDetail: String
)