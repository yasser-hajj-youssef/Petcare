package com.example.petcare

import android.content.Context
import androidx.room.Room
import com.example.petcare.data.*

class AppContainer(context: Context) {
    private val database = Room.databaseBuilder(
        context, AppDatabase::class.java, "pet_care.db"
    ).build()

    val petRepository by lazy { PetRepository(database.petDao()) }
    val careRepository by lazy { CareRepository(database.careDao()) }
}