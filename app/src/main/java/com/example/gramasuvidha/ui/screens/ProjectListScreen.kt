package com.example.gramasuvidha.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gramasuvidha.R
import com.example.gramasuvidha.data.model.Project
import com.example.gramasuvidha.viewmodel.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    viewModel: ProjectViewModel,
    onProjectClick: (Int) -> Unit,
    onLanguageToggle: () -> Unit,
    onAdminLoginClick: () -> Unit,
    onAddProjectClick: () -> Unit
) {
    val projects by viewModel.allProjects.collectAsState()
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    var showLogoutBanner by remember { mutableStateOf(false) }
    
    val blurRadius by animateFloatAsState(
        targetValue = if (showLogoutBanner) 10f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "blurAnimation"
    )
    
    LaunchedEffect(showLogoutBanner) {
        if (showLogoutBanner) {
            kotlinx.coroutines.delay(3000)
            showLogoutBanner = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.blur(blurRadius.dp),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.projects_title)) },
                    actions = {
                        TextButton(onClick = onLanguageToggle) {
                            Text("EN/KN", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        if (isAdmin) {
                            TextButton(onClick = { 
                                viewModel.logout()
                                showLogoutBanner = true
                            }) {
                                Text(stringResource(R.string.logout), color = MaterialTheme.colorScheme.error)
                            }
                        } else {
                            IconButton(onClick = onAdminLoginClick) {
                                Icon(Icons.Default.Person, contentDescription = "Admin Login", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            floatingActionButton = {
                if (isAdmin) {
                    FloatingActionButton(onClick = onAddProjectClick) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_project))
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Start
        ) { padding ->
            if (projects.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    contentPadding = padding,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        HeroSection()
                    }
                    items(projects) { project ->
                        ProjectItem(project = project, onClick = { onProjectClick(project.id) })
                    }
                }
            }
        }
        
        // Top logout banner overlay
        androidx.compose.animation.AnimatedVisibility(
            visible = showLogoutBanner,
            enter = androidx.compose.animation.slideInVertically { -it } + androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.slideOutVertically { -it } + androidx.compose.animation.fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "✓ Logged out successfully",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun HeroSection() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.hero_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.hero_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ProjectItem(project: Project, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isKannada = configuration.locales.get(0).language == "kn"
    val displayName = if (isKannada && project.nameKn != null) project.nameKn else project.name
    val displayStatus = if (isKannada && project.statusKn != null) project.statusKn else project.status

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = project.beforeImage,
                contentDescription = displayName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${stringResource(R.string.budget)} ₹${project.budget}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "${stringResource(R.string.status)} $displayStatus", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
            var targetProgress by remember { mutableFloatStateOf(0f) }
            LaunchedEffect(project.progress) {
                targetProgress = project.progress / 100f
            }
            
            val animatedProgress by animateFloatAsState(
                targetValue = targetProgress,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                label = "progressAnimation"
            )
            
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth().height(12.dp)
            )
            Text(
                text = "${project.progress}%",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
            }
        }
    }
}
