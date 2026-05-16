package com.example.gramasuvidha.data.repository

import android.content.Context
import android.util.Log
import com.example.gramasuvidha.data.local.ProjectDao
import com.example.gramasuvidha.data.model.Project
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val context: Context
) {
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    fun getProject(id: Int): Flow<Project> {
        return projectDao.getProjectById(id)
    }

    suspend fun syncProjectsFromJson() {
        withContext<Unit>(Dispatchers.IO) {
            try {
                // Only sync if the database is empty, to preserve admin edits
                if (projectDao.getAllProjects().first().isEmpty()) {
                    val inputStream = context.assets.open("projects.json")
                    val reader = InputStreamReader(inputStream)
                    val type = object : TypeToken<List<Project>>() {}.type
                    val projects: List<Project> = Gson().fromJson(reader, type)
                    reader.close()
                    
                    projectDao.insertAll(projects)
                }
            } catch (e: Exception) {
                Log.e("ProjectRepository", "Error syncing JSON to Room", e)
            }
        }
    }

    suspend fun updateProject(project: Project) {
        withContext(Dispatchers.IO) {
            projectDao.updateProject(project)
        }
    }

    suspend fun addProject(project: Project) {
        withContext(Dispatchers.IO) {
            projectDao.insertProject(project)
        }
    }
}
