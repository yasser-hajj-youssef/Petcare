package com.example.petcare.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder

object WorkScheduler {

    fun scheduleBreedRefresh(context: Context) {

        val request =
            PeriodicWorkRequestBuilder<RefreshBreedsWorker>(
                7,
                TimeUnit.DAYS
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(
                            NetworkType.CONNECTED
                        )
                        .build()
                )
                .build()


        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "refresh-breeds",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }

    fun refreshBreedsNow(context: Context) {

        val request =
            OneTimeWorkRequestBuilder<RefreshBreedsWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(
                            NetworkType.CONNECTED
                        )
                        .build()
                )
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "refresh-breeds-now",
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}