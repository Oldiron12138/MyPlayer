package com.example.myplayer.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MoviesEntity(
    @PrimaryKey @ColumnInfo(name = "num") var num: String,
    var url: String,
    var video: String,
    var lock: Boolean
)