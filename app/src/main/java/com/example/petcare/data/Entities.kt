package com.example.petcare.data
import androidx.room.*
import java.util.UUID
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val species: String,
    val birthYear: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val breedId: String? = null,
    val breedName: String? = null,
    val notes: String? = null
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
    val reminderTime: String? = null, // "08:00", or null when the reminder is off
    val lastNotifiedAt: Long? = null
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

@Entity(
    tableName = "vaccines",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")]
)
data class VaccineEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val name: String,
    val givenOn: Long? = null,
    val nextDueOn: Long,
    val notes: String? = null,
    val lastNotifiedAt: Long? = null
)

@Entity(
    tableName = "supplies",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")]
)
data class SupplyEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val name: String,
    val unit: String,
    val quantityLeft: Double,
    val dailyUse: Double,
    val updatedAt: Long = System.currentTimeMillis(),
    val lastNotifiedAt: Long? = null
)

@Entity(
    tableName = "weights",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("petId")]
)
data class WeightEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val petId: String,
    val kg: Double,
    val recordedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "breeds")
data class BreedEntity(
    @PrimaryKey
    val id: String,
    val species: String,
    val name: String,
    val temperament: String?,
    val lifeSpan: String?,
    val weightKg: String?,
    val origin: String?,
    val imageId: String?,
    val updatedAt: Long
)