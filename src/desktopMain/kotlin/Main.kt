import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import model.AppSession
import model.Task
import ui.App

fun main() = application {
    val session = remember { AppSession() }
    var currentTasks = remember { mutableListOf<Task>() }

    fun exitWithSummary() {
        session.printTuiSummary(currentTasks)
        exitApplication()
    }

    Window(
        onCloseRequest = ::exitWithSummary,
        title = "Task Manager"
    ) {
        App(
            session = session,
            onTasksUpdated = { updatedList ->
                currentTasks = updatedList.toMutableList()
            },
            onExitRequested = ::exitWithSummary
        )
    }
}
