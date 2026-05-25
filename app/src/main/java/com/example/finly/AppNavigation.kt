package com.example.finly

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.finly.ui.screens.*
import com.example.finly.viewModel.BudgetViewModel
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Income : Screen("income")
    object Expenses : Screen("expenses")
    object Goals : Screen("goals")
    object Debts : Screen("debts")
    object Subscriptions : Screen("subscriptions")
    object Settings : Screen("settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinlyApp(
    viewModel: BudgetViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route

    fun navigate(route: String) {
        navController.navigate(route)
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Finly", style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                DrawerItem(Icons.Default.Home, stringResource(R.string.nav_home), currentRoute == Screen.Home.route) { navigate(Screen.Home.route) }
                DrawerItem(Icons.Default.TrendingUp, stringResource(R.string.nav_income), currentRoute == Screen.Income.route) { navigate(Screen.Income.route) }
                DrawerItem(Icons.Default.TrendingDown, stringResource(R.string.nav_expenses), currentRoute == Screen.Expenses.route) { navigate(Screen.Expenses.route) }
                DrawerItem(Icons.Default.Savings, stringResource(R.string.nav_goals), currentRoute == Screen.Goals.route) { navigate(Screen.Goals.route) }
                DrawerItem(Icons.Default.AccountBalance, stringResource(R.string.nav_debts), currentRoute == Screen.Debts.route) { navigate(Screen.Debts.route) }
                DrawerItem(Icons.Default.Autorenew, stringResource(R.string.nav_subscriptions), currentRoute == Screen.Subscriptions.route) { navigate(Screen.Subscriptions.route) }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                DrawerItem(Icons.Default.Settings, stringResource(R.string.nav_settings), currentRoute == Screen.Settings.route) { navigate(Screen.Settings.route) }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(routeTitle(currentRoute)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.nav_menu))
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(navController = navController, startDestination = Screen.Home.route,
                modifier = Modifier.padding(padding)) {
                composable(Screen.Home.route) { HomeScreen(viewModel) }
                composable(Screen.Income.route) { IncomeScreen(viewModel) }
                composable(Screen.Expenses.route) { ExpensesScreen(viewModel) }
                composable(Screen.Goals.route) { GoalsScreen(viewModel) }
                composable(Screen.Debts.route) { DebtsScreen(viewModel) }
                composable(Screen.Subscriptions.route) { SubscriptionsScreen(viewModel) }
                // Передаем стейт темы в настройки
                composable(Screen.Settings.route) { SettingsScreen(isDarkTheme, onThemeChange) }
            }
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
fun routeTitle(route: String?) = when (route) {
    Screen.Home.route -> stringResource(R.string.nav_home)
    Screen.Income.route -> stringResource(R.string.nav_income)
    Screen.Expenses.route -> stringResource(R.string.nav_expenses)
    Screen.Goals.route -> stringResource(R.string.nav_goals)
    Screen.Debts.route -> stringResource(R.string.nav_debts)
    Screen.Subscriptions.route -> stringResource(R.string.nav_subscriptions)
    Screen.Settings.route -> stringResource(R.string.nav_settings)
    else -> "Finly"
}