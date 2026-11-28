package com.example.petcare

import android.content.Context
import androidx.room.Room
import com.example.petcare.data.*
import com.example.petcare.notify.NotificationHelper
import com.example.petcare.data.remote.createBreedApi
import com.example.petcare.data.SettingsRepository
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

    private val dogApi = createBreedApi(
        "https://api.thedogapi.com/v1/",
        BuildConfig.DOG_API_KEY
    )


    private val catApi = createBreedApi(
        "https://api.thecatapi.com/v1/",
        BuildConfig.CAT_API_KEY
    )


    val breedRepository by lazy {
        BreedRepository(
            dogApi,
            catApi,
            database.breedDao()
        )
    }

    val settingsRepository by lazy {

        SettingsRepository(context)
    }
}