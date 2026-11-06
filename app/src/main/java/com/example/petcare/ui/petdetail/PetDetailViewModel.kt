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
        val history: List<CareLogUi>
    ) : PetDetailUiState
}
class PetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    petRepository: PetRepository,
    private val careRepository: CareRepository
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
                history = logs.map { it.toUi() }
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
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PetCareApplication
                PetDetailViewModel(
                    createSavedStateHandle(),
                    app.container.petRepository,
                    app.container.careRepository
                )
            }
        }
    }
}