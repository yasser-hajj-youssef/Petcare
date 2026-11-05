package com.example.petcare.ui.pets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare.data.PetEntity

@Composable
fun PetsRoute(
    onAddPet: () -> Unit,
    onOpenPet: (String) -> Unit,
    viewModel: PetsViewModel = viewModel(factory = PetsViewModel.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PetsScreen(state = state, onAddPet = onAddPet, onOpenPet = onOpenPet)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsScreen(
    state: PetsUiState,
    onAddPet: () -> Unit,
    onOpenPet: (String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("My pets") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPet) { Text("+") }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (state) {
                PetsUiState.Loading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                PetsUiState.Empty ->
                    Text(
                        "No pets yet. Tap + to add your first pet.",
                        modifier = Modifier.align(Alignment.Center).padding(24.dp)
                    )

                is PetsUiState.Content ->
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.pets, key = { it.id }) { pet ->
                            PetRow(pet = pet, onClick = { onOpenPet(pet.id) })
                        }
                    }
            }
        }
    }
}

@Composable
fun PetRow(pet: PetEntity, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(pet.name, style = MaterialTheme.typography.titleMedium)
            val born = pet.birthYear?.let { " · born $it" } ?: ""
            Text(pet.species + born, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetsScreenPreview() {
    PetsScreen(
        state = PetsUiState.Content(
            listOf(
                PetEntity(name = "Bella", species = "Dog", birthYear = 2021),
                PetEntity(name = "Milo", species = "Cat")
            )
        ),
        onAddPet = {}, onOpenPet = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PetsScreenEmptyPreview() {
    PetsScreen(state = PetsUiState.Empty, onAddPet = {}, onOpenPet = {})
}