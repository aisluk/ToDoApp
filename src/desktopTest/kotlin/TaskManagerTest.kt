import model.AppSession
import model.Task
import model.TaskImpact
import model.TaskPriority
import model.TaskType
import model.TaskUrgency
import ui.applyFilters
import ui.sortTasks
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaskManagerTest {

    // 1. P1: Immediate Urgency and Critical Impact
    @Test
    fun testP1PriorityImmediateCritical() {
        val task = Task("Fix prod outage", "Server down", TaskType.Active, TaskUrgency.Immediate, TaskImpact.Critical)
        assertEquals(TaskPriority.P1, task.priority)
    }

    // 2. P1: Immediate Urgency and High Impact
    @Test
    fun testP1PriorityImmediateHigh() {
        val task = Task("Urgent client bug", "Urgent ticket", TaskType.Active, TaskUrgency.Immediate, TaskImpact.High)
        assertEquals(TaskPriority.P1, task.priority)
    }

    // 3. P1: High Urgency and Critical Impact
    @Test
    fun testP1PriorityHighCritical() {
        val task = Task("Security breach fix", "Patch CVE", TaskType.Active, TaskUrgency.High, TaskImpact.Critical)
        assertEquals(TaskPriority.P1, task.priority)
    }

    // 4. P2 Priority combinations
    @Test
    fun testP2PriorityCombinations() {
        val t1 = Task("T1", "", TaskType.Inactive, TaskUrgency.Moderate, TaskImpact.Critical)
        val t2 = Task("T2", "", TaskType.Inactive, TaskUrgency.Moderate, TaskImpact.High)
        val t3 = Task("T3", "", TaskType.Inactive, TaskUrgency.High, TaskImpact.High)
        val t4 = Task("T4", "", TaskType.Inactive, TaskUrgency.Immediate, TaskImpact.Moderate)

        assertEquals(TaskPriority.P2, t1.priority)
        assertEquals(TaskPriority.P2, t2.priority)
        assertEquals(TaskPriority.P2, t3.priority)
        assertEquals(TaskPriority.P2, t4.priority)
    }

    // 5. P3 Priority combinations
    @Test
    fun testP3PriorityCombinations() {
        val t1 = Task("T1", "", TaskType.Inactive, TaskUrgency.Low, TaskImpact.Critical)
        val t2 = Task("T2", "", TaskType.Inactive, TaskUrgency.Low, TaskImpact.High)
        val t3 = Task("T3", "", TaskType.Inactive, TaskUrgency.Moderate, TaskImpact.Moderate)
        val t4 = Task("T4", "", TaskType.Inactive, TaskUrgency.High, TaskImpact.Moderate)
        val t5 = Task("T5", "", TaskType.Inactive, TaskUrgency.Immediate, TaskImpact.Low)

        assertEquals(TaskPriority.P3, t1.priority)
        assertEquals(TaskPriority.P3, t2.priority)
        assertEquals(TaskPriority.P3, t3.priority)
        assertEquals(TaskPriority.P3, t4.priority)
        assertEquals(TaskPriority.P3, t5.priority)
    }

    // 6. P4 Priority combinations
    @Test
    fun testP4PriorityCombinations() {
        val t1 = Task("T1", "", TaskType.Inactive, TaskUrgency.Low, TaskImpact.Moderate)
        val t2 = Task("T2", "", TaskType.Inactive, TaskUrgency.Low, TaskImpact.Low)
        val t3 = Task("T3", "", TaskType.Inactive, TaskUrgency.Moderate, TaskImpact.Low)
        val t4 = Task("T4", "", TaskType.Inactive, TaskUrgency.High, TaskImpact.Low)

        assertEquals(TaskPriority.P4, t1.priority)
        assertEquals(TaskPriority.P4, t2.priority)
        assertEquals(TaskPriority.P4, t3.priority)
        assertEquals(TaskPriority.P4, t4.priority)
    }

    // 7. Filtering by Status Type
    @Test
    fun testApplyFiltersByType() {
        val tasks = listOf(
            Task("T1", "", TaskType.Active, TaskUrgency.Low, TaskImpact.Low),
            Task("T2", "", TaskType.Inactive, TaskUrgency.Low, TaskImpact.Low),
            Task("T3", "", TaskType.Done, TaskUrgency.Low, TaskImpact.Low)
        )

        val active = applyFilters(tasks, type = "Active", urgency = "All", impact = "All")
        assertEquals(1, active.size)
        assertEquals("T1", active[0].name)

        val done = applyFilters(tasks, type = "Done", urgency = "All", impact = "All")
        assertEquals(1, done.size)
        assertEquals("T3", done[0].name)

        val all = applyFilters(tasks, type = "All", urgency = "All", impact = "All")
        assertEquals(3, all.size)
    }

    // 8. Filtering by Urgency
    @Test
    fun testApplyFiltersByUrgency() {
        val tasks = listOf(
            Task("ImmediateTask", "", TaskType.Active, TaskUrgency.Immediate, TaskImpact.Low),
            Task("HighTask", "", TaskType.Active, TaskUrgency.High, TaskImpact.Low),
            Task("ModTask", "", TaskType.Active, TaskUrgency.Moderate, TaskImpact.Low),
            Task("LowTask", "", TaskType.Active, TaskUrgency.Low, TaskImpact.Low)
        )

        val immediateOnly = applyFilters(tasks, type = "All", urgency = "Immediate", impact = "All")
        assertEquals(1, immediateOnly.size)
        assertEquals("ImmediateTask", immediateOnly[0].name)

        val highOnly = applyFilters(tasks, type = "All", urgency = "High", impact = "All")
        assertEquals(1, highOnly.size)
        assertEquals("HighTask", highOnly[0].name)
    }

    // 9. Filtering by Impact
    @Test
    fun testApplyFiltersByImpact() {
        val tasks = listOf(
            Task("CritTask", "", TaskType.Active, TaskUrgency.Low, TaskImpact.Critical),
            Task("HighTask", "", TaskType.Active, TaskUrgency.Low, TaskImpact.High),
            Task("ModTask", "", TaskType.Active, TaskUrgency.Low, TaskImpact.Moderate),
            Task("LowTask", "", TaskType.Active, TaskUrgency.Low, TaskImpact.Low)
        )

        val critOnly = applyFilters(tasks, type = "All", urgency = "All", impact = "Critical")
        assertEquals(1, critOnly.size)
        assertEquals("CritTask", critOnly[0].name)
    }

    // 10. Combined Multi-Filter Filtering
    @Test
    fun testApplyFiltersCombined() {
        val tasks = listOf(
            Task("Match", "", TaskType.Active, TaskUrgency.Immediate, TaskImpact.Critical),
            Task("MismatchType", "", TaskType.Inactive, TaskUrgency.Immediate, TaskImpact.Critical),
            Task("MismatchUrgency", "", TaskType.Active, TaskUrgency.Low, TaskImpact.Critical),
            Task("MismatchImpact", "", TaskType.Active, TaskUrgency.Immediate, TaskImpact.Low)
        )

        val result = applyFilters(tasks, type = "Active", urgency = "Immediate", impact = "Critical")
        assertEquals(1, result.size)
        assertEquals("Match", result[0].name)
    }

    // 11. Sorting by Importance Order: Priority first (P1 -> P4), then Urgency desc, then Impact desc
    @Test
    fun testTaskSortingImportanceOrder() {
        val p4 = Task("P4_Low_Low", "", TaskType.Active, TaskUrgency.Low, TaskImpact.Low)
        val p3 = Task("P3_Mod_Mod", "", TaskType.Active, TaskUrgency.Moderate, TaskImpact.Moderate)
        val p2 = Task("P2_High_High", "", TaskType.Active, TaskUrgency.High, TaskImpact.High)
        val p1_a = Task("P1_Imm_High", "", TaskType.Active, TaskUrgency.Immediate, TaskImpact.High)
        val p1_b = Task("P1_Imm_Crit", "", TaskType.Active, TaskUrgency.Immediate, TaskImpact.Critical)
        val p1_c = Task("P1_High_Crit", "", TaskType.Active, TaskUrgency.High, TaskImpact.Critical)

        val unsorted = listOf(p4, p2, p1_a, p3, p1_c, p1_b)
        val sorted = sortTasks(unsorted)

        // P1 tasks should come first
        assertEquals(TaskPriority.P1, sorted[0].priority)
        assertEquals(TaskPriority.P1, sorted[1].priority)
        assertEquals(TaskPriority.P1, sorted[2].priority)

        // Within P1: Immediate comes before High urgency. For same urgency, Critical comes before High impact.
        assertEquals("P1_Imm_Crit", sorted[0].name)
        assertEquals("P1_Imm_High", sorted[1].name)
        assertEquals("P1_High_Crit", sorted[2].name)

        // Then P2, P3, P4
        assertEquals(TaskPriority.P2, sorted[3].priority)
        assertEquals(TaskPriority.P3, sorted[4].priority)
        assertEquals(TaskPriority.P4, sorted[5].priority)
    }

    // 12. AppSession Action Tracking
    @Test
    fun testAppSessionActionTracking() {
        val session = AppSession()
        assertEquals(0, session.totalActions.get())

        session.recordTaskCreated()
        session.recordTaskEdited()
        session.recordTaskDeleted()
        session.recordStatusToggled()
        session.recordFilterChanged()

        assertEquals(5, session.totalActions.get())
        assertEquals(1, session.tasksCreated.get())
        assertEquals(1, session.tasksEdited.get())
        assertEquals(1, session.tasksDeleted.get())
        assertEquals(1, session.statusToggled.get())
        assertEquals(1, session.filtersChanged.get())
    }

    // 13. AppSession Duration Formatting & TUI Summary Generation
    @Test
    fun testAppSessionDurationAndTuiGeneration() {
        val session = AppSession(startTimeMs = System.currentTimeMillis() - 75000) // 75 seconds ago
        val duration = session.formatDuration()
        assertTrue(duration.contains("m") || duration.contains("s"))

        val tasks = listOf(
            Task("Important Task", "Must do", TaskType.Active, TaskUrgency.Immediate, TaskImpact.Critical)
        )
        val tui = session.generateTuiSummary(tasks)

        assertTrue(tui.contains("PROGRAM RESULTS SUMMARY"))
        assertTrue(tui.contains("Important Task"))
        assertTrue(tui.contains("P1"))
        assertTrue(tui.contains("Immediate"))
        assertTrue(tui.contains("Critical"))
    }
}
