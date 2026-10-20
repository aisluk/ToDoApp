package ui
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import model.Task
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement                                                            
import androidx.compose.ui.unit.dp   

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit = {},
    onToggleStatus: (Task) -> Unit = {}
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tasks) { task ->
            TaskRow(
                task = task,
                onClick = { onTaskClick(task) },
                onToggleStatus = {onToggleStatus(task)}
            )
        }
    }
}
