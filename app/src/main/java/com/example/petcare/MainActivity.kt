package com.example.petcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.petcare.data.ThemeMode
import com.example.petcare.ui.navigation.PetCareNavHost
import com.example.petcare.ui.theme.PetCareTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val app = application as PetCareApplication

            val themeMode by app.container.settingsRepository
                .themeMode
                .collectAsStateWithLifecycle(
                    initialValue = ThemeMode.SYSTEM
                )


            val darkTheme = when (themeMode) {

                ThemeMode.SYSTEM -> isSystemInDarkTheme()

                ThemeMode.LIGHT -> false

                ThemeMode.DARK -> true
            }


            PetCareTheme(

                darkTheme = darkTheme,

                dynamicColor = false

            ) {

                PetCareNavHost()
            }
        }
    }
}