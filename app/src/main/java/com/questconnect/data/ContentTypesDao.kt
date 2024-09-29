package com.questconnect.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContentTypesDao {
    @Insert
    suspend fun insert(contentType: ContentTypes)

    @Update
    suspend fun update(contentType: ContentTypes)

    @Delete
    suspend fun delete(contentType: ContentTypes)

    @Query("SELECT * FROM content_types")
    fun getAllContentTypes(): LiveData<List<ContentTypes>>

    @Query("SELECT * FROM content_types WHERE id = :id")
    suspend fun getContentTypeById(id: Long): ContentTypes?

    @Query("SELECT * FROM content_types WHERE name = :name")
    suspend fun getContentTypeByName(name: String): ContentTypes?
}