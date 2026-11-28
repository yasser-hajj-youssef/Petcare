package com.example.petcare.ui.petdetail
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.petcare.PetCareApplication
import com.example.petcare.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface PetDetailUiState {
    data object Loading : PetDetailUiState
    data object NotFound : PetDetailUiState
    data class Content(
        val petName: String,
        val species: String,
        val tasks: List<CareTaskUi>,
        val history: List<CareLogUi>,
        val pet: PetEntity
    ) : PetDetailUiState
}
class PetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val petRepository: PetRepository,
    private val careRepository: CareRepository,
    private val breedRepository: BreedRepository
) : ViewModel() {
    private val petId: String = checkNotNull(savedStateHandle["petId"])
    val uiState: StateFlow<PetDetailUiState> = combine(
        petRepository.observePet(petId),
        careRepository.observeTasks(petId),
        careRepository.observeLogs(petId)
    ) { pet, tasks, logs ->
        if (pet == null) {
            PetDetailUiState.NotFound
        } else {
            val now = System.currentTimeMillis()
            PetDetailUiState.Content(
                petName = pet.name,
                species = pet.species,
                tasks = tasks.map { it.toUi(now) },
                history = logs.map { it.toUi() },
                pet = pet
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PetDetailUiState.Loading)
    fun addTask(title: String, frequencyDays: Int) {
        viewModelScope.launch { careRepository.addTask(petId, title, frequencyDays) }
    }
    fun complete(task: CareTaskEntity) {
        viewModelScope.launch { careRepository.completeTask(task) }
    }
    fun setReminder(task: CareTaskEntity, time: String?) {
        viewModelScope.launch { careRepository.setReminder(task.id, time) }
    }

    fun observeBreed(id: String): Flow<BreedEntity?> {

        return breedRepository.observeBreed(id)
    }

    fun saveNotes(notes: String) {

        viewModelScope.launch {

            petRepository.updateNotes(
                petId = petId,
                notes = notes
            )
        }
    }

    private val _isDeleted = MutableStateFlow(false)

    val isDeleted = _isDeleted.asStateFlow()


    private val _isDeleting = MutableStateFlow(false)

    val isDeleting = _isDeleting.asStateFlow()


    private val _deleteError = MutableStateFlow<String?>(null)

    val deleteError = _deleteError.asStateFlow()


    fun deletePet(pet: PetEntity) {

        if (_isDeleting.value) return

        _isDeleting.value = true

        _deleteError.value = null

        viewModelScope.launch {

            try {

                petRepository.deletePet(pet)

                _isDeleted.value = true

            } catch (e: kotlinx.coroutines.CancellationException) {

                throw e

            } catch (e: Exception) {

                _deleteError.value = "Unable to delete pet."

            } finally {

                _isDeleting.value = false
            }
        }
    }
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PetCareApplication
                PetDetailViewModel(
                    createSavedStateHandle(),
                    app.container.petRepository,
                    app.container.careRepository,
                    app.container.breedRepository
                )
            }
        }
    }
}