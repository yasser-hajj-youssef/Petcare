package com.example.petcare.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PetEntity::class,
        CareTaskEntity::class,
        CareLogEntity::class,
        VaccineEntity::class,
        SupplyEntity::class,
        WeightEntity::class,
        BreedEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao

    abstract fun careDao(): CareDao

    abstract fun vaccineDao(): VaccineDao

    abstract fun supplyDao(): SupplyDao

    abstract fun breedDao(): BreedDao
}