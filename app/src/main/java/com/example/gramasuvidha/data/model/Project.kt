package com.example.gramasuvidha.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey
    val id: Int,
    val name: String,
    val nameKn: String? = null,
    val description: String,
    val descriptionKn: String? = null,
    val budget: Long,
    val progress: Int,
    val status: String,
    val statusKn: String? = null,
    val expectedCompletion: String,
    val beforeImage: String,
    val afterImage: String
)
