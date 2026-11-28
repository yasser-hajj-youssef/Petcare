package com.example.petcare.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.petcare.PetCareApplication
import com.example.petcare.data.SettingsRepository
import com.example.petcare.data.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


data class SettingsUiState(

    val remindersEnabled: Boolean = false,

    val permissionDenied: Boolean = false,

    val themeMode: ThemeMode = ThemeMode.SYSTEM
)


class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {


    private val permissionDenied =
        MutableStateFlow(false)


    val uiState: StateFlow<SettingsUiState> = combine(

        repository.remindersEnabled,

        permissionDenied,

        repository.themeMode

    ) { enabled, denied, theme ->

        SettingsUiState(
            remindersEnabled = enabled,
            permissionDenied = denied,
            themeMode = theme
        )

    }.stateIn(

        viewModelScope,

        SharingStarted.WhileSubscribed(5_000),

        SettingsUiState()
    )


    fun setRemindersEnabled(enabled: Boolean) {

        viewModelScope.launch {

            repository.setRemindersEnabled(enabled)

            permissionDenied.value = false
        }
    }


    fun onPermissionResult(granted: Boolean) {

        viewModelScope.launch {

            repository.setRemindersEnabled(granted)

            permissionDenied.value = !granted
        }
    }


    fun setThemeMode(mode: ThemeMode) {

        viewModelScope.launch {

            repository.setThemeMode(mode)
        }
    }


    companion object {

        val Factory = viewModelFactory {

            initializer {

                val app =
                    this[APPLICATION_KEY] as PetCareApplication

                SettingsViewModel(
                    app.container.settingsRepository
                )
            }
        }
    }
}