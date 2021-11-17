package com.example.myplayer.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey @ColumnInfo(name = "title") val title: String
    //val lists: List<String>
)