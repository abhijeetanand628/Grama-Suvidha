package com.example.gramasuvidha.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gramasuvidha.data.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Int): Flow<Project>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(projects: List<Project>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)
}
