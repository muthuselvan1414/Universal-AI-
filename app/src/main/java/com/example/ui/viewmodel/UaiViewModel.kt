package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.UaiDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.SavedInsight
import com.example.data.model.UserProfile
import com.example.data.network.Content
import com.example.data.network.GeminiApiClient
import com.example.data.network.Part
import com.example.data.repository.UaiRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UaiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UaiRepository

    init {
        val database = UaiDatabase.getDatabase(application)
        repository = UaiRepository(
            userProfileDao = database.userProfileDao(),
            chatMessageDao = database.chatMessageDao(),
            savedInsightDao = database.savedInsightDao()
        )
    }

    // Selected Module State
    private val _selectedModule = MutableStateFlow("GENERAL")
    val selectedModule: StateFlow<String> = _selectedModule.asStateFlow()

    // Observe active messages reactively based on active module selection
    val activeChatMessages: StateFlow<List<ChatMessage>> = _selectedModule
        .flatMapLatest { module ->
            repository.getChatMessagesByModule(module)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // User Profile state
    val userProfile: StateFlow<UserProfile> = repository.profile
        .map { it ?: UserProfile() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    // Saved Insights
    val savedInsights: StateFlow<List<SavedInsight>> = repository.savedInsights
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // UI Input / Processing States
    private val _isAiResponding = MutableStateFlow(false)
    val isAiResponding: StateFlow<Boolean> = _isAiResponding.asStateFlow()

    private val _aiDiagnosticLog = MutableStateFlow("Brain state nominal. Direct pipeline linked.")
    val aiDiagnosticLog: StateFlow<String> = _aiDiagnosticLog.asStateFlow()

    fun selectModule(moduleName: String) {
        _selectedModule.value = moduleName
        _aiDiagnosticLog.value = "Context updated. Core routed to module $moduleName."
    }

    fun completeOnboarding(name: String, userRole: String, goal: Int, language: String) {
        viewModelScope.launch {
            val updated = UserProfile(
                userId = 1,
                name = name,
                userRole = userRole,
                preferredLanguage = language,
                onboardingCompleted = true,
                dailyMinutesGoal = goal,
                learningStreak = 1
            )
            repository.updateProfile(updated)
            _aiDiagnosticLog.value = "Neural profile created for $name as $userRole."
        }
    }

    fun resetProfile() {
        viewModelScope.launch {
            val fresh = UserProfile(onboardingCompleted = false)
            repository.updateProfile(fresh)
            repository.clearAllChats()
            _aiDiagnosticLog.value = "Neural parameters wiped. Factory reset standard."
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val currentModule = _selectedModule.value

        viewModelScope.launch {
            // 1. Insert User Message
            val userMsg = ChatMessage(sender = "USER", text = text, module = currentModule)
            repository.insertMessage(userMsg)

            _isAiResponding.value = true
            _aiDiagnosticLog.value = "Decoding intent... Aligning multi-domain brain..."

            // 2. Classify system instruction based on module + user role
            val role = userProfile.value.userRole
            val lang = userProfile.value.preferredLanguage
            val sysInstruction = getSystemPromptForModule(currentModule, role, lang)

            // 3. Collect recent context for conversation memory
            val recentHistoryList = activeChatMessages.value.takeLast(10).map { msg ->
                Content(parts = listOf(Part(text = if (msg.sender == "USER") msg.text else msg.text)))
            }

            // 4. Request from Gemini API
            val aiResponse = GeminiApiClient.generateAIResponse(
                prompt = text,
                systemPrompt = sysInstruction,
                history = recentHistoryList
            )

            // 5. Save AI response
            _isAiResponding.value = false
            val aiMsg = ChatMessage(sender = "AI", text = aiResponse, module = currentModule)
            repository.insertMessage(aiMsg)
            _aiDiagnosticLog.value = "Response synchronized. Direct connection live."
        }
    }

    fun clearChat(module: String) {
        viewModelScope.launch {
            repository.clearMessagesByModule(module)
            _aiDiagnosticLog.value = "$module buffers cleared."
        }
    }

    fun addCustomInsight(title: String, content: String, category: String) {
        viewModelScope.launch {
            val insight = SavedInsight(title = title, content = content, category = category)
            repository.insertOrUpdateInsight(insight)
            _aiDiagnosticLog.value = "Knowledge asset cataloged under $category."
        }
    }

    fun deleteInsight(id: Long) {
        viewModelScope.launch {
            repository.deleteInsightById(id)
            _aiDiagnosticLog.value = "Knowledge asset with ID $id de-allocated."
        }
    }

    private fun getSystemPromptForModule(module: String, role: String, language: String): String {
        val base = "You are UAI (Universal AI Platform), responding in $language to a user who is a $role. Maintain an incredibly helpful, professional, adaptive, futuristic tone and structure your layouts with paragraphs and neat lists."
        return when (module) {
            "LEARNING" -> "$base You are the Academic Mentor. Explain complex concepts simply, generate formatted notes, prepare small practice quizzes, or solve educational doubts."
            "CODING" -> "$base You are the elite Programming Assistant. Generate optimized robust code, explain technical compilation errors, perform troubleshooting steps, and highlight secure software architecture constraints."
            "BUSINESS" -> "$base You are the Strategic Business Intelligence Analyst. Build sales predictions, analyze customer trends, draft business model outlines, suggest automation pipelines, and model operational workflows."
            "RESEARCH" -> "$base You are the Scientific Research Fellow. Auto-summarize dense topics, process data calculations, draft simulation scenarios, and resolve advanced STEM inquiries."
            "LIFE" -> "$base You are the Personal Productivity Assistant. Plan weekly time charts, craft micro budgets, suggest wellness advice, or devise smart habit/time triggers."
            else -> "$base You are the general Multi-Domain intelligence core. Provide high-quality human-like personalized responses."
        }
    }
}
