package com.example.myplayer.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "info")
data class InfoEntity(
    @PrimaryKey @ColumnInfo(name = "title") val title: String,
    val city: String,
    val desc: String,
    val street: String,
    val phone: String,
    val price: String,
    val url: String,
    var lock: Boolean
)