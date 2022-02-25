package com.example.myplayer.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@TypeConverters(StringTypeConverter::class)
@Entity(tableName = "info")
data class InfoEntity(
    @PrimaryKey @ColumnInfo(name = "num") val num: Int,
    val title: String,
    val city: String,
    val desc: String,
    val street: String,
    val phone: String,
    val price: String,
    val url: List<String>,
    var lock: Boolean
)