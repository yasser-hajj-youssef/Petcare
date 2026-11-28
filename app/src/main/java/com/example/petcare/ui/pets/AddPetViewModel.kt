package com.example.petcare.ui.pets

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.petcare.PetCareApplication
import com.example.petcare.data.BreedEntity
import com.example.petcare.data.BreedRepository
import com.example.petcare.data.PetEntity
import com.example.petcare.data.PetRepository
import com.example.petcare.work.WorkScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AddPetUiState(
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val isLoading: Boolean = false,
    val existingPet: PetEntity? = null,
    val notFound: Boolean = false,
    val error: String? = null
)

class AddPetViewModel(
    private val repository: PetRepository,
    private val breedRepository: BreedRepository,
    private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String? = savedStateHandle["petId"]

    val isEditMode = petId != null

    private val _uiState = MutableStateFlow(
        AddPetUiState(isLoading = isEditMode)
    )

    val uiState = _uiState.asStateFlow()

    init {
        if (petId != null) {
            viewModelScope.launch {

                val pet = repository.observePet(petId).first()

                _uiState.update {
                    it.copy(
                        existingPet = pet,
                        isLoading = false,
                        notFound = pet == null
                    )
                }
            }
        }
    }

    fun observeBreeds(species: String): Flow<List<BreedEntity>> {
        return breedRepository.observeBreeds(species)
    }

    fun filterBreeds(
        breeds: List<BreedEntity>,
        query: String
    ): List<BreedEntity> {

        return breeds.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    fun retryBreeds() {
        WorkScheduler.refreshBreedsNow(context)
    }

    fun save(
        name: String,
        species: String,
        birthYear: Int?,
        breedId: String?,
        breedName: String?
    ) {

        if (_uiState.value.isSaving ||
            _uiState.value.isLoading ||
            _uiState.value.notFound ||
            _uiState.value.saved
        ) return

        _uiState.update {
            it.copy(isSaving = true, error = null)
        }

        viewModelScope.launch {

            try {
                if (petId != null) {

                    val existingPet =
                        repository.observePet(petId).first()

                    if (existingPet == null) {
                        _uiState.update {
                            it.copy(
                                isSaving = false,
                                notFound = true
                            )
                        }
                        return@launch
                    }

                    repository.updatePet(
                        pet = existingPet,
                        name = name,
                        species = species,
                        birthYear = birthYear,
                        breedId = breedId,
                        breedName = breedName
                    )

                } else {

                    repository.addPet(
                        name,
                        species,
                        birthYear,
                        breedId,
                        breedName
                    )
                }

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        saved = true
                    )
                }

            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Unable to save pet. Please try again."
                    )
                }
            }
        }
    }

    companion object {

        val Factory = viewModelFactory {

            initializer {

                val app =
                    this[APPLICATION_KEY] as PetCareApplication

                AddPetViewModel(
                    app.container.petRepository,
                    app.container.breedRepository,
                    app.applicationContext,
                    createSavedStateHandle()
                )
            }
        }
    }
}