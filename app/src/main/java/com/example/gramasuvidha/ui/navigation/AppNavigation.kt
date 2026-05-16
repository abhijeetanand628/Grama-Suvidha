package com.example.gramasuvidha.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gramasuvidha.ui.screens.AddProjectScreen
import com.example.gramasuvidha.ui.screens.AdminLoginScreen
import com.example.gramasuvidha.ui.screens.ProjectDetailScreen
import com.example.gramasuvidha.ui.screens.ProjectListScreen
import com.example.gramasuvidha.viewmodel.ProjectViewModel

@Composable
fun AppNavigation(viewModel: ProjectViewModel, onLanguageToggle: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "project_list") {
        composable("project_list") {
            ProjectListScreen(
                viewModel = viewModel,
                onProjectClick = { projectId ->
                    navController.navigate("project_detail/$projectId")
                },
                onLanguageToggle = onLanguageToggle,
                onAdminLoginClick = { navController.navigate("admin_login") },
                onAddProjectClick = { navController.navigate("add_project") }
            )
        }
        composable("admin_login") {
            AdminLoginScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("add_project") {
            AddProjectScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "project_detail/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.IntType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId") ?: return@composable
            ProjectDetailScreen(
                projectId = projectId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onLanguageToggle = onLanguageToggle
            )
        }
    }
}
