package com.example.petcare.ui.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.petcare.PetCareApplication
import com.example.petcare.data.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
data class AddPetUiState(
    val isSaving: Boolean = false,
    val saved: Boolean = false
)
class AddPetViewModel(private val repository: PetRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AddPetUiState())
    val uiState = _uiState.asStateFlow()
    fun save(name: String, species: String, birthYear: Int?) {
        if (_uiState.value.isSaving) return // ignore double taps
        _uiState.value = AddPetUiState(isSaving = true)
        viewModelScope.launch {
            repository.addPet(name, species, birthYear)
            _uiState.value = AddPetUiState(saved = true)
        }
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {val app = this[APPLICATION_KEY] as PetCareApplication
                AddPetViewModel(app.container.petRepository)
            }
        }
    }
}