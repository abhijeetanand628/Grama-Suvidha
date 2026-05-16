package com.example.gramasuvidha.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feedback")
data class Feedback(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val projectId: Int,
    val citizenName: String = "Anonymous",
    val rating: Int,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
