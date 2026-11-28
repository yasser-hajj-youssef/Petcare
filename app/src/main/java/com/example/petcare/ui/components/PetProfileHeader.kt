package com.example.petcare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.petcare.data.PetEntity
import java.time.Year


@Composable
fun PetProfileHeader(pet: PetEntity) {

    val currentYear = Year.now().value

    val age = pet.birthYear?.let {
        (currentYear - it).coerceAtLeast(0)
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        PetAvatar(
            name = pet.name,
            species = pet.species,
            petId = pet.id,
            size = 96.dp
        )


        Text(
            text = pet.name,
            style = MaterialTheme.typography.headlineMedium
        )


        val breed = pet.breedName?.let {
            " · $it"
        } ?: ""


        Text(
            text = pet.species + breed,
            style = MaterialTheme.typography.titleMedium
        )


        if (age != null) {

            Text(
                text = when (age) {
                    0 -> "Less than 1 year old"
                    1 -> "About 1 year old"
                    else -> "About $age years old"
                },

                style = MaterialTheme.typography.bodyMedium,

                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}