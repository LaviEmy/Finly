package com.example.finly.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.finly.R

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.settings_title), fontSize = 24.sp)

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.theme_dark), fontSize = 18.sp)
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeChange(it) }
                )
            }
        }
        LanguageSwitcher()
    }
}

@Composable
fun LanguageSwitcher() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Language / Jazyk / Язык", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { setAppLanguage("en") }) { Text("EN") }
            Button(onClick = { setAppLanguage("sk") }) { Text("SK") }
            Button(onClick = { setAppLanguage("ru") }) { Text("RU") }
        }
    }
}

fun setAppLanguage(languageTag: String) {
    val localeList = LocaleListCompat.forLanguageTags(languageTag)
    AppCompatDelegate.setApplicationLocales(localeList)
}