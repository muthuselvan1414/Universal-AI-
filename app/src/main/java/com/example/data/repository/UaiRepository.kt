package com.example.data.repository

import com.example.data.database.ChatMessageDao
import com.example.data.database.SavedInsightDao
import com.example.data.database.UserProfileDao
import com.example.data.model.ChatMessage
import com.example.data.model.SavedInsight
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

class UaiRepository(
    private val userProfileDao: UserProfileDao,
    private val chatMessageDao: ChatMessageDao,
    private val savedInsightDao: SavedInsightDao
) {
    val profile: Flow<UserProfile?> = userProfileDao.getProfile()

    val savedInsights: Flow<List<SavedInsight>> = savedInsightDao.getAllSavedInsights()

    fun getChatMessagesByModule(module: String): Flow<List<ChatMessage>> {
        return chatMessageDao.getMessagesByModule(module)
    }

    suspend fun getProfileSync(): UserProfile? {
        return userProfileDao.getProfileSync()
    }

    suspend fun updateProfile(userProfile: UserProfile) {
        userProfileDao.insertOrUpdateProfile(userProfile)
    }

    suspend fun insertMessage(message: ChatMessage) {
        chatMessageDao.insertMessage(message)
    }

    suspend fun clearMessagesByModule(module: String) {
        chatMessageDao.clearMessagesByModule(module)
    }

    suspend fun clearAllChats() {
        chatMessageDao.clearAllMessages()
    }

    suspend fun insertOrUpdateInsight(insight: SavedInsight) {
        savedInsightDao.insertOrUpdateInsight(insight)
    }

    suspend fun deleteInsightById(id: Long) {
        savedInsightDao.deleteInsightById(id)
    }
}
