package com.questconnect.data
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import androidx.room.Room

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideQuestConnectDatabase(@ApplicationContext context: Context): QuestConnectDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            QuestConnectDatabase::class.java,
            "quest_connect_database"
        ).build()
    }
}