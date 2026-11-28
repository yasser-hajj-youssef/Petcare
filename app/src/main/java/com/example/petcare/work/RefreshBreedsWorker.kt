package com.example.petcare.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.petcare.PetCareApplication
import retrofit2.HttpException
import java.io.IOException


class RefreshBreedsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        val repository =
            (applicationContext as PetCareApplication)
                .container.breedRepository

        return try {

            repository.refreshBreeds()

            Result.success()

        } catch (e: IOException) {

            Result.retry()

        } catch (e: HttpException) {

            if (e.code() >= 500) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}