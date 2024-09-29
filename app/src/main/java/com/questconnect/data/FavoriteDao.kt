package com.questconnect.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDao {
    @Insert
    suspend fun insert(favorite: Favorite)

    @Update
    suspend fun update(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("""
        SELECT f.* 
        FROM favorites f 
        INNER JOIN content_types ct 
        ON f.typeId = ct.id 
        WHERE ct.name = :type
    """)
    fun getFavoritesByType(type: String): LiveData<List<Favorite>>

    @Query("""
        SELECT f.* 
        FROM favorites f 
        INNER JOIN content_types ct 
        ON f.typeId = ct.id 
        WHERE ct.name = 'SteamGames'
    """)
    fun getAllFavoriteGames(): LiveData<List<Favorite>>
}