package com.example.petcare.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete
@Dao
interface PetDao {
    @Query("SELECT * FROM pets ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :id")
    fun observeById(id: String): Flow<PetEntity?>

    @Query("UPDATE pets SET notes = :notes WHERE id = :petId")
    suspend fun updateNotes(petId: String, notes: String?)

    @Delete
    suspend fun delete(pet: PetEntity)
    @Upsert
    suspend fun upsert(pet: PetEntity)
}