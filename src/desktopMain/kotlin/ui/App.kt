@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)
package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import model.AppSession
import model.Task
import model.TaskImpact
import model.TaskType
import model.TaskUrgency

val ComicSansFontFamily = FontFamily("Comic Sans MS")

val AppTextColor = Color(0xFFFEC7FF) // light purple

val defaultTypography = Typography()
val ComicSansTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = ComicSansFontFamily, color = AppTextColor),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = ComicSansFontFamily, color = AppTextColor)
)

val DarkThemeColors = darkColorScheme(
    primary = Color(0xFFBD1CE3),
    onPrimary = AppTextColor,
    primaryContainer = Color(0xFF3B1E40),
    onPrimaryContainer = AppTextColor,
    secondary = AppTextColor,
    onSecondary = Color.Black,
    background = Color.Black,
    onBackground = AppTextColor,
    surface = Color.Black,
    onSurface = AppTextColor,
    surfaceVariant = Color(0xFF1E1E1E),
    onSurfaceVariant = AppTextColor,
    outline = AppTextColor
)

@Composable
fun App(
    session: AppSession = remember { AppSession() },
    onTasksUpdated: (List<Task>) -> Unit = {},
    onExitRequested: () -> Unit = {}
) {
    var showPopup by remember { mutableStateOf(false) }
    var showSummaryDialog by remember { mutableStateOf(false) }
    var currentFilterType by remember { mutableStateOf("All") }
    var currentFilterUrgency by remember { mutableStateOf("All") }
    var currentFilterImpact by remember { mutableStateOf("All") }
    val tasks = remember { mutableStateListOf<Task>() }
    var editingTask by remember { mutableStateOf<Task?>(null) }

    var actionTicker by remember { mutableStateOf(0) }
    var elapsedSeconds by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            elapsedSeconds = session.getDurationMs() / 1000
        }
    }

    LaunchedEffect(tasks.size) {
        onTasksUpdated(tasks.toList())
    }

    MaterialTheme(colorScheme = DarkThemeColors, typography = ComicSansTypography) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(fontFamily = ComicSansFontFamily, color = AppTextColor),
            LocalContentColor provides AppTextColor
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black,
                contentColor = AppTextColor
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    // Top header bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF141414),
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 2.dp),
                            border = BorderStroke(1.dp, AppTextColor)
                        ) {
                            Text(
                                text = "Time for a new task?",
                                style = MaterialTheme.typography.titleLarge,
                                color = AppTextColor,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                editingTask = null
                                showPopup = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFBD1CE3),
                                contentColor = AppTextColor
                            )
                        ) {
                            Text("Add task")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Multi-category FilterBar
                    FilterBar(
                        selectedFilterType = currentFilterType,
                        selectedFilterUrgency = currentFilterUrgency,
                        selectedFilterImpact = currentFilterImpact,
                        onFilterTypeSelected = {
                            currentFilterType = it
                            session.recordFilterChanged()
                            actionTicker++
                        },
                        onFilterUrgencySelected = {
                            currentFilterUrgency = it
                            session.recordFilterChanged()
                            actionTicker++
                        },
                        onFilterImpactSelected = {
                            currentFilterImpact = it
                            session.recordFilterChanged()
                            actionTicker++
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Filtered and sorted tasks
                    val filteredTasks = sortTasks(
                        applyFilters(
                            tasks = tasks,
                            type = currentFilterType,
                            urgency = currentFilterUrgency,
                            impact = currentFilterImpact
                        )
                    )

                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        TaskList(
                            tasks = filteredTasks,
                            onTaskClick = { clickedTask ->
                                editingTask = clickedTask
                                showPopup = true
                            },
                            onToggleStatus = { task ->
                                val index = tasks.indexOf(task)
                                if (index != -1) {
                                    val nextType = when (task.type) {
                                        TaskType.Inactive -> TaskType.Active
                                        TaskType.Active -> TaskType.Done
                                        TaskType.Done -> TaskType.Inactive
                                        else -> task.type
                                    }
                                    tasks[index] = task.copy(type = nextType)
                                    session.recordStatusToggled()
                                    actionTicker++
                                    onTasksUpdated(tasks.toList())
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bottom Status & Summary Bar
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF141414),
                        border = BorderStroke(1.dp, AppTextColor.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Total actions: ${session.totalActions.get()} | Duration: ${session.formatDuration()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppTextColor
                                )
                                Text(
                                    text = "Total tasks: ${tasks.size} (shown: ${filteredTasks.size})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AppTextColor.copy(alpha = 0.7f)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { showSummaryDialog = true },
                                    border = BorderStroke(1.dp, AppTextColor),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppTextColor)
                                ) {
                                    Text("Summary")
                                }

                                Button(
                                    onClick = onExitRequested,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFBD1CE3),
                                        contentColor = AppTextColor
                                    )
                                ) {
                                    Text("End program")
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showPopup) {
            AddTaskBar(
                initialTask = editingTask,
                onConfirm = { taskResult ->
                    if (editingTask != null) {
                        val index = tasks.indexOf(editingTask)
                        if (index != -1) {
                            tasks[index] = taskResult
                            session.recordTaskEdited()
                        }
                    } else {
                        tasks.add(taskResult)
                        session.recordTaskCreated()
                    }
                    actionTicker++
                    onTasksUpdated(tasks.toList())
                    showPopup = false
                    editingTask = null
                },
                onDismiss = {
                    showPopup = false
                    editingTask = null
                },
                onDelete = { taskToDelete ->
                    tasks.remove(taskToDelete)
                    session.recordTaskDeleted()
                    actionTicker++
                    onTasksUpdated(tasks.toList())
                    showPopup = false
                    editingTask = null
                }
            )
        }

        if (showSummaryDialog) {
            AlertDialog(
                onDismissRequest = { showSummaryDialog = false },
                containerColor = Color(0xFF161616),
                titleContentColor = AppTextColor,
                textContentColor = AppTextColor,
                shape = RoundedCornerShape(8.dp),
                title = {
                    Text("Program results summary", fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("• Session duration: ${session.formatDuration()}")
                        Text("• Total actions count: ${session.totalActions.get()}")
                        Text("   - Created tasks: ${session.tasksCreated.get()}")
                        Text("   - Edited tasks: ${session.tasksEdited.get()}")
                        Text("   - Deleted tasks: ${session.tasksDeleted.get()}")
                        Text("   - Changed statuses: ${session.statusToggled.get()}")
                        Text("   - Changed filters: ${session.filtersChanged.get()}")
                        Spacer(Modifier.height(4.dp))
                        Text("• Total tasks count: ${tasks.size}")
                        Text("After program ending a summary will also be written in console.")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showSummaryDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBD1CE3),
                            contentColor = AppTextColor
                        )
                    ) {
                        Text("Okay")
                    }
                }
            )
        }
    }
}

fun applyFilters(
    tasks: List<Task>,
    type: String,
    urgency: String,
    impact: String
): List<Task> {
    return tasks.filter { task ->
        val matchesType = when (type) {
            "Active" -> task.type == TaskType.Active
            "Done" -> task.type == TaskType.Done
            "Inactive" -> task.type == TaskType.Inactive
            else -> true
        }
        val matchesUrgency = when (urgency) {
            "Low" -> task.urgency == TaskUrgency.Low
            "Moderate" -> task.urgency == TaskUrgency.Moderate
            "High" -> task.urgency == TaskUrgency.High
            "Immediate" -> task.urgency == TaskUrgency.Immediate
            else -> true
        }
        val matchesImpact = when (impact) {
            "Low" -> task.impact == TaskImpact.Low
            "Moderate" -> task.impact == TaskImpact.Moderate
            "High" -> task.impact == TaskImpact.High
            "Critical" -> task.impact == TaskImpact.Critical
            else -> true
        }
        matchesType && matchesUrgency && matchesImpact
    }
}

fun sortTasks(tasks: List<Task>): List<Task> {
    return tasks.sortedWith(
        compareBy<Task> { it.priority.ordinal }
            .thenByDescending { it.urgency.ordinal }
            .thenByDescending { it.impact.ordinal }
    )
}