package com.example.petcare

import android.app.Application
import com.example.petcare.notify.createCareRemindersChannel

class PetCareApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)

        createCareRemindersChannel(this)
    }
}