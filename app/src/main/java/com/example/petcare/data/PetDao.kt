package com.example.petcare.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :id")
    fun observeById(id: String): Flow<PetEntity?>

    @Upsert
    suspend fun upsert(pet: PetEntity)
}