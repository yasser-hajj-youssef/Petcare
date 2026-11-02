package com.example.petcare.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PetEntity::class, CareTaskEntity::class, CareLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun careDao(): CareDao
}