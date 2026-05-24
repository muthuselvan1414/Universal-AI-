package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.theme.HighlightCyan
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.TechPurple

@Composable
fun ChatScreen(
    moduleName: String,
    messages: List<ChatMessage>,
    isAiResponding: Boolean,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onClearChat: () -> Unit,
    onSaveInsight: (title: String, content: String, category: String) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    // Auto scroll down when messages arrive
    LaunchedEffect(messages.size, isAiResponding) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E293B).copy(alpha = 0.4f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("chat_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Back",
                        tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(getModuleColor(moduleName).copy(alpha = 0.2f))
                        .border(width = 1.dp, color = getModuleColor(moduleName), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getModuleIcon(moduleName),
                        contentDescription = "Module Symbol",
                        tint = getModuleColor(moduleName),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = getModuleTitle(moduleName),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = "Gemini multi-context alignment active",
                        style = MaterialTheme.typography.bodySmall,
                        color = HighlightCyan.copy(alpha = 0.8f)
                    )
                }

                // Header actions
                IconButton(
                    onClick = {
                        onClearChat()
                        Toast.makeText(context, "Chat conversation cleared", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.testTag("clear_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear Chat History",
                        tint = Color.White.copy(alpha = 0.6f)
                    )
                }
            }

            // Central Chat Log
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (messages.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = getModuleIcon(moduleName),
                                contentDescription = "Concept Logo",
                                tint = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = "COGNITIVE REGISTER EMPTY",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                ),
                                color = HighlightCyan.copy(alpha = 0.4f)
                            )
                            Text(
                                text = getModuleDescription(moduleName),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.4f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize()
                            .testTag("chat_messages_list"),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(messages, key = { it.id }) { message ->
                            ChatBubble(
                                message = message,
                                moduleName = moduleName,
                                onSaveInsight = onSaveInsight
                            )
                        }

                        if (isAiResponding) {
                            item {
                                ThinkingIndicator()
                            }
                        }
                    }
                }
            }

            // Footer Input Drawer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F172A))
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Query module core context...", color = Color.Gray, fontSize = 14.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedBorderColor = HighlightCyan,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f)
                    ),
                    trailingIcon = {
                        val canSend = textInput.trim().isNotEmpty() && !isAiResponding
                        IconButton(
                            onClick = {
                                if (canSend) {
                                    onSendMessage(textInput.trim())
                                    textInput = ""
                                    keyboardController?.hide()
                                }
                            },
                            enabled = canSend,
                            modifier = Modifier.testTag("send_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send Message",
                                tint = if (canSend) HighlightCyan else Color.White.copy(alpha = 0.2f)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = {
                        if (textInput.trim().isNotEmpty() && !isAiResponding) {
                            onSendMessage(textInput.trim())
                            textInput = ""
                            keyboardController?.hide()
                        }
                    })
                )
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    moduleName: String,
    onSaveInsight: (title: String, content: String, category: String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val isUser = message.sender == "USER"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(if (isUser) "user_message" else "ai_message"),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Author label
        Text(
            text = if (isUser) "REPLICANT" else "UAI SYSTEM ENGINE",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
            color = if (isUser) HighlightCyan.copy(alpha = 0.7f) else getModuleColor(moduleName).copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 4.dp, start = 8.dp, end = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            // Main Bubble Container
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (isUser) Color(0xFF1E293B) else Color(0xFF1E1B4B)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isUser) Color.White.copy(alpha = 0.1f) else getModuleColor(moduleName).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .padding(14.dp)
                    .widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            // Quick actions side drawer for AI responses
            if (!isUser) {
                Row(
                    modifier = Modifier.padding(start = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(message.text))
                            Toast.makeText(context, "Copied response to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp).testTag("copy_msg_button_${message.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy text",
                            tint = Color.White.copy(alpha = 0.4f),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val derivedTitle = if (message.text.length > 25) {
                                message.text.take(25) + "..."
                            } else {
                                message.text
                            }
                            onSaveInsight(derivedTitle, message.text, moduleName)
                            Toast.makeText(context, "Asset saved to Neural Vault!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp).testTag("save_msg_button_${message.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Save to vault",
                            tint = HighlightCyan.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThinkingIndicator() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "UAI NEURAL LOGIC",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
            color = HighlightCyan.copy(alpha = 0.5f),
            modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp))
                .background(Color(0xFF1E1B4B).copy(alpha = 0.3f))
                .border(width = 1.dp, color = HighlightCyan.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = HighlightCyan
                )
                Text(
                    text = "UAI core is thinking and aligning modules...",
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// Helpers
private fun getModuleTitle(module: String): String {
    return when (module) {
        "LEARNING" -> "Learning Assistant Block"
        "CODING" -> "Cyber Coding & IT Block"
        "BUSINESS" -> "Strategic Venture Analytics"
        "RESEARCH" -> "Lab Research & Summary"
        "LIFE" -> "Smart Life Companion"
        else -> "General Multi-Brain"
    }
}

private fun getModuleIcon(module: String): ImageVector {
    return when (module) {
        "LEARNING" -> Icons.AutoMirrored.Filled.MenuBook
        "CODING" -> Icons.Default.Code
        "BUSINESS" -> Icons.Default.Analytics
        "RESEARCH" -> Icons.Default.Science
        "LIFE" -> Icons.Default.SpaceDashboard
        else -> Icons.Default.AllInclusive
    }
}

private fun getModuleDescription(module: String): String {
    return when (module) {
        "LEARNING" -> "Explain complex concepts simply, draft formatted notes, or generate quizzes on any academic curriculum."
        "CODING" -> "Resolve programming bugs step-by-step, draft optimal algorithms, and examine secure architecture constraints."
        "BUSINESS" -> "Model SaaS forecasts, analyze customer feedback pipelines, and draft operations reports."
        "RESEARCH" -> "Auto-summarize long papers, perform statistical calculations, or formulate predictive solar/grid simulations."
        "LIFE" -> "Plan balanced weekly itineraries, analyze smart habits, and manage daily microscale task buffers."
        else -> "Ask anything! Broad-spectrum Gemini AI orchestration covering multiple learning, programming, and automation contexts."
    }
}

private fun getModuleColor(module: String): Color {
    return when (module) {
        "LEARNING" -> HighlightCyan
        "CODING" -> TechPurple
        "BUSINESS" -> Color(0xFFF59E0B)
        "RESEARCH" -> Color(0xFF10B981)
        "LIFE" -> Color(0xFFEC4899)
        else -> PrimaryIndigo
    }
}
