package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class InfoResponse(
    @field:SerializedName("num") val num: Int,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("city") val city: String,
    @field:SerializedName("desc") val desc: String,
    @field:SerializedName("street") val street: String,
    @field:SerializedName("phone") val phone: String,
    @field:SerializedName("price") val price: String,
    @field:SerializedName("url") val url: List<String>
)