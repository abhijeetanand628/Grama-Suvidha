package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.R
import com.example.gramasuvidha.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(
    viewModel: ProjectViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var expectedDate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_project)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.project_name)) },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text(stringResource(R.string.budget)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            OutlinedTextField(
                value = expectedDate,
                onValueChange = { expectedDate = it },
                label = { Text(stringResource(R.string.expected_date)) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val budgetLong = budget.toLongOrNull() ?: 0L
                    if (name.isNotBlank() && description.isNotBlank()) {
                        viewModel.addProject(name, description, budgetLong, expectedDate)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && description.isNotBlank()
            ) {
                Text(stringResource(R.string.save_project))
            }
        }
    }
}
