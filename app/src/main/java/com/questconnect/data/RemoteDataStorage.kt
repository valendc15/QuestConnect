package com.questconnect.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class, ContentTypes::class,], version = 2,  exportSchema = false)
abstract class QuestConnectDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun contentTypesDao(): ContentTypesDao

    companion object {
        @Volatile
        private var INSTANCE: QuestConnectDatabase? = null

        fun getDatabase(context: Context): QuestConnectDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuestConnectDatabase::class.java,
                    "questconnect_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}