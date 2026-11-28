package com.example.petcare.data

import com.example.petcare.data.remote.BreedApi
import com.example.petcare.data.remote.BreedDto
import kotlinx.coroutines.flow.Flow


class BreedRepository(
    private val dogApi: BreedApi,
    private val catApi: BreedApi,
    private val dao: BreedDao
) {

    fun observeBreeds(species: String): Flow<List<BreedEntity>> {
        return dao.observeBySpecies(species)
    }


    fun observeBreed(id: String): Flow<BreedEntity?> {
        return dao.observeById(id)
    }


    suspend fun isEmpty(): Boolean {
        return dao.count() == 0
    }


    suspend fun refreshBreeds() {

        val now = System.currentTimeMillis()

        val dogs = dogApi.getBreeds().map {
            it.toEntity("Dog", now)
        }

        val cats = catApi.getBreeds().map {
            it.toEntity("Cat", now)
        }

        dao.upsertAll(dogs + cats)
    }
}


private fun BreedDto.toEntity(
    species: String,
    now: Long
): BreedEntity {

    return BreedEntity(
        id = "${species.lowercase()}:$id",
        species = species,
        name = name,
        temperament = temperament,
        lifeSpan = lifeSpan,
        weightKg = weight?.metric,
        origin = origin,
        imageId = referenceImageId,
        updatedAt = now
    )
}