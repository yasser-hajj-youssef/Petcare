package com.example.petcare

import android.app.Application
import com.example.petcare.notify.createCareRemindersChannel
import com.example.petcare.work.WorkScheduler


class PetCareApplication : Application() {

    lateinit var container: AppContainer
        private set


    override fun onCreate() {

        super.onCreate()

        container = AppContainer(this)

        createCareRemindersChannel(this)

        WorkScheduler.scheduleBreedRefresh(this)
    }
}