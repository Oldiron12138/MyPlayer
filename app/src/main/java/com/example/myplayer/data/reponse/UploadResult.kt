package com.example.myplayer.data.reponse

import com.google.gson.annotations.SerializedName

data class UploadResult(
    @field:SerializedName("resultData") val resultData: String
)