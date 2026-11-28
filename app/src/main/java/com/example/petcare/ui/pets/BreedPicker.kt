package com.example.petcare.ui.pets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petcare.data.BreedEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedPicker(
    breeds: List<BreedEntity>,
    filteredBreeds: List<BreedEntity>,
    query: String,
    onQueryChange: (String) -> Unit,
    onSelect: (BreedEntity?) -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                "Select breed",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("Search breeds") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = 400.dp)
            ) {

                item {

                    ListItem(
                        headlineContent = {
                            Text("Unknown / mixed")
                        },
                        modifier = Modifier.clickable {
                            onSelect(null)
                        }
                    )

                    HorizontalDivider()
                }

                if (breeds.isEmpty()) {

                    item {

                        Text(
                            "Breeds need internet the first time"
                        )

                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }

                } else if (filteredBreeds.isEmpty()) {

                    item {
                        Text("No matching breeds")
                    }

                } else {

                    items(
                        filteredBreeds,
                        key = { it.id }
                    ) { breed ->

                        ListItem(
                            headlineContent = {
                                Text(breed.name)
                            },
                            modifier = Modifier.clickable {
                                onSelect(breed)
                            }
                        )

                        HorizontalDivider()
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}