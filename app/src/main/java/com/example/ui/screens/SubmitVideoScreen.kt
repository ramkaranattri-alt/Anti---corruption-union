package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.JusticeGold
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.UnionDarkNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitVideoScreen(
    title: String,
    department: String,
    location: String,
    description: String,
    targetOfficial: String,
    whistleblowerName: String,
    isAnonymous: Boolean,
    isVideoSelected: Boolean,
    onTitleChange: (String) -> Unit,
    onDeptChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onDescChange: (String) -> Unit,
    onTargetOfficialChange: (String) -> Unit,
    onWhistleblowerNameChange: (String) -> Unit,
    onAnonymousToggle: (Boolean) -> Unit,
    onVideoSelectedToggle: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var deptExpanded by remember { mutableStateOf(false) }

    val departments = listOf(
        "Education & NEET",
        "Elections & ECI",
        "Infrastructure & Tenders",
        "Electoral Bonds & Finance",
        "Urban Development",
        "Police & Bureaucracy",
        "Healthcare & Hospitals"
    )

    // System Video Picker using Android Photo/Video Picker contract
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onVideoSelectedToggle(true)
            Toast.makeText(context, "Video attached successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("submit_video_screen")
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SaffronPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Submit Video Evidence",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Expose corruption, bribes, tender fraud & rigging",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Whistleblower Security Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = UnionDarkNavy),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = JusticeGold, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Whistleblower Protection Active",
                        style = MaterialTheme.typography.labelMedium.copy(color = JusticeGold, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Your submission is processed locally. Anonymous mode scrubs device identifiers, camera model, and geolocation.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFCBD5E1), fontSize = 11.sp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Video Attachment Box
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isVideoSelected) EmeraldGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = if (isVideoSelected) EmeraldGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    // Toggle or trigger media picker
                    onVideoSelectedToggle(!isVideoSelected)
                    if (!isVideoSelected) {
                        Toast.makeText(context, "Evidence video selected (24.8 MB)", Toast.LENGTH_SHORT).show()
                    }
                }
                .testTag("video_attachment_box")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (isVideoSelected) EmeraldGreen else SaffronPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isVideoSelected) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = if (isVideoSelected) Color.White else SaffronPrimary,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isVideoSelected) "Video Evidence Attached (video_leak_evidence.mp4)" else "Tap to Select Video or Record Evidence",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isVideoSelected) EmeraldGreen else MaterialTheme.colorScheme.onSurface
                    )
                )

                Text(
                    text = if (isVideoSelected) "Duration: 02:45 min • Tap to change" else "Supports MP4, MOV, MKV up to 500 MB",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Fields
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title of Expose *") },
            placeholder = { Text("e.g. Substandard Concrete in New Flyover / Bribe Demanded") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_report_title")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Department Dropdown
        ExposedDropdownMenuBox(
            expanded = deptExpanded,
            onExpandedChange = { deptExpanded = !deptExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = department,
                onValueChange = {},
                readOnly = true,
                label = { Text("Ministry / Department *") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deptExpanded) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("dropdown_department")
            )

            ExposedDropdownMenu(
                expanded = deptExpanded,
                onDismissRequest = { deptExpanded = false }
            ) {
                departments.forEach { dept ->
                    DropdownMenuItem(
                        text = { Text(dept) },
                        onClick = {
                            onDeptChange(dept)
                            deptExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Location
        OutlinedTextField(
            value = location,
            onValueChange = onLocationChange,
            label = { Text("Location (City, District & State) *") },
            placeholder = { Text("e.g. Patna, Bihar or Surat, Gujarat") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_report_location")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Implicated Officials
        OutlinedTextField(
            value = targetOfficial,
            onValueChange = onTargetOfficialChange,
            label = { Text("Implicated Ministry / Official / Contractor") },
            placeholder = { Text("e.g. Executive Engineer, PWD Division 4 / Local MLA") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_report_target_official")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = onDescChange,
            label = { Text("Details of Corruption / Irregularities *") },
            placeholder = { Text("Provide factual context: dates, monetary sums demanded, contractor names, paper leak modus operandi...") },
            minLines = 4,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_report_description")
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Anonymous Mode Toggle
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = EmeraldGreen)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Anonymous Whistleblower Mode",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isAnonymous) "Identity masked as 'Whistleblower #...'" else "Publish under your name",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }

                Switch(
                    checked = isAnonymous,
                    onCheckedChange = onAnonymousToggle,
                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen),
                    modifier = Modifier.testTag("toggle_anonymous")
                )
            }
        }

        if (!isAnonymous) {
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = whistleblowerName,
                onValueChange = onWhistleblowerNameChange,
                label = { Text("Your Name / Organization") },
                placeholder = { Text("e.g. Rajesh Sharma, Citizen Audit Group") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_whistleblower_name")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                if (title.isBlank() || description.isBlank()) {
                    Toast.makeText(context, "Please fill in title and description.", Toast.LENGTH_SHORT).show()
                } else {
                    onSubmit()
                    Toast.makeText(context, "Expose submitted to Indian Anti-Corruption Union!", Toast.LENGTH_LONG).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("publish_corruption_report_button")
        ) {
            Icon(imageVector = Icons.Default.Videocam, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Publish Expose to Union Feed",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}
