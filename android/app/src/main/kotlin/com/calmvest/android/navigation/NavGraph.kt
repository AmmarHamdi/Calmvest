package com.calmvest.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.calmvest.feature.dashboard.DashboardScreen
import com.calmvest.feature.goals.GoalDetailScreen
import com.calmvest.feature.goals.GoalsScreen
import com.calmvest.feature.onboarding.GoalSetupScreen
import com.calmvest.feature.onboarding.InvestmentModeScreen
import com.calmvest.feature.onboarding.OnboardingScreen
import com.calmvest.feature.portfolio.PortfolioScreen
import com.calmvest.feature.settings.RoundUpRuleScreen
import com.calmvest.feature.settings.SettingsScreen
import com.calmvest.feature.transactions.TransactionsScreen

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(Routes.DASHBOARD, "Dashboard", Icons.Filled.Dashboard),
    BottomNavItem(Routes.GOALS, "Goals", Icons.Filled.Flag),
    BottomNavItem(Routes.TRANSACTIONS, "Activity", Icons.Filled.Receipt),
    BottomNavItem(Routes.PORTFOLIO, "Portfolio", Icons.Filled.AccountBalanceWallet),
    BottomNavItem(Routes.SETTINGS, "Settings", Icons.Filled.Settings),
)

private val bottomNavRoutes = bottomNavItems.map { it.route }.toSet()

@Composable
fun CalmvestNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING,
        modifier = modifier
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onGetStarted = { navController.navigate(Routes.GOAL_SETUP) }
            )
        }
        composable(Routes.GOAL_SETUP) {
            GoalSetupScreen(
                onContinue = { navController.navigate(Routes.INVESTMENT_MODE) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.INVESTMENT_MODE) {
            InvestmentModeScreen(
                onStartSaving = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToGoals = { navController.navigate(Routes.GOALS) },
                onNavigateToTransactions = { navController.navigate(Routes.TRANSACTIONS) }
            )
        }
        composable(Routes.GOALS) {
            GoalsScreen(
                onGoalClick = { goalId -> navController.navigate(Routes.goalDetail(goalId)) }
            )
        }
        composable(
            route = Routes.GOAL_DETAIL,
            arguments = listOf(navArgument("goalId") { type = NavType.StringType })
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getString("goalId") ?: return@composable
            GoalDetailScreen(
                goalId = goalId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.TRANSACTIONS) {
            TransactionsScreen()
        }
        composable(Routes.PORTFOLIO) {
            PortfolioScreen()
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToRoundUpRule = { navController.navigate(Routes.ROUND_UP_RULE) }
            )
        }
        composable(Routes.ROUND_UP_RULE) {
            RoundUpRuleScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
