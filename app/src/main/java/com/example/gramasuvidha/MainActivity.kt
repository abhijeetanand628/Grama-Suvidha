package com.example.gramasuvidha

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gramasuvidha.ui.navigation.AppNavigation
import com.example.gramasuvidha.ui.theme.GramaSuvidhaTheme
import com.example.gramasuvidha.viewmodel.ProjectViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: ProjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GramaSuvidhaTheme {
                AppNavigation(
                    viewModel = viewModel,
                    onLanguageToggle = {
                        toggleLanguage()
                    }
                )
            }
        }
    }

    private fun toggleLanguage() {
        val currentLang = resources.configuration.locales.get(0).language
        val newLang = if (currentLang == "kn") "en" else "kn"
        setAppLocale(newLang)
    }

    private fun setAppLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        recreate()
    }
}
