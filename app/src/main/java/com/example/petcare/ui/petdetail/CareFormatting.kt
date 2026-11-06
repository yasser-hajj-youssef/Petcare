package com.example.petcare.ui.petdetail
import com.example.petcare.data.CareLogEntity
import com.example.petcare.data.CareTaskEntity
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
data class CareTaskUi(
    val task: CareTaskEntity,
    val frequencyLabel: String,
    val lastDoneLabel: String,
    val nextDueLabel: String,
    val isDue: Boolean
)
data class CareLogUi(val id: String, val title: String, val whenLabel: String)
private const val DAY_MS = 24L * 60 * 60 * 1000
private val formatter = DateTimeFormatter.ofPattern("EEE d MMM, HH:mm")
fun formatTime(millis: Long): String =
    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(formatter)
fun frequencyLabel(days: Int): String = when (days) {
    1 -> "Every day"
    7 -> "Every week"
    30 -> "Every month"
    else -> "Every $days days"
}
fun CareTaskEntity.toUi(now: Long): CareTaskUi {
    val last = lastCompletedAt
    val nextDue = last?.plus(frequencyDays * DAY_MS)
    return CareTaskUi(
        task = this,
        frequencyLabel = frequencyLabel(frequencyDays),
        lastDoneLabel = last?.let { "Last done: ${formatTime(it)}" } ?: "Never done",
        nextDueLabel = nextDue?.let { "Next due: ${formatTime(it)}" } ?: "Due now",
        isDue = nextDue == null || now >= nextDue
    )
}
fun CareLogEntity.toUi(): CareLogUi = CareLogUi(id, taskTitle, formatTime(completedAt))