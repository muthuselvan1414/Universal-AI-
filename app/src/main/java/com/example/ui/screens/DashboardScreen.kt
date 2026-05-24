package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.HighlightCyan
import com.example.ui.theme.PrimaryIndigo
import com.example.ui.theme.TechPurple

data class ModuleItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color,
    val statText: String
)

@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    brainLog: String,
    onSelectModule: (String) -> Unit
) {
    val modules = listOf(
        ModuleItem(
            id = "LEARNING",
            title = "Learning Block",
            description = "Concepts, study notes, dynamic practice quizzes, academic teacher support.",
            icon = Icons.AutoMirrored.Filled.MenuBook,
            iconTint = HighlightCyan,
            statText = "Core 1 - Stable"
        ),
        ModuleItem(
            id = "CODING",
            title = "Coding Block",
            description = "Code generation, error troubleshooter, secure software architectures.",
            icon = Icons.Default.Code,
            iconTint = TechPurple,
            statText = "Core 2 - Ready"
        ),
        ModuleItem(
            id = "BUSINESS",
            title = "Business Block",
            description = "Market forecasts, CRM automations, strategic financial modeling reports.",
            icon = Icons.Default.Analytics,
            iconTint = Color(0xFFF59E0B),
            statText = "Core 3 - Ready"
        ),
        ModuleItem(
            id = "RESEARCH",
            title = "Research Block",
            description = "AI science research summarizer, technical estimations, predictive workflows.",
            icon = Icons.Default.Science,
            iconTint = Color(0xFF10B981),
            statText = "Core 4 - Online"
        ),
        ModuleItem(
            id = "LIFE",
            title = "Life Companion",
            description = "Personal productivity agendas, habit tracking, health & budget planning.",
            icon = Icons.Default.SpaceDashboard,
            iconTint = Color(0xFFEC4899),
            statText = "Core 5 - Optimized"
        )
    )

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cosmic Glowing Welcoming Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1E293B).copy(alpha = 0.9f),
                                Color(0xFF0F172A).copy(alpha = 0.9f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = HighlightCyan.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NEURAL UAI PIPELINE ACTIVE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = HighlightCyan
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Welcome, ${userProfile.name}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Primary Alignment: ${userProfile.userRole} | Language: ${userProfile.preferredLanguage}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }

                    // Pulse Glowing Status Lamp
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(HighlightCyan)
                            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                    )
                }
            }

            // Streak & Productivity Goal Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Streak Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak Icon",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = "STREAK",
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                color = Color.White.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "${userProfile.learningStreak} DAY${if (userProfile.learningStreak > 1) "S" else ""}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }

                // Daily Goal Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Routine Goal Icon",
                            tint = HighlightCyan,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = "DAILY GOAL",
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                color = Color.White.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "${userProfile.dailyMinutesGoal} MINS",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Central Modular Navigation
            Text(
                text = "COG CORES (INTELLIGENCE DEEP SINK)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.5.sp
                ),
                color = HighlightCyan,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Responsive Layout - Display Grid Cards manually to fit Scroll
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                modules.forEach { mod ->
                    ModuleRowCard(
                        module = mod,
                        onSelect = { onSelectModule(mod.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Diagnostic Telemetry Console terminal box at the bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF020617), shape = RoundedCornerShape(8.dp))
                    .border(width = 1.dp, color = HighlightCyan.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Diagnostic Console",
                        tint = HighlightCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "CORE TELEMETRY TERMINAL v1.02",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        ),
                        color = HighlightCyan
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                _TerminalLogLine(text = "UAI_PLATFORM_API: Established secure socket thread.")
                _TerminalLogLine(text = "CPU_CORE_ACTIVE: $brainLog")
            }

            Spacer(modifier = Modifier.height(80.dp)) // Floating bar spacer
        }
    }
}

@Composable
fun ModuleRowCard(
    module: ModuleItem,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .testTag("module_card_${module.id.lowercase()}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(module.iconTint.copy(alpha = 0.15f))
                    .border(width = 1.dp, color = module.iconTint.copy(alpha = 0.3f), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = module.icon,
                    contentDescription = module.title,
                    tint = module.iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = module.statText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = HighlightCyan.copy(alpha = 0.7f),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun _TerminalLogLine(text: String) {
    Text(
        text = "> $text",
        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
        color = Color.White.copy(alpha = 0.7f),
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
