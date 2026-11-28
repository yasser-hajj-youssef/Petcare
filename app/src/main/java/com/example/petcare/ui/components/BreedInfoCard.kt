package com.example.petcare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petcare.data.BreedEntity


@Composable
fun BreedInfoCard(
    breed: BreedEntity?,
    breedName: String?
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Breed information",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = breed?.name ?: breedName ?: "Unknown",
                style = MaterialTheme.typography.titleLarge
            )

            if (breed != null) {

                HorizontalDivider()

                BreedInfoRow(
                    label = "Temperament",
                    value = breed.temperament
                )

                BreedInfoRow(
                    label = "Life expectancy",
                    value = breed.lifeSpan
                )

                BreedInfoRow(
                    label = "Typical weight",
                    value = breed.weightKg?.let { "$it kg" }
                )

                BreedInfoRow(
                    label = "Origin",
                    value = breed.origin
                )
            }
        }
    }
}


@Composable
private fun BreedInfoRow(
    label: String,
    value: String?
) {

    if (!value.isNullOrBlank()) {

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}