package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.HighlightCyan
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.TechPurple

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    onSaveProfile: (name: String, role: String, goal: Int, language: String) -> Unit,
    onResetAll: () -> Unit
) {
    var nameInput by remember { mutableStateOf(userProfile.name) }
    var selectedRole by remember { mutableStateOf(userProfile.userRole) }
    var preferredLanguage by remember { mutableStateOf(userProfile.preferredLanguage) }
    var dailyGoal by remember { mutableStateOf(userProfile.dailyMinutesGoal) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val roles = listOf(
        Pair("STUDENT", "Student / Learner"),
        Pair("DEVELOPER", "Developer / IT Pro"),
        Pair("BUSINESS", "Venture & Business"),
        Pair("SCIENTIST", "Scientist & Researcher"),
        Pair("PROFESSOR", "Professor & Teacher"),
        Pair("GENERAL", "General Productivity")
    )
    val languages = listOf("English", "Spanish", "French", "German", "Japanese", "Hindi")

    // Update settings states whenever userProfile changes remotely
    LaunchedEffect(userProfile) {
        nameInput = userProfile.name
        selectedRole = userProfile.userRole
        preferredLanguage = userProfile.preferredLanguage
        dailyGoal = userProfile.dailyMinutesGoal
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ALIGNMENT CENTER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.5.sp
                        ),
                        color = HighlightCyan
                    )
                    Text(
                        text = "Neural System Settings",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Tune Icon",
                    tint = HighlightCyan,
                    modifier = Modifier.size(32.dp)
                )
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))

            // Profile Fields
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "REPLICANT USERNAME",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = HighlightCyan
                )
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HighlightCyan,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedContainerColor = Color(0xFF1E293B),
                        unfocusedContainerColor = Color(0xFF1E293B),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("settings_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Role Settings
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "ACTIVE ALIGNMENT PERSPECTIVE",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = HighlightCyan
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    roles.forEach { (roleKey, roleLabel) ->
                        val isSelected = selectedRole == roleKey
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) Brush.horizontalGradient(
                                        colors = listOf(PrimaryIndigo, TechPurple)
                                    ) else Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF1E293B), Color(0xFF1E293B))
                                    )
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) HighlightCyan else Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedRole = roleKey }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = roleLabel,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                ),
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Language Configurations
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "SYSTEM SYNTAX TRANSLATION",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = HighlightCyan
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    languages.forEach { lang ->
                        val isSel = preferredLanguage == lang
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) HighlightCyan.copy(alpha = 0.15f) else Color(0xFF1E293B))
                                .border(
                                    width = 1.dp,
                                    color = if (isSel) HighlightCyan else Color.Transparent,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable { preferredLanguage = lang }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = lang,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSel) HighlightCyan else Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // Daily Minutes Range slider
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ENGAGEMENT COMMITMENT TIMER",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                        color = HighlightCyan
                    )
                    Text(
                        text = "$dailyGoal mins/day",
                        color = HighlightCyan,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace)
                    )
                }

                Slider(
                    value = dailyGoal.toFloat(),
                    onValueChange = { dailyGoal = it.toInt() },
                    valueRange = 5f..120f,
                    steps = 22,
                    colors = SliderDefaults.colors(
                        thumbColor = HighlightCyan,
                        activeTrackColor = HighlightCyan,
                        inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                    )
                )
            }

            // Save profile details
            Button(
                onClick = {
                    val finalName = if (nameInput.trim().isEmpty()) "Explorer" else nameInput.trim()
                    onSaveProfile(finalName, selectedRole, dailyGoal, preferredLanguage)
                    Toast.makeText(context, "System alignment updated successfully!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("save_settings_button"),
                colors = ButtonDefaults.buttonColors(containerColor = HighlightCyan)
            ) {
                Text("UPGRADE COGNITIVE SYSTEM PROFILE", color = Color(0xFF0F172A), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Danger Wipe Buffer Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color.Red.copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.04f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Shield Warning",
                            tint = Color.Red
                        )
                        Text(
                            text = "CRITICAL: FACTORY DESTRUCT CORE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Red
                        )
                    }

                    Text(
                        text = "Wiping the dynamic memory registers is deep and irreversible. This destroys all historical conversations, saved learning guides, and setup flags.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            onResetAll()
                            Toast.makeText(context, "All memory buffers de-allocated.", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("wipe_data_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                    ) {
                        Text("PURGE ALL REGISTERS", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
