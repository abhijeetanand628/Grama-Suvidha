package com.example.gramasuvidha.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramasuvidha.data.local.AppDatabase
import com.example.gramasuvidha.data.local.FeedbackDao
import com.example.gramasuvidha.data.model.Feedback
import com.example.gramasuvidha.data.model.Project
import com.example.gramasuvidha.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProjectRepository
    private val feedbackDao: FeedbackDao

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        val projectDao = db.projectDao()
        feedbackDao = db.feedbackDao()
        repository = ProjectRepository(projectDao, application)
        
        viewModelScope.launch {
            repository.syncProjectsFromJson()
        }
    }

    val allProjects: StateFlow<List<Project>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun getProject(id: Int): kotlinx.coroutines.flow.Flow<Project> {
        return repository.getProject(id)
    }

    fun updateProjectProgress(project: Project, newProgress: Int) {
        viewModelScope.launch {
            repository.updateProject(project.copy(progress = newProgress))
        }
    }

    fun updateProjectStatus(project: Project, newStatus: String) {
        viewModelScope.launch {
            repository.updateProject(project.copy(status = newStatus))
        }
    }

    fun addProject(name: String, description: String, budget: Long, expectedDate: String) {
        viewModelScope.launch {
            val newId = (allProjects.value.maxOfOrNull { it.id } ?: 0) + 1
            val project = Project(
                id = newId,
                name = name,
                description = description,
                budget = budget,
                progress = 0,
                status = "Just Started",
                expectedCompletion = expectedDate,
                beforeImage = "https://picsum.photos/seed/newproject${System.currentTimeMillis()}/600/400",
                afterImage = "https://picsum.photos/seed/newprojectafter${System.currentTimeMillis()}/600/400"
            )
            repository.addProject(project)
        }
    }

    fun loginAsAdmin(password: String): Boolean {
        if (password == "admin123") {
            _isAdminLoggedIn.value = true
            return true
        }
        return false
    }

    fun logout() {
        _isAdminLoggedIn.value = false
    }

    fun submitFeedback(projectId: Int, rating: Int, description: String) {
        viewModelScope.launch {
            feedbackDao.insertFeedback(
                Feedback(
                    projectId = projectId,
                    rating = rating,
                    description = description
                )
            )
        }
    }

    fun getFeedbackForProject(projectId: Int): kotlinx.coroutines.flow.Flow<List<Feedback>> {
        return feedbackDao.getFeedbackForProject(projectId)
    }
}
