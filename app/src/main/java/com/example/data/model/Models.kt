package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val userId: Int = 1,
    val name: String = "Explorer",
    val userRole: String = "STUDENT", // STUDENT, DEVELOPER, BUSINESS, SCIENTIST, PROFESSOR, GENERAL
    val preferredLanguage: String = "English",
    val learningStreak: Int = 1,
    val onboardingCompleted: Boolean = false,
    val securityEnabled: Boolean = true,
    val dailyMinutesGoal: Int = 15
)

@Entity(tableName = "chat_history")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "USER" or "AI"
    val text: String,
    val module: String, // "GENERAL", "LEARNING", "CODING", "BUSINESS", "RESEARCH", "LIFE"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_insights")
data class SavedInsight(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val category: String, // "CODE", "QUIZ", "PLAN", "RESEARCH", "DAILY"
    val timestamp: Long = System.currentTimeMillis()
)
