package com.example.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.ui.theme.HighlightCyan
import com.example.ui.viewmodel.UaiViewModel

@Composable
fun UaiApp(
    viewModel: UaiViewModel = viewModel()
) {
    val navController = rememberNavController()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val activeChatMessages by viewModel.activeChatMessages.collectAsStateWithLifecycle()
    val savedInsights by viewModel.savedInsights.collectAsStateWithLifecycle()
    val isAiResponding by viewModel.isAiResponding.collectAsStateWithLifecycle()
    val brainLog by viewModel.aiDiagnosticLog.collectAsStateWithLifecycle()

    if (!userProfile.onboardingCompleted) {
        OnboardingScreen(
            onComplete = { name, role, goal, language ->
                viewModel.completeOnboarding(name, role, goal, language)
            }
        )
    } else {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                // Only show bottom navigation if we are not in chat screen
                if (currentRoute != null && !currentRoute.startsWith("chat/")) {
                    NavigationBar(
                        containerColor = Color(0xFF0F172A),
                        contentColor = Color.White,
                        modifier = Modifier.testTag("bottom_nav_bar")
                    ) {
                        NavigationBarItem(
                            selected = currentRoute == "dashboard",
                            onClick = {
                                if (currentRoute != "dashboard") {
                                    navController.navigate("dashboard") {
                                        popUpTo("dashboard") { inclusive = true }
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.SpaceDashboard,
                                    contentDescription = "Dashboard view icon"
                                )
                            },
                            label = { Text("Dashboard") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0F172A),
                                selectedTextColor = HighlightCyan,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f),
                                indicatorColor = HighlightCyan
                            ),
                            modifier = Modifier.testTag("nav_item_dashboard")
                        )

                        NavigationBarItem(
                            selected = currentRoute == "insights",
                            onClick = {
                                if (currentRoute != "insights") {
                                    navController.navigate("insights") {
                                        popUpTo("dashboard")
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Inventory2,
                                    contentDescription = "Insights view icon"
                                )
                            },
                            label = { Text("Insights") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0F172A),
                                selectedTextColor = HighlightCyan,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f),
                                indicatorColor = HighlightCyan
                            ),
                            modifier = Modifier.testTag("nav_item_insights")
                        )

                        NavigationBarItem(
                            selected = currentRoute == "settings",
                            onClick = {
                                if (currentRoute != "settings") {
                                    navController.navigate("settings") {
                                        popUpTo("dashboard")
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Settings view icon"
                                )
                            },
                            label = { Text("Settings") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0F172A),
                                selectedTextColor = HighlightCyan,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                unselectedTextColor = Color.White.copy(alpha = 0.5f),
                                indicatorColor = HighlightCyan
                            ),
                            modifier = Modifier.testTag("nav_item_settings")
                        )
                    }
                }
            },
            containerColor = Color(0xFF0F172A),
            contentWindowInsets = WindowInsets.navigationBars
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ) {
                    composable("dashboard") {
                        DashboardScreen(
                            userProfile = userProfile,
                            brainLog = brainLog,
                            onSelectModule = { moduleId ->
                                viewModel.selectModule(moduleId)
                                navController.navigate("chat/$moduleId")
                            }
                        )
                    }

                    composable(
                        route = "chat/{module}",
                        arguments = listOf(navArgument("module") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val moduleParam = backStackEntry.arguments?.getString("module") ?: "GENERAL"
                        ChatScreen(
                            moduleName = moduleParam,
                            messages = activeChatMessages,
                            isAiResponding = isAiResponding,
                            onBack = { navController.popBackStack() },
                            onSendMessage = { text -> viewModel.sendChatMessage(text) },
                            onClearChat = { viewModel.clearChat(moduleParam) },
                            onSaveInsight = { title, content, category ->
                                viewModel.addCustomInsight(title, content, category)
                            }
                        )
                    }

                    composable("insights") {
                        InsightsScreen(
                            insights = savedInsights,
                            onDeleteInsight = { id -> viewModel.deleteInsight(id) }
                        )
                    }

                    composable("settings") {
                        SettingsScreen(
                            userProfile = userProfile,
                            onSaveProfile = { name, role, goal, language ->
                                viewModel.completeOnboarding(name, role, goal, language)
                            },
                            onResetAll = {
                                viewModel.resetProfile()
                                navController.navigate("dashboard") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
