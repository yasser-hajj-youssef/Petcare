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
@Composable
fun PetDetailRoute(
    onBack: () -> Unit,
    viewModel: PetDetailViewModel = viewModel(factory = PetDetailViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PetDetailScreen(
        state = state,
        onBack = onBack,
        onAddTask = viewModel::addTask,
        onComplete = viewModel::complete,
        onSetReminder = viewModel::setReminder
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    state: PetDetailUiState,
    onBack: () -> Unit,
    onAddTask: (String, Int) -> Unit,
    onComplete: (CareTaskEntity) -> Unit,
    onSetReminder: (CareTaskEntity, String?) -> Unit
) {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text((state as? PetDetailUiState.Content)?.petName ?: "Pet") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        },
        floatingActionButton = {
            if (state is PetDetailUiState.Content) {
                ExtendedFloatingActionButton(onClick = { showAddDialog = true }) {
                    Text("Add care task")
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (state) {
                PetDetailUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                PetDetailUiState.NotFound ->
                    Text("This pet no longer exists.", Modifier.align(Alignment.Center))
                is PetDetailUiState.Content -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { SectionTitle("Care tasks") }
                    if (state.tasks.isEmpty()) {
                        item { Text("No care tasks yet. Add one, e.g. Feed, every day.") }
                    }
                    items(state.tasks, key = { it.task.id }) { taskUi ->
                        CareTaskCard(
                            taskUi = taskUi,
                            onComplete = { onComplete(taskUi.task) },
                            onSetReminder = { time -> onSetReminder(taskUi.task, time) }
                        )
                    }
                    item { SectionTitle("History") }
                    if (state.history.isEmpty()) {
                        item { Text("Nothing completed yet.") }
                    }
                    items(state.history, key = { it.id }) { log ->
                        Text("${log.title} - ${log.whenLabel}",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        AddCareTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, days ->
                onAddTask(title, days)
                showAddDialog = false
            }
        )
    }
}
@Composable
fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(top = 8.dp))
}
val REMINDER_OPTIONS: List<String?> = listOf(null, "08:00", "13:00", "20:00")
@Composable
fun CareTaskCard(
    taskUi: CareTaskUi,
    onComplete: () -> Unit,
    onSetReminder: (String?) -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(taskUi.task.title, style = MaterialTheme.typography.titleMedium)
            Text(taskUi.frequencyLabel, style = MaterialTheme.typography.bodySmall)
            Text(taskUi.lastDoneLabel, style = MaterialTheme.typography.bodySmall)
            Text(
                taskUi.nextDueLabel,
                style = MaterialTheme.typography.bodySmall,
                color = if (taskUi.isDue) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text("Reminder", style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                REMINDER_OPTIONS.forEach { option ->
                    FilterChip(
                        selected = taskUi.task.reminderTime == option,
                        onClick = { onSetReminder(option) },
                        label = { Text(option ?: "Off") }
                    )
                }
            }
            Button(onClick = onComplete, modifier = Modifier.align(Alignment.End)) {
                Text("Mark done")
            }
        }
    }
}
@Composable
fun AddCareTaskDialog(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var title by rememberSaveable { mutableStateOf("") }
    var days by rememberSaveable { mutableIntStateOf(1) }
    val frequencies = listOf(1 to "Daily", 7 to "Weekly", 30 to "Monthly")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New care task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task, e.g. Feed") },
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    frequencies.forEach { (value, label) ->
                        FilterChip(
                            selected = days == value,
                            onClick = { days = value },
                            label = { Text(label) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(enabled = title.isNotBlank(), onClick = { onConfirm(title, days) }) {
                Text("Add")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}