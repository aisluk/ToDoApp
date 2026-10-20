package model

import androidx.compose.ui.graphics.Color

enum class TaskType {
    Inactive, Active, Done, Deleted
}

enum class TaskUrgency {
    Low, Moderate, High, Immediate
}

enum class TaskImpact {
    Low, Moderate, High, Critical
}

enum class TaskPriority(val label: String, val color: Color) {
    P1("P1", Color(0xFFFF4D4D)),   // Red
    P2("P2", Color(0xFFFF9800)),   // Orange
    P3("P3", Color(0xFFFFEB3B)),   // Yellow
    P4("P4", Color(0xFF4CAF50))    // Green
}

data class Task(
    val name: String,
    val desc: String,
    val type: TaskType,
    val urgency: TaskUrgency,
    val impact: TaskImpact
) {
    val priority: TaskPriority
        get() = when {
            // P1 (Red) - High Urgency and Critical Impact, Immediate Urgency and Critical Impact, Immediate Urgency and High Impact
            (urgency == TaskUrgency.High && impact == TaskImpact.Critical) ||
            (urgency == TaskUrgency.Immediate && impact == TaskImpact.Critical) ||
            (urgency == TaskUrgency.Immediate && impact == TaskImpact.High) -> TaskPriority.P1

            // P2 (orange) - Moderate Urgency and Critical Impact, Moderate Urgency and High Impact, High Urgency and High Impact, Immediate Urgency and Moderate Impact
            (urgency == TaskUrgency.Moderate && impact == TaskImpact.Critical) ||
            (urgency == TaskUrgency.Moderate && impact == TaskImpact.High) ||
            (urgency == TaskUrgency.High && impact == TaskImpact.High) ||
            (urgency == TaskUrgency.Immediate && impact == TaskImpact.Moderate) -> TaskPriority.P2

            // P3 (Yellow) - Low Urgency and Critical Impact, Low Urgency and High Impact, Moderate Urgency and Moderate Impact, High Urgency and Moderate Impact, Immediate Urgency and Low Impact
            (urgency == TaskUrgency.Low && impact == TaskImpact.Critical) ||
            (urgency == TaskUrgency.Low && impact == TaskImpact.High) ||
            (urgency == TaskUrgency.Moderate && impact == TaskImpact.Moderate) ||
            (urgency == TaskUrgency.High && impact == TaskImpact.Moderate) ||
            (urgency == TaskUrgency.Immediate && impact == TaskImpact.Low) -> TaskPriority.P3

            // P4 (Green) - Low Urgency and Moderate Impact, Low Urgency and Low Impact, Moderate Urgency and Low Impact, High Urgency and Low Impact
            else -> TaskPriority.P4
        }
}
