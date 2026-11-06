package com.example.petcare.data

import kotlinx.coroutines.flow.Flow
class PetRepository(private val dao: PetDao)
{
    val pets: Flow<List<PetEntity>> = dao.observeAll()

    fun observePet(id: String): Flow<PetEntity?> = dao.observeById(id)

    suspend fun addPet(name:String, species: String, birthYear: Int? = null) {
        dao.upsert(
            PetEntity(
                name = name.trim(),
                species = species,
                birthYear = birthYear
            )
        )
    }
}