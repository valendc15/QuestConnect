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

    @Query("""
        INSERT INTO favorites (appId, typeId)
        SELECT :appId, ct.id 
        FROM content_types ct 
        WHERE ct.name = :typeName
    """)
    suspend fun insertFavoriteByTypeName(appId: Long, typeName: String): Long

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("""
    DELETE FROM favorites 
    WHERE appId = :appId AND typeId IN (SELECT id FROM content_types WHERE name = :typeName)
""")
    suspend fun deleteFavoriteByTypeName(appId: Long, typeName: String): Int

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("""
    SELECT f.* 
    FROM favorites f 
    INNER JOIN content_types ct 
    ON f.typeId = ct.id 
    WHERE f.appId = :appId AND ct.name = :typeName
""")
    fun getFavoritesByIdAndType(appId: Long, typeName: String): LiveData<Favorite>

    @Query("""
        SELECT f.* 
        FROM favorites f 
        INNER JOIN content_types ct 
        ON f.typeId = ct.id 
        WHERE ct.name = 'SteamGames'
    """)
    fun getAllFavoriteGames(): LiveData<List<Favorite>>
}