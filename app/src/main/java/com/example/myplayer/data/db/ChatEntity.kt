package com.example.myplayer.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatEntity(
    @PrimaryKey @ColumnInfo(name = "content") val content: String,
    val isMe: Boolean
    //val lists: List<String>
)