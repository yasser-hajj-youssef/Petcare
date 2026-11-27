package com.example.petcare.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

const val CARE_REMINDERS_CHANNEL_ID = "care_reminders"

fun createCareRemindersChannel(context: Context) {

    val channel = NotificationChannel(
        CARE_REMINDERS_CHANNEL_ID,
        "Care reminders",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Feeding, grooming, vaccine and supply reminders"
    }

    context.getSystemService(NotificationManager::class.java)
        .createNotificationChannel(channel)
}