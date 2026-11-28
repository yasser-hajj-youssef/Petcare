package com.example.petcare.ui.petdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare.data.CareTaskEntity
import com.example.petcare.data.BreedEntity
import com.example.petcare.ui.components.PetProfileHeader
import com.example.petcare.ui.components.BreedInfoCard
import com.example.petcare.ui.components.PetNotesCard
import kotlinx.coroutines.flow.Flow
import com.example.petcare.data.PetEntity

@Composable
fun PetDetailRoute(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,

    viewModel: PetDetailViewModel = viewModel(
        factory = PetDetailViewModel.Factory
    )
) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle()

    val isDeleted by viewModel.isDeleted
        .collectAsStateWithLifecycle()

    val isDeleting by viewModel.isDeleting
        .collectAsStateWithLifecycle()

    val deleteError by viewModel.deleteError
        .collectAsStateWithLifecycle()


    LaunchedEffect(isDeleted) {

        if (isDeleted) {

            onBack()
        }
    }


    PetDetailScreen(
        state = state,
        onBack = onBack,
        onEdit = onEdit,
        onAddTask = viewModel::addTask,
        onComplete = viewModel::complete,
        onSetReminder = viewModel::setReminder,
        observeBreed = viewModel::observeBreed,
        onSaveNotes = viewModel::saveNotes,
        onDelete = viewModel::deletePet,
        isDeleting = isDeleting,
        deleteError = deleteError
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    state: PetDetailUiState,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    onAddTask: (String, Int) -> Unit,
    onComplete: (CareTaskEntity) -> Unit,
    onSetReminder: (CareTaskEntity, String?) -> Unit,
    observeBreed: (String) -> Flow<BreedEntity?>,
    onSaveNotes: (String) -> Unit,
    onDelete: (PetEntity) -> Unit,
    isDeleting: Boolean,
    deleteError: String?
) {

    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        (state as? PetDetailUiState.Content)
                            ?.petName ?: "Pet"
                    )
                },

                navigationIcon = {

                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },

                // A7.4 - Edit pet button

                actions = {

                    if (state is PetDetailUiState.Content) {

                        TextButton(
                            enabled = !isDeleting,

                            onClick = {
                                onEdit(state.pet.id)
                            }

                        ) {
                            Text("Edit")
                        }


                        TextButton(
                            enabled = !isDeleting,

                            onClick = {
                                showDeleteDialog = true
                            }

                        ) {

                            Text(
                                text = "Delete",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },


        floatingActionButton = {

            if (state is PetDetailUiState.Content) {

                ExtendedFloatingActionButton(

                    onClick = {
                        showAddDialog = true
                    }

                ) {
                    Text("Add care task")
                }
            }
        }

    ) { padding ->

        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            when (state) {

                PetDetailUiState.Loading ->

                    CircularProgressIndicator(
                        Modifier.align(Alignment.Center)
                    )


                PetDetailUiState.NotFound ->

                    Text(
                        "This pet no longer exists.",
                        Modifier.align(Alignment.Center)
                    )


                is PetDetailUiState.Content -> LazyColumn(

                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 88.dp
                    ),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {

                    // A7.1 - Pet profile header

                    item(key = "profile_header") {

                        PetProfileHeader(
                            pet = state.pet
                        )
                    }


                    // A7.2 - Breed information

                    val breedId = state.pet.breedId

                    if (breedId != null) {

                        item(key = "breed_information") {

                            val breedFlow = remember(breedId) {
                                observeBreed(breedId)
                            }

                            val breed by breedFlow
                                .collectAsStateWithLifecycle(
                                    initialValue = null
                                )

                            BreedInfoCard(
                                breed = breed,
                                breedName = state.pet.breedName
                            )
                        }
                    }


                    // A7.3 - Pet notes

                    item(key = "pet_notes") {

                        PetNotesCard(
                            petId = state.pet.id,
                            savedNotes = state.pet.notes,
                            onSave = onSaveNotes
                        )
                    }


                    item(key = "profile_divider") {

                        HorizontalDivider()
                    }


                    // Care tasks

                    item(key = "care_tasks_title") {

                        SectionTitle("Care tasks")
                    }


                    if (state.tasks.isEmpty()) {

                        item(key = "empty_tasks") {

                            Text(
                                "No care tasks yet. Add one, e.g. Feed, every day."
                            )
                        }
                    }


                    items(
                        state.tasks,
                        key = { "task_${it.task.id}" }

                    ) { taskUi ->

                        CareTaskCard(
                            taskUi = taskUi,

                            onComplete = {
                                onComplete(taskUi.task)
                            },

                            onSetReminder = { time ->
                                onSetReminder(taskUi.task, time)
                            }
                        )
                    }


                    // History

                    item(key = "history_title") {

                        SectionTitle("History")
                    }


                    if (state.history.isEmpty()) {

                        item(key = "empty_history") {

                            Text("Nothing completed yet.")
                        }
                    }


                    items(
                        state.history,
                        key = { "log_${it.id}" }

                    ) { log ->

                        Text(
                            "${log.title} - ${log.whenLabel}",

                            style =
                                MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }


    // Add care task dialog

    if (showAddDialog) {

        AddCareTaskDialog(

            onDismiss = {
                showAddDialog = false
            },

            onConfirm = { title, days ->

                onAddTask(title, days)

                showAddDialog = false
            }
        )
    }

    if (
        showDeleteDialog &&
        state is PetDetailUiState.Content
    ) {

        AlertDialog(

            onDismissRequest = {

                if (!isDeleting) {
                    showDeleteDialog = false
                }
            },

            title = {
                Text("Delete pet?")
            },

            text = {

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "Delete ${state.pet.name} and all associated " +
                                "tasks, vaccines, supplies and history?"
                    )

                    Text("This action cannot be undone.")

                    if (deleteError != null) {

                        Text(
                            text = deleteError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },

            confirmButton = {

                TextButton(

                    enabled = !isDeleting,

                    onClick = {
                        onDelete(state.pet)
                    }

                ) {

                    Text(
                        text = if (isDeleting)
                            "Deleting..."
                        else
                            "Delete",

                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {

                TextButton(

                    enabled = !isDeleting,

                    onClick = {
                        showDeleteDialog = false
                    }

                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun SectionTitle(text: String) {

    Text(
        text = text,

        style = MaterialTheme.typography.titleSmall,

        modifier = Modifier.padding(top = 8.dp)
    )
}


val REMINDER_OPTIONS: List<String?> =
    listOf(null, "08:00", "13:00", "20:00")


@Composable
fun CareTaskCard(
    taskUi: CareTaskUi,
    onComplete: () -> Unit,
    onSetReminder: (String?) -> Unit
) {

    Card(
        Modifier.fillMaxWidth()
    ) {

        Column(
            Modifier.padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(4.dp)

        ) {

            Text(
                taskUi.task.title,
                style = MaterialTheme.typography.titleMedium
            )


            Text(
                taskUi.frequencyLabel,
                style = MaterialTheme.typography.bodySmall
            )


            Text(
                taskUi.lastDoneLabel,
                style = MaterialTheme.typography.bodySmall
            )


            Text(
                taskUi.nextDueLabel,

                style = MaterialTheme.typography.bodySmall,

                color = if (taskUi.isDue)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )


            Text(
                "Reminder",

                style = MaterialTheme.typography.labelLarge,

                modifier = Modifier.padding(top = 6.dp)
            )


            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(6.dp)
            ) {

                REMINDER_OPTIONS.forEach { option ->

                    FilterChip(

                        selected =
                            taskUi.task.reminderTime == option,

                        onClick = {
                            onSetReminder(option)
                        },

                        label = {
                            Text(option ?: "Off")
                        }
                    )
                }
            }


            Button(
                onClick = onComplete,

                modifier = Modifier.align(Alignment.End)

            ) {

                Text("Mark done")
            }
        }
    }
}


@Composable
fun AddCareTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {

    var title by rememberSaveable {
        mutableStateOf("")
    }

    var days by rememberSaveable {
        mutableIntStateOf(1)
    }


    val frequencies = listOf(
        1 to "Daily",
        7 to "Weekly",
        30 to "Monthly"
    )


    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("New care task")
        },

        text = {

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = title,

                    onValueChange = {
                        title = it
                    },

                    label = {
                        Text("Task, e.g. Feed")
                    },

                    singleLine = true
                )


                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    frequencies.forEach { (value, label) ->

                        FilterChip(

                            selected = days == value,

                            onClick = {
                                days = value
                            },

                            label = {
                                Text(label)
                            }
                        )
                    }
                }
            }
        },


        confirmButton = {

            TextButton(

                enabled = title.isNotBlank(),

                onClick = {
                    onConfirm(title, days)
                }

            ) {
                Text("Add")
            }
        },


        dismissButton = {

            TextButton(onClick = onDismiss) {

                Text("Cancel")
            }
        }
    )
}