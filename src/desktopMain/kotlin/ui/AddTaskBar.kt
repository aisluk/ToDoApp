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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Task
import model.TaskImpact
import model.TaskType
import model.TaskUrgency

@Composable
fun AddTaskBar(
    initialTask: Task? = null,
    onConfirm: (Task) -> Unit,
    onDismiss: () -> Unit,
    onDelete: (Task) -> Unit = {}
) {
    var name by remember { mutableStateOf(initialTask?.name ?: "") }
    var desc by remember { mutableStateOf(initialTask?.desc ?: "") }
    var urgency by remember { mutableStateOf(initialTask?.urgency ?: TaskUrgency.Low) }
    var impact by remember { mutableStateOf(initialTask?.impact ?: TaskImpact.Low) }

    var urgencyExpanded by remember { mutableStateOf(false) }
    var impactExpanded by remember { mutableStateOf(false) }

    val accentColor = Color(0xFFFEC7FF)
    val cardBackground = Color(0xFF1A1A1A)

    val previewTask = Task(
        name = name,
        desc = desc,
        type = initialTask?.type ?: TaskType.Inactive,
        urgency = urgency,
        impact = impact
    )
    val priority = previewTask.priority

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = cardBackground,
        titleContentColor = accentColor,
        textContentColor = accentColor,
        shape = RoundedCornerShape(8.dp),
        title = {
            Text(
                if (initialTask == null) "New Task" else "Edit Task",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = accentColor,
                        unfocusedTextColor = accentColor,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = accentColor.copy(alpha = 0.7f),
                        cursorColor = accentColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = accentColor,
                        unfocusedTextColor = accentColor,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = accentColor.copy(alpha = 0.5f),
                        focusedLabelColor = accentColor,
                        unfocusedLabelColor = accentColor.copy(alpha = 0.7f),
                        cursorColor = accentColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Urgency selector dropdown
                Column {
                    Text(
                        "Urgency",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { urgencyExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, accentColor),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(urgency.name)
                                Text("▼")
                            }
                        }
                        DropdownMenu(
                            expanded = urgencyExpanded,
                            onDismissRequest = { urgencyExpanded = false }
                        ) {
                            TaskUrgency.entries.forEach { u ->
                                DropdownMenuItem(
                                    text = { Text(u.name) },
                                    onClick = {
                                        urgency = u
                                        urgencyExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Impact selector dropdown
                Column {
                    Text(
                        "Impact",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { impactExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, accentColor),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(impact.name)
                                Text("▼")
                            }
                        }
                        DropdownMenu(
                            expanded = impactExpanded,
                            onDismissRequest = { impactExpanded = false }
                        ) {
                            TaskImpact.entries.forEach { imp ->
                                DropdownMenuItem(
                                    text = { Text(imp.name) },
                                    onClick = {
                                        impact = imp
                                        impactExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Importance / Priority preview
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Calculated Priority:", style = MaterialTheme.typography.bodyMedium, color = accentColor)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = priority.color.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, priority.color)
                    ) {
                        Text(
                            text = "${priority.label} (${priority.name})",
                            color = priority.color,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newTask = Task(
                        name = name,
                        desc = desc,
                        type = initialTask?.type ?: TaskType.Inactive,
                        urgency = urgency,
                        impact = impact
                    )
                    onConfirm(newTask)
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBD1CE3),
                    contentColor = accentColor,
                    disabledContainerColor = Color(0xFF3B1E40),
                    disabledContentColor = accentColor.copy(alpha = 0.4f)
                )
            ) {
                Text(if (initialTask == null) "Add" else "Save")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (initialTask != null) {
                    TextButton(onClick = { onDelete(initialTask) }) {
                        Text("Delete", color = Color(0xFFFF4D4D))
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = accentColor)
                }
            }
        }
    )
}