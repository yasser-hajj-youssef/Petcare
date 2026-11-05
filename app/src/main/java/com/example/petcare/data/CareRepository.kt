package com.example.petcare.data
import kotlinx.coroutines.flow.Flow
class CareRepository(private val dao: CareDao) {
    fun observeTasks(petId: String): Flow<List<CareTaskEntity>> = dao.observeTasks(petId)
    fun observeLogs(petId: String): Flow<List<CareLogEntity>> = dao.observeLogs(petId)
    suspend fun addTask(petId: String, title: String, frequencyDays: Int) {
        dao.upsertTask(
            CareTaskEntity(petId = petId, title = title.trim(), frequencyDays = frequencyDays)
        )
    }
    suspend fun completeTask(task: CareTaskEntity, now: Long = System.currentTimeMillis()) {
        dao.completeTask(task, now)
    }
    suspend fun setReminder(taskId: String, time: String?) {
        dao.setReminder(taskId, time)
    }
}