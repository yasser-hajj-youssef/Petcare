package com.example.petcare.ui.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.petcare.PetCareApplication
import com.example.petcare.data.PetEntity
import com.example.petcare.data.PetRepository
import kotlinx.coroutines.flow.*

sealed interface PetUiState {
    data object Loading : PetUiState
    data object Empty : PetUiState
    data class Content(val pets: List<PetEntity>) : PetUiState
}
class PetViewModel(repository: PetRepository) : ViewModel() {
    val uiState: StateFlow<PetUiState> = repository.pets
        .map { pets ->
            if (pets.isEmpty()) PetUiState.Empty
            else PetUiState.Content(pets)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            PetUiState.Loading
        )


    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PetCareApplication)
                PetViewModel(application.petRepository)
            }
        }
        }
    }
}