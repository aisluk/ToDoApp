package model

import java.util.concurrent.atomic.AtomicInteger

class AppSession(val startTimeMs: Long = System.currentTimeMillis()) {
    val totalActions = AtomicInteger(0)
    val tasksCreated = AtomicInteger(0)
    val tasksEdited = AtomicInteger(0)
    val tasksDeleted = AtomicInteger(0)
    val statusToggled = AtomicInteger(0)
    val filtersChanged = AtomicInteger(0)

    fun recordTaskCreated() {
        totalActions.incrementAndGet()
        tasksCreated.incrementAndGet()
    }

    fun recordTaskEdited() {
        totalActions.incrementAndGet()
        tasksEdited.incrementAndGet()
    }

    fun recordTaskDeleted() {
        totalActions.incrementAndGet()
        tasksDeleted.incrementAndGet()
    }

    fun recordStatusToggled() {
        totalActions.incrementAndGet()
        statusToggled.incrementAndGet()
    }

    fun recordFilterChanged() {
        totalActions.incrementAndGet()
        filtersChanged.incrementAndGet()
    }

    fun getDurationMs(): Long = System.currentTimeMillis() - startTimeMs

    fun formatDuration(durationMs: Long = getDurationMs()): String {
        val totalSeconds = durationMs / 1000.0
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        return if (minutes > 0) {
            String.format("%dm %02ds (%.2fs)", minutes, seconds, totalSeconds)
        } else {
            String.format("%.2fs", totalSeconds)
        }
    }

    fun generateTuiSummary(tasks: List<Task>): String {
        val durationStr = formatDuration()
        val actions = totalActions.get()

        val sb = StringBuilder()
        sb.appendLine()
        sb.appendLine("------------------------------------------------------------------------------------------------------------")
        sb.appendLine("|                                         PROGRAM RESULTS SUMMARY                                          |")
        sb.appendLine("|----------------------------------------------------------------------------------------------------------|")
        sb.appendLine("|  Program runtime:          %-78s|".format(durationStr))
        sb.appendLine("|  Actions count:            %-78d|".format(actions))
        sb.appendLine("|      Created tasks:        %-78d|".format(tasksCreated.get()))
        sb.appendLine("|      Edited tasks:         %-78d|".format(tasksEdited.get()))
        sb.appendLine("|      Deleted tasks:        %-78d|".format(tasksDeleted.get()))
        sb.appendLine("|      Changed statuses:     %-78d|".format(statusToggled.get()))
        sb.appendLine("|      Changed filters:      %-78d|".format(filtersChanged.get()))
        sb.appendLine("|----------------------------------------------------------------------------------------------------------|")
        sb.appendLine("|                                             TASKS LIST (%-3d)                                            |".format(tasks.size))
        sb.appendLine("|----------------------------------------------------------------------------------------------------------|")
        sb.appendLine("| Nr. | Name                           | Priority    | Urgency         | Impact          | Status          |")
        sb.appendLine("|----------------------------------------------------------------------------------------------------------|")

        if (tasks.isEmpty()) {
            sb.appendLine("| -   | (no tasks)                     | -           | -               | -               | -               |")
        } else {
            tasks.forEachIndexed { index, task ->
                val num = (index + 1).toString().padEnd(3)
                val rawName = task.name
                val name = (if (rawName.length > 30) rawName.take(27) + "..." else rawName).padEnd(30)
                val priority = task.priority.label.padEnd(11)
                val urgency = task.urgency.name.padEnd(15)
                val impact = task.impact.name.padEnd(15)
                val status = task.type.name.padEnd(15)
                sb.appendLine("| $num | $name | $priority | $urgency | $impact | $status |")
            }
        }
        sb.appendLine("------------------------------------------------------------------------------------------------------------")
        sb.appendLine()
        return sb.toString()
    }

    fun printTuiSummary(tasks: List<Task>) {
        print(generateTuiSummary(tasks))
    }
}
