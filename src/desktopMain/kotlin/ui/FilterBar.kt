package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterBar(
    selectedFilterType: String,
    selectedFilterUrgency: String,
    selectedFilterImpact: String,
    onFilterTypeSelected: (String) -> Unit,
    onFilterUrgencySelected: (String) -> Unit,
    onFilterImpactSelected: (String) -> Unit
) {
    var expandedType by remember { mutableStateOf(false) }
    var expandedUrgency by remember { mutableStateOf(false) }
    var expandedImpact by remember { mutableStateOf(false) }

    val accentColor = Color(0xFFFEC7FF)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Status Type filter
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { expandedType = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, accentColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
            ) {
                Text("Type: $selectedFilterType")
            }
            DropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false }
            ) {
                listOf("All", "Inactive", "Active", "Done").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onFilterTypeSelected(option)
                            expandedType = false
                        }
                    )
                }
            }
        }

        // Urgency filter
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { expandedUrgency = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, accentColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
            ) {
                Text("Urgency: $selectedFilterUrgency")
            }
            DropdownMenu(
                expanded = expandedUrgency,
                onDismissRequest = { expandedUrgency = false }
            ) {
                listOf("All", "Low", "Moderate", "High", "Immediate").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onFilterUrgencySelected(option)
                            expandedUrgency = false
                        }
                    )
                }
            }
        }

        // Impact filter
        Box(modifier = Modifier.weight(1f)) {
            OutlinedButton(
                onClick = { expandedImpact = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, accentColor),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
            ) {
                Text("Impact: $selectedFilterImpact")
            }
            DropdownMenu(
                expanded = expandedImpact,
                onDismissRequest = { expandedImpact = false }
            ) {
                listOf("All", "Low", "Moderate", "High", "Critical").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onFilterImpactSelected(option)
                            expandedImpact = false
                        }
                    )
                }
            }
        }
    }
}
