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
sealed interface PetsUiState {
    data object Loading : PetsUiState
    data object Empty : PetsUiState
    data class Content(val pets: List<PetEntity>) : PetsUiState
}
class PetsViewModel(repository: PetRepository) : ViewModel() {
    val uiState: StateFlow<PetsUiState> = repository.pets
        .map { pets ->
            if (pets.isEmpty()) PetsUiState.Empty else PetsUiState.Content(pets)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            PetsUiState.Loading
        )
    companion object {
        val Factory = viewModelFactory {initializer {
            val app = this[APPLICATION_KEY] as PetCareApplication
            PetsViewModel(app.container.petRepository)
        }
        }
    }
}