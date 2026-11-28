package com.example.petcare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@Composable
fun PetNotesCard(
    petId: String,
    savedNotes: String?,
    onSave: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    var notes by rememberSaveable(petId) {
        mutableStateOf(savedNotes.orEmpty())
    }

    var saveRequested by rememberSaveable(petId) {
        mutableStateOf(false)
    }

    val hasChanges =
        notes.trim() != savedNotes.orEmpty().trim()


    LaunchedEffect(savedNotes) {

        notes = savedNotes.orEmpty()
    }


    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Notes",
                style = MaterialTheme.typography.titleMedium
            )


            OutlinedTextField(
                value = notes,

                onValueChange = {
                    notes = it
                    saveRequested = false
                },

                label = {
                    Text("Pet notes")
                },

                placeholder = {
                    Text("Food brand, allergies, vet phone...")
                },

                minLines = 3,
                maxLines = 6,

                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {

                    saveRequested = true

                    onSave(notes.trim())

                    focusManager.clearFocus()
                },

                enabled = hasChanges,

                modifier = Modifier.fillMaxWidth()
            ) {

                Text("Save notes")
            }


            if (saveRequested && !hasChanges) {

                Text(
                    text = "Notes saved successfully!",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )

            } else if (hasChanges) {

                Text(
                    text = "Unsaved changes",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}