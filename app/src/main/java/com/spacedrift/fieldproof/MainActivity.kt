
package com.spacedrift.fieldproof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ChecklistItem(
    val title: String,
    val required: Boolean,
    val complete: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FieldProofTheme {
                InspectionScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InspectionScreen() {

    var photoCaptured by remember { mutableStateOf(false) }
    var noteAdded by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }

    var items by remember {
        mutableStateOf(
            listOf(
                ChecklistItem("Equipment condition", true),
                ChecklistItem("Safety signage", true),
                ChecklistItem("Access area", true),
                ChecklistItem("Additional observations", false)
            )
        )
    }

    val requiredItems = items.filter { it.required }
    val completedRequired = requiredItems.count { it.complete }
    val progress = completedRequired.toFloat() / requiredItems.size

    val readyToSubmit =
        completedRequired == requiredItems.size &&
                photoCaptured &&
                noteAdded

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "FieldProof",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Inspection #FP-1001",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                actions = {
                    Icon(
                        imageVector = Icons.Outlined.CloudOff,
                        contentDescription = "Offline"
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Offline status

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CloudOff,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Working offline",
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Changes are saved on this device",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Inspection header

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Facility inspection",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Main equipment area",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        FilterChip(
                            selected = !submitted,
                            onClick = {},
                            label = {
                                Text(
                                    if (submitted) "Submitted" else "Draft"
                                )
                            }
                        )
                    }

                    Divider()

                    Text(
                        text = "$completedRequired of ${requiredItems.size} required checks complete",
                        fontWeight = FontWeight.SemiBold
                    )

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
            }

            // Checklist

            Text(
                text = "Inspection checklist",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {

                    items.forEachIndexed { index, item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = item.complete,
                                onCheckedChange = { checked ->

                                    items = items.toMutableList().also {
                                        it[index] = item.copy(
                                            complete = checked
                                        )
                                    }
                                }
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = item.title,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = if (item.required)
                                        "Required"
                                    else
                                        "Optional",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            if (item.complete) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "Complete",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        if (index < items.lastIndex) {
                            Divider()
                        }
                    }
                }
            }

            // Evidence

            Text(
                text = "Evidence",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            EvidenceCard(
                icon = Icons.Outlined.CameraAlt,
                title = "Photo evidence",
                description = if (photoCaptured)
                    "1 photo captured"
                else
                    "Required before submission",
                complete = photoCaptured,
                buttonText = if (photoCaptured)
                    "Retake photo"
                else
                    "Capture photo",
                onClick = {
                    photoCaptured = !photoCaptured
                }
            )

            EvidenceCard(
                icon = Icons.Outlined.NoteAdd,
                title = "Inspection notes",
                description = if (noteAdded)
                    "Inspection note added"
                else
                    "Add observations or findings",
                complete = noteAdded,
                buttonText = if (noteAdded)
                    "Edit note"
                else
                    "Add note",
                onClick = {
                    noteAdded = !noteAdded
                }
            )

            // Readiness

            if (!readyToSubmit && !submitted) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Inspection not ready",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Complete all required checks and evidence."
                            )
                        }
                    }
                }
            }

            // Submit

            Button(
                onClick = {
                    submitted = true
                },
                enabled = readyToSubmit && !submitted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Description,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (submitted)
                        "Inspection submitted"
                    else
                        "Submit inspection"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun EvidenceCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    complete: Boolean,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (complete) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Complete"
                    )
                }
            }

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(buttonText)
            }
        }
    }
}

@Composable
fun FieldProofTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
}
