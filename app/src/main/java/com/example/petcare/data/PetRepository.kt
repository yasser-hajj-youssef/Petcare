package com.example.petcare.data

import kotlinx.coroutines.flow.Flow


class PetRepository(private val dao: PetDao) {

    val pets: Flow<List<PetEntity>> = dao.observeAll()


    fun observePet(id: String): Flow<PetEntity?> =
        dao.observeById(id)


    suspend fun addPet(
        name: String,
        species: String,
        birthYear: Int? = null,
        breedId: String? = null,
        breedName: String? = null
    ) {

        dao.upsert(
            PetEntity(
                name = name.trim(),
                species = species.trim(),
                birthYear = birthYear,
                breedId = breedId,
                breedName = breedName
            )
        )
    }

    suspend fun updateNotes(petId: String, notes: String) {

        dao.updateNotes(
            petId = petId,
            notes = notes.trim().ifBlank { null }
        )
    }

    suspend fun updatePet(
        pet: PetEntity,
        name: String,
        species: String,
        birthYear: Int?,
        breedId: String?,
        breedName: String?
    ) {
        dao.upsert(
            pet.copy(
                name = name.trim(),
                species = species.trim(),
                birthYear = birthYear,
                breedId = breedId,
                breedName = breedName
            )
        )
    }

    suspend fun deletePet(pet: PetEntity) {

        dao.delete(pet)
    }
}