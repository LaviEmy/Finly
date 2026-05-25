package com.example.finly

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finly.data.repository.OfflineCategRepository
import com.example.finly.data.repository.OfflineDebtRepository
import com.example.finly.data.repository.OfflineGoalRepository
import com.example.finly.data.repository.OfflineSubsRepository
import com.example.finly.data.repository.OfflineTransRepository
import com.example.finly.ui.theme.FinlyTheme
import com.example.finly.viewModel.BudgetViewModel
import com.example.finly.viewModel.BudgetViewModelFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as FinanceApp
        val factory = BudgetViewModelFactory(
            OfflineTransRepository(app.database.transactionDao()),
            OfflineCategRepository(app.database.categoryDao()),
            OfflineGoalRepository(app.database.goalDao()),
            OfflineDebtRepository(app.database.debtDao()),
            OfflineSubsRepository(app.database.subscriptionDao())
        )
        val sharedPref = getPreferences(MODE_PRIVATE)
        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkTheme by remember {
                mutableStateOf(sharedPref.getBoolean("dark_theme", systemTheme))
            }
            val viewModel: BudgetViewModel = viewModel(factory = factory)
            FinlyTheme(darkTheme = isDarkTheme) {
                FinlyApp(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDark ->
                        isDarkTheme = isDark
                        sharedPref.edit {
                            putBoolean("dark_theme", isDark)
                        }
                    }
                )
            }
        }
    }
}

