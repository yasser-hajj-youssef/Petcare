package com.example.petcare.data
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface CareDao {
    @Query("SELECT * FROM care_tasks WHERE petId = :petId ORDER BY title COLLATE NOCASE")
    fun observeTasks(petId: String): Flow<List<CareTaskEntity>>
    @Query("SELECT * FROM care_logs WHERE petId = :petId ORDER BY completedAt DESC")
    fun observeLogs(petId: String): Flow<List<CareLogEntity>>
    @Upsert
    suspend fun upsertTask(task: CareTaskEntity)
    @Insert
    suspend fun insertLog(log: CareLogEntity)
    @Query("UPDATE care_tasks SET lastCompletedAt = :time WHERE id = :taskId")
    suspend fun setLastCompleted(taskId: String, time: Long)
    @Query("UPDATE care_tasks SET reminderTime = :time WHERE id = :taskId")
    suspend fun setReminder(taskId: String, time: String?)
    @Transaction
    suspend fun completeTask(task: CareTaskEntity, time: Long) {
        insertLog(
            CareLogEntity(
                petId = task.petId, taskId = task.id,
                taskTitle = task.title, completedAt = time
            )
        )
        setLastCompleted(task.id, time)
    }
}