package com.example.petcare

import android.content.Context
import androidx.room.Room
import com.example.petcare.data.*
import com.example.petcare.notify.NotificationHelper

class AppContainer(private val context: Context) {

    private val database = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "pet_care.db"
    )
        .addMigrations(MIGRATION_1_2)
        .build()

    val petRepository by lazy {
        PetRepository(database.petDao())
    }

    val careRepository by lazy {
        CareRepository(database.careDao())
    }

    val notificationHelper by lazy {
        NotificationHelper(context)
    }
}