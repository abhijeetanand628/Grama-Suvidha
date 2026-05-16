package com.example.gramasuvidha.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gramasuvidha.R
import com.example.gramasuvidha.viewmodel.ProjectViewModel

/**
 * Screen displaying the detailed view of a single project.
 * Includes progress information, before/after images, and a feedback form with a 5-star rating.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    projectId: Int,
    viewModel: ProjectViewModel,
    onBackClick: () -> Unit,
    onLanguageToggle: () -> Unit
) {
    val projectFlow = remember(projectId, viewModel) { viewModel.getProject(projectId) }
    val project by projectFlow.collectAsState(initial = null)
    val context = LocalContext.current
    var feedbackText by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }
    val isAdmin by viewModel.isAdminLoggedIn.collectAsState()
    var editMode by remember { mutableStateOf(false) }
    var sliderValue by remember { mutableFloatStateOf(0f) }

    val configuration = LocalConfiguration.current
    val isKannada = configuration.locales.get(0).language == "kn"
    val displayName = if (isKannada && project?.nameKn != null) project?.nameKn else project?.name ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(displayName ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        // Use AutoMirrored ArrowBack to support RTL layouts properly
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onLanguageToggle) {
                        Text("EN/KN", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        if (project == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            project?.let { p ->
            val displayDescription = if (isKannada && p.descriptionKn != null) p.descriptionKn else p.description
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Display either Kannada or English description based on locale
                Text(text = displayDescription, style = MaterialTheme.typography.bodyLarge)

                Text(text = "${stringResource(R.string.completion_date)} ${p.expectedCompletion}", fontWeight = FontWeight.SemiBold)

                Column {
                    Text(text = stringResource(R.string.progress), fontWeight = FontWeight.Bold)
                    
                    // Ensure animation triggers when p.progress loads
                    var targetProgress by remember { mutableFloatStateOf(0f) }
                    LaunchedEffect(p.progress) {
                        targetProgress = p.progress / 100f
                    }
                    
                    val animatedProgress by animateFloatAsState(
                        targetValue = targetProgress,
                        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                        label = "progressDetailAnimation"
                    )
                    
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(12.dp)
                    )
                    val completeText = if (isKannada) "ಪೂರ್ಣಗೊಂಡಿದೆ" else "Complete"
                    Text(text = "${p.progress}% $completeText", Modifier.align(Alignment.End))

                    if (isAdmin) {
                        Spacer(modifier = Modifier.height(16.dp))
                        if (editMode) {
                            Slider(
                                value = sliderValue,
                                onValueChange = { sliderValue = it },
                                valueRange = 0f..100f
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = { editMode = false }) { Text(stringResource(R.string.cancel)) }
                                Button(onClick = { 
                                    viewModel.updateProjectProgress(p, sliderValue.toInt())
                                    editMode = false
                                }) {
                                    Text(stringResource(R.string.save_progress))
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = { 
                                    sliderValue = p.progress.toFloat()
                                    editMode = true 
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(stringResource(R.string.update_progress))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Status dropdown
                        val statusOptions = listOf("Just Started", "In Progress", "Near Completion", "Completed", "On Hold")
                        var statusExpanded by remember { mutableStateOf(false) }
                        
                        val statusLabel = if (isKannada) "ಸ್ಥಿತಿ ಬದಲಾಯಿಸಿ" else "Change Status"
                        ExposedDropdownMenuBox(
                            expanded = statusExpanded,
                            onExpandedChange = { statusExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = p.status,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(statusLabel) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = statusExpanded,
                                onDismissRequest = { statusExpanded = false }
                            ) {
                                statusOptions.forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status) },
                                        onClick = {
                                            viewModel.updateProjectStatus(p, status)
                                            statusExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.before_img), fontWeight = FontWeight.Bold)
                        AsyncImage(
                            model = p.beforeImage,
                            contentDescription = "Before",
                            modifier = Modifier.height(150.dp).fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.after_img), fontWeight = FontWeight.Bold)
                        AsyncImage(
                            model = p.afterImage,
                            contentDescription = "After",
                            modifier = Modifier.height(150.dp).fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                HorizontalDivider()

                Text(text = stringResource(R.string.citizen_feedback), style = MaterialTheme.typography.titleMedium)

                if (isAdmin) {
                    // Admin sees real reported issues from database
                    val feedbackList by viewModel.getFeedbackForProject(p.id).collectAsState(initial = emptyList())
                    val reportedIssuesTitle = if (isKannada) "ವರದಿ ಮಾಡಿದ ಸಮಸ್ಯೆಗಳು" else "Reported Issues"
                    Text(text = reportedIssuesTitle, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    
                    if (feedbackList.isEmpty()) {
                        val noIssuesText = if (isKannada) "ಇನ್ನೂ ಯಾವುದೇ ಸಮಸ್ಯೆಗಳು ವರದಿಯಾಗಿಲ್ಲ" else "No issues reported yet"
                        Text(
                            text = noIssuesText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        feedbackList.forEach { fb ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = fb.citizenName, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                        Text(text = "⭐".repeat(fb.rating), style = MaterialTheme.typography.bodySmall)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = fb.description, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                } else {
                    // Citizen sees the report form
                    // 1-5 Star Interactive Rating System
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star $i",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { rating = i },
                                tint = if (i <= rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        label = { Text(stringResource(R.string.issue_description)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Button(
                        onClick = {
                            if (feedbackText.isBlank() && rating == 0) {
                                Toast.makeText(context, context.getString(R.string.feedback_empty), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.submitFeedback(p.id, rating, feedbackText)
                                Toast.makeText(context, context.getString(R.string.feedback_submitted), Toast.LENGTH_SHORT).show()
                                feedbackText = ""
                                rating = 0
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(R.string.report_issue))
                    }
                }
            }
        }
    }
}
}
