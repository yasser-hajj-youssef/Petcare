package com.example.petcare.ui.pets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
val SPECIES = listOf("Dog", "Cat", "Bird", "Other")
@Composable
fun AddPetRoute(
    onDone: () -> Unit,
    viewModel: AddPetViewModel = viewModel(factory = AddPetViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.saved) {
        if (state.saved) onDone()
    }
    AddPetScreen(isSaving = state.isSaving, onSave = viewModel::save, onCancel = onDone)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    isSaving: Boolean,
    onSave: (String, String, Int?) -> Unit,
    onCancel: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var species by rememberSaveable { mutableStateOf(SPECIES.first()) }
    var birthYear by rememberSaveable { mutableStateOf("") }
    var showErrors by rememberSaveable {
        mutableStateOf(false)
    }
    val nameError = validatePetName(name)
    val yearError = validateBirthYear(birthYear)
    Scaffold(topBar = { TopAppBar(title = { Text("Add a pet") }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                singleLine = true,
                isError = showErrors && nameError != null,
                supportingText = { if (showErrors && nameError != null) Text(nameError) },
                modifier = Modifier.fillMaxWidth()
            )
            Text("Species", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SPECIES.forEach { option ->
                    FilterChip(
                        selected = species == option,
                        onClick = { species = option },
                        label = { Text(option) }
                    )
                }
            }
            OutlinedTextField(
                value = birthYear,
                onValueChange = { birthYear = it.filter(Char::isDigit).take(4) },
                label = { Text("Birth year (optional)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = showErrors && yearError != null,
                supportingText = { if (showErrors && yearError != null) Text(yearError) },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onCancel) { Text("Cancel") }
                Button(
                    enabled = !isSaving,
                    onClick = {
                        showErrors = true
                        if (nameError == null && yearError == null) {
                            onSave(name, species, birthYear.toIntOrNull())
                        }
                    }
                ) { Text(if (isSaving) "Saving..." else "Save pet") }
            }
        }
    }
}