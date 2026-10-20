package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import model.Task
import model.TaskType

@Composable
fun TaskRow(
    task: Task,
    onClick: () -> Unit,
    onToggleStatus: () -> Unit
) {
    val accentColor = Color(0xFFFEC7FF)
    val priorityColor = task.priority.color

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, priorityColor.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF161616),
            contentColor = accentColor
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Priority Color Tag
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = priorityColor.copy(alpha = 0.22f),
                        border = BorderStroke(1.dp, priorityColor)
                    ) {
                        Text(
                            text = task.priority.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = priorityColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }

                if (task.desc.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = task.desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = accentColor.copy(alpha = 0.85f)
                    )
                }

                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Urgency: ${task.urgency.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.75f)
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.4f)
                    )
                    Text(
                        text = "Impact: ${task.impact.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.75f)
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.4f)
                    )
                    Text(
                        text = "Status: ${task.type.name}",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor.copy(alpha = 0.75f)
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            when (task.type) {
                TaskType.Active -> {
                    Button(
                        onClick = onToggleStatus,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8333CC),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Finish")
                    }
                }
                TaskType.Done -> {
                    Button(
                        onClick = onToggleStatus,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6A13EC),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Reinsert")
                    }
                }
                TaskType.Inactive -> {
                    Button(
                        onClick = onToggleStatus,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBD1CE3),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Begin")
                    }
                }
                else -> {}
            }
        }
    }
}
