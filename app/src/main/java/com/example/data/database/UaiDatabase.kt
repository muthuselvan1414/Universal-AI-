package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.SavedInsight
import com.example.data.model.UserProfile

@Database(
    entities = [UserProfile::class, ChatMessage::class, SavedInsight::class],
    version = 1,
    exportSchema = false
)
abstract class UaiDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun savedInsightDao(): SavedInsightDao

    companion object {
        @Volatile
        private var INSTANCE: UaiDatabase? = null

        fun getDatabase(context: Context): UaiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UaiDatabase::class.java,
                    "uai_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
