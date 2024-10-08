package com.questconnect.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("""
    DELETE FROM favorites 
    WHERE appId = :appId and typeName = :typeName and userId = :userId
""")
    suspend fun deleteFavoriteByIdAndType(appId: Long, typeName: String, userId: Long): Int

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("""
    SELECT f.* 
    FROM favorites f
    WHERE f.appId = :appId AND f.typeName = :typeName and f.userId = :userId
""")
    fun getFavoritesByIdAndTypeAndUserId(appId: Long, typeName: String, userId: Long): LiveData<Favorite>

    @Query("""
        SELECT f.* 
        FROM favorites f 
        where f.typeName = 'SteamGames'
    """)
    fun getAllFavoriteGames(): LiveData<List<Favorite>>
}