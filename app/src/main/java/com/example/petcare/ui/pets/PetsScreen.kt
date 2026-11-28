package com.example.petcare.ui.pets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare.data.PetEntity
import com.example.petcare.ui.components.PetAvatar


@Composable
fun PetsRoute(

    onAddPet: () -> Unit,

    onOpenPet: (String) -> Unit,

    onOpenSettings: () -> Unit,

    viewModel: PetsViewModel = viewModel(
        factory = PetsViewModel.Factory
    )

) {

    val state by viewModel.uiState
        .collectAsStateWithLifecycle()


    PetsScreen(

        state = state,

        onAddPet = onAddPet,

        onOpenPet = onOpenPet,

        onOpenSettings = onOpenSettings
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsScreen(

    state: PetsUiState,

    onAddPet: () -> Unit,

    onOpenPet: (String) -> Unit,

    onOpenSettings: () -> Unit

) {


    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("My pets")
                },

                actions = {

                    IconButton(
                        onClick = onOpenSettings
                    ) {

                        Icon(

                            imageVector = Icons.Filled.Settings,

                            contentDescription = "Settings",

                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },


        floatingActionButton = {

            FloatingActionButton(

                onClick = onAddPet

            ) {

                Text("+")
            }
        }

    ) { padding ->


        Box(

            modifier = Modifier
                .padding(padding)
                .fillMaxSize()

        ) {


            when (state) {


                PetsUiState.Loading -> {

                    CircularProgressIndicator(

                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }


                PetsUiState.Empty -> {

                    Column(

                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally,

                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)

                    ) {

                        Text(

                            text = "No pets yet",

                            style = MaterialTheme
                                .typography.titleLarge
                        )


                        Text(
                            "Add your first pet to get started."
                        )


                        Button(
                            onClick = onAddPet
                        ) {

                            Text("Add a pet")
                        }
                    }
                }


                is PetsUiState.Content -> {

                    LazyColumn(

                        contentPadding = PaddingValues(16.dp),

                        verticalArrangement =
                            Arrangement.spacedBy(8.dp)

                    ) {

                        items(

                            items = state.pets,

                            key = { it.id }

                        ) { pet ->


                            PetRow(

                                pet = pet,

                                onClick = {
                                    onOpenPet(pet.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PetRow(

    pet: PetEntity,

    onClick: () -> Unit

) {


    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)

    ) {


        Row(

            modifier = Modifier.padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.spacedBy(16.dp)

        ) {


            PetAvatar(

                name = pet.name,

                species = pet.species,

                petId = pet.id,

                size = 56.dp
            )


            Column(

                modifier = Modifier.weight(1f),

                verticalArrangement =
                    Arrangement.spacedBy(4.dp)

            ) {


                Text(

                    text = pet.name,

                    style = MaterialTheme
                        .typography.titleMedium
                )


                val description = buildString {

                    append(pet.species)


                    if (!pet.breedName.isNullOrBlank()) {

                        append(" · ")

                        append(pet.breedName)
                    }


                    if (pet.birthYear != null) {

                        append(" · born ")

                        append(pet.birthYear)
                    }
                }


                Text(

                    text = description,

                    style = MaterialTheme
                        .typography.bodyMedium
                )
            }
        }
    }
}