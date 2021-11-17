package com.example.myplayer.data

import java.io.Serializable

data class MoivesReceiveData(
    val assetId: String,
    val fromWhere: Int          // 0: Series, 1: Brand_Page
) : Serializable