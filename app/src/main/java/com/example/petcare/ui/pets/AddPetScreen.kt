package com.example.petcare.ui.pets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petcare.data.BreedEntity
import com.example.petcare.data.PetEntity
import kotlinx.coroutines.flow.Flow


val SPECIES = listOf("Dog", "Cat", "Bird", "Other")


@Composable
fun AddPetRoute(
    onDone: () -> Unit,
    viewModel: AddPetViewModel = viewModel(
        factory = AddPetViewModel.Factory
    )
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.saved) {
        if (state.saved) onDone()
    }

    when {

        state.isLoading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.notFound -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Pet not found")

                Button(onClick = onDone) {
                    Text("Back")
                }
            }
        }

        else -> {

            AddPetScreen(
                isSaving = state.isSaving,
                onSave = viewModel::save,
                onCancel = onDone,
                observeBreeds = viewModel::observeBreeds,
                filterBreeds = viewModel::filterBreeds,
                onRetry = viewModel::retryBreeds,
                existingPet = state.existingPet,
                isEditMode = viewModel.isEditMode,
                error = state.error
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    isSaving: Boolean,
    onSave: (String, String, Int?, String?, String?) -> Unit,
    onCancel: () -> Unit,
    observeBreeds: (String) -> Flow<List<BreedEntity>>,
    filterBreeds: (List<BreedEntity>, String) -> List<BreedEntity>,
    onRetry: () -> Unit,
    existingPet: PetEntity? = null,
    isEditMode: Boolean = false,
    error: String? = null
) {

    // Determine the initial species

    val initialSpecies = SPECIES
        .firstOrNull {
            it != "Other" &&
                    it.equals(
                        existingPet?.species,
                        ignoreCase = true
                    )
        }
        ?: if (existingPet == null) "Dog" else "Other"


    // Form values

    var name by rememberSaveable(existingPet?.id) {
        mutableStateOf(existingPet?.name ?: "")
    }

    var species by rememberSaveable(existingPet?.id) {
        mutableStateOf(initialSpecies)
    }

    var otherSpecies by rememberSaveable(existingPet?.id) {
        mutableStateOf(
            if (initialSpecies == "Other")
                existingPet?.species ?: ""
            else
                ""
        )
    }

    var birthYear by rememberSaveable(existingPet?.id) {
        mutableStateOf(
            existingPet?.birthYear?.toString() ?: ""
        )
    }

    var breedId by rememberSaveable(existingPet?.id) {
        mutableStateOf(existingPet?.breedId)
    }

    var breedName by rememberSaveable(existingPet?.id) {
        mutableStateOf(existingPet?.breedName)
    }

    var showBreedPicker by rememberSaveable {
        mutableStateOf(false)
    }

    var query by rememberSaveable {
        mutableStateOf("")
    }

    var showErrors by rememberSaveable {
        mutableStateOf(false)
    }


    // Validation

    val nameError = validatePetName(name)

    val yearError = validateBirthYear(birthYear)

    val otherError =
        species == "Other" && otherSpecies.isBlank()

    val supportsBreeds =
        species == "Dog" || species == "Cat"


    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        if (isEditMode)
                            "Edit pet"
                        else
                            "Add a pet"
                    )
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth(),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            // Pet name

            OutlinedTextField(
                value = name,

                onValueChange = {
                    name = it
                },

                label = {
                    Text("Name")
                },

                singleLine = true,

                isError = showErrors && nameError != null,

                supportingText = {
                    if (showErrors && nameError != null) {
                        Text(nameError)
                    }
                },

                modifier = Modifier.fillMaxWidth()
            )


            // Species selection

            Text(
                "Species",
                style = MaterialTheme.typography.labelLarge
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                SPECIES.forEach { option ->

                    FilterChip(
                        selected = species == option,

                        onClick = {

                            if (species != option) {

                                species = option

                                breedId = null
                                breedName = null
                                query = ""
                                showBreedPicker = false
                            }
                        },

                        label = {
                            Text(option)
                        }
                    )
                }
            }


            // Custom species

            if (species == "Other") {

                OutlinedTextField(
                    value = otherSpecies,

                    onValueChange = {
                        otherSpecies = it
                    },

                    label = {
                        Text("What kind of pet?")
                    },

                    singleLine = true,

                    isError = showErrors && otherError,

                    supportingText = {
                        if (showErrors && otherError) {
                            Text("Species is required")
                        }
                    },

                    modifier = Modifier.fillMaxWidth()
                )
            }


            // Breed selection for Dog and Cat

            if (supportsBreeds) {

                Text(
                    "Breed (optional)",
                    style = MaterialTheme.typography.labelLarge
                )

                OutlinedButton(
                    onClick = {

                        query = ""
                        showBreedPicker = true
                    },

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        breedName ?: "Select breed"
                    )
                }
            }


            // Birth year

            OutlinedTextField(
                value = birthYear,

                onValueChange = {

                    birthYear = it
                        .filter(Char::isDigit)
                        .take(4)
                },

                label = {
                    Text("Birth year (optional)")
                },

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),

                isError = showErrors && yearError != null,

                supportingText = {
                    if (showErrors && yearError != null) {
                        Text(yearError)
                    }
                },

                modifier = Modifier.fillMaxWidth()
            )


            // Saving errors

            if (error != null) {

                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }


            // Cancel and Save buttons

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onCancel,
                    enabled = !isSaving
                ) {

                    Text("Cancel")
                }


                Button(
                    enabled = !isSaving,

                    onClick = {

                        showErrors = true

                        if (
                            nameError == null &&
                            yearError == null &&
                            !otherError
                        ) {

                            val finalSpecies =
                                if (species == "Other")
                                    otherSpecies.trim()
                                else
                                    species

                            onSave(
                                name,
                                finalSpecies,
                                birthYear.toIntOrNull(),
                                if (supportsBreeds) breedId else null,
                                if (supportsBreeds) breedName else null
                            )
                        }
                    }

                ) {

                    Text(
                        when {
                            isSaving -> "Saving..."
                            isEditMode -> "Save changes"
                            else -> "Save pet"
                        }
                    )
                }
            }
        }
    }


    // Breed picker bottom sheet

    if (showBreedPicker && supportsBreeds) {

        val breedFlow = remember(species) {
            observeBreeds(species)
        }

        val breeds by breedFlow.collectAsStateWithLifecycle(
            initialValue = emptyList()
        )

        val filteredBreeds = remember(breeds, query) {
            filterBreeds(breeds, query)
        }

        BreedPicker(
            breeds = breeds,
            filteredBreeds = filteredBreeds,
            query = query,

            onQueryChange = {
                query = it
            },

            onSelect = { breed ->

                breedId = breed?.id
                breedName = breed?.name

                showBreedPicker = false
            },

            onRetry = onRetry,

            onDismiss = {
                showBreedPicker = false
            }
        )
    }
}