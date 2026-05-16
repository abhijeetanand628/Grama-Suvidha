package com.example.gramasuvidha.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.gramasuvidha.R
import com.example.gramasuvidha.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(viewModel: ProjectViewModel, onNavigateBack: () -> Unit) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.admin_login)) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.enter_password),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it 
                    showError = false
                },
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(if (passwordVisible) "Hide" else "Show")
                    }
                },
                isError = showError,
                modifier = Modifier.fillMaxWidth()
            )
            if (showError) {
                Text(stringResource(R.string.incorrect_password), color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val success = viewModel.loginAsAdmin(password)
                    if (success) {
                        onNavigateBack()
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.login))
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onNavigateBack) {
                Text(stringResource(R.string.cancel))
            }
        }
    }
}
