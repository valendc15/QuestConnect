package com.questconnect.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val typeId: Long
)

@Entity(tableName = "content_types")
data class ContentTypes(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String
)