package com.example.petcare.data
import androidx.room.*
import java.util.UUID
@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val species: String,
    val birthYear: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)
@Entity(
    tableName = "care_tasks",
    foreignKeys = [ForeignKey(
        entity = PetEntity::class,
        parentColumns = ["id"],
        childColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("petId")]
)
data class CareTaskEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val title: String, // "Feed", "Brush", "Flea treatment"
    val frequencyDays: Int, // 1 = daily, 7 = weekly, 30 = monthly
    val lastCompletedAt: Long? = null,
    val reminderTime: String? = null // "08:00", or null when the reminder is off
)
@Entity(
    tableName = "care_logs",
    foreignKeys = [ForeignKey(
        entity = PetEntity::class,
        parentColumns = ["id"],
        childColumns = ["petId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("petId")]
)
data class CareLogEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val taskId: String,
    val taskTitle: String, // copied, so history stays readable
    val completedAt: Long
)