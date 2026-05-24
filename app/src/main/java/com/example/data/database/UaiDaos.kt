package com.example.data.database

import androidx.room.*
import com.example.data.model.ChatMessage
import com.example.data.model.SavedInsight
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE userId = 1")
    fun getProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE userId = 1")
    suspend fun getProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_history WHERE module = :module ORDER BY timestamp ASC")
    fun getMessagesByModule(module: String): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_history WHERE module = :module")
    suspend fun clearMessagesByModule(module: String)

    @Query("DELETE FROM chat_history")
    suspend fun clearAllMessages()
}

@Dao
interface SavedInsightDao {
    @Query("SELECT * FROM saved_insights ORDER BY timestamp DESC")
    fun getAllSavedInsights(): Flow<List<SavedInsight>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateInsight(insight: SavedInsight)

    @Query("DELETE FROM saved_insights WHERE id = :id")
    suspend fun deleteInsightById(id: Long)
}
