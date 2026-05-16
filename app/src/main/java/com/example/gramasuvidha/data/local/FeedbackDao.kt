package com.example.gramasuvidha.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gramasuvidha.data.model.Feedback
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedbackDao {
    @Insert
    suspend fun insertFeedback(feedback: Feedback)

    @Query("SELECT * FROM feedback WHERE projectId = :projectId ORDER BY timestamp DESC")
    fun getFeedbackForProject(projectId: Int): Flow<List<Feedback>>
}
