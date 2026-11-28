package com.example.petcare.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {

    @Query("SELECT * FROM breeds WHERE species = :species ORDER BY name COLLATE NOCASE")
    fun observeBySpecies(species: String): Flow<List<BreedEntity>>


    @Query("SELECT * FROM breeds WHERE id = :id")
    fun observeById(id: String): Flow<BreedEntity?>


    @Query("SELECT COUNT(*) FROM breeds")
    suspend fun count(): Int


    @Upsert
    suspend fun upsertAll(breeds: List<BreedEntity>)
}