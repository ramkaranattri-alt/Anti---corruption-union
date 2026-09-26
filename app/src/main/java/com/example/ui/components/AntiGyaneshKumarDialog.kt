package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.JusticeGold
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.UnionDarkNavy

@Composable
fun AntiGyaneshKumarDialog(
    signaturesCount: Int,
    hasSigned: Boolean,
    onSignPetition: () -> Unit,
    onDismiss: () -> Unit,
    onReportEciIssue: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AlertRed.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AlertRed,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ANTI GYANESH KUMAR",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AlertRed
                                )
                            )
                            Text(
                                text = "ECI Independence & Accountability Watchdog",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_anti_gyanesh_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Citizen Trust & Independence Score
                Card(
                    colors = CardDefaults.cardColors(containerColor = UnionDarkNavy),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Election Commission Trust Score",
                                style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF94A3B8))
                            )
                            Text(
                                text = "18% (CRITICAL)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = AlertRed,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.18f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AlertRed,
                            trackColor = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Audited by Indian Anti-Corruption Union citizens based on transparency and appointment neutrality.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4 Pillars of Concern
                Text(
                    text = "Core Violations & Grievances",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ConcernItem(
                    icon = Icons.Default.Gavel,
                    title = "1. Supreme Court CJI Excluded from Selection Panel",
                    detail = "Parliamentary law of 2023 removed the Chief Justice of India from the selection committee, replacing the judge with a cabinet minister to ensure a 2-1 government majority in picking Gyanesh Kumar."
                )

                ConcernItem(
                    icon = Icons.Default.HowToVote,
                    title = "2. Voter Turnout Data Delays & Form 17C Suppression",
                    detail = "ECI refused to immediately publish exact booth-level voter turnout numbers (Form 17C). Final phase revisions showed unexplained jumps of over 1.07 crore votes across constituencies."
                )

                ConcernItem(
                    icon = Icons.Default.Security,
                    title = "3. Selective Enforcement of Model Code of Conduct",
                    detail = "Ruling party leaders faced zero disqualifications or FIRs for inflammatory campaign speeches, while opposition leaders received notices within hours."
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action 1: Sign the Petition
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (hasSigned) EmeraldGreen.copy(alpha = 0.12f) else SaffronPrimary.copy(alpha = 0.12f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (hasSigned) Icons.Default.CheckCircle else Icons.Default.HowToVote,
                                contentDescription = null,
                                tint = if (hasSigned) EmeraldGreen else SaffronPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Citizen Petition: Restore ECI Independence",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Demanding the restoration of CJI to the selection panel and immediate real-time disclosure of Form 17C at closing of polls.",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✊ $signaturesCount Citizens Signed",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasSigned) EmeraldGreen else SaffronPrimary
                                )
                            )

                            Button(
                                onClick = onSignPetition,
                                enabled = !hasSigned,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hasSigned) EmeraldGreen else SaffronPrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("sign_anti_gyanesh_petition_button")
                            ) {
                                Text(if (hasSigned) "Signed ✓" else "Sign Petition")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action 2: Copy RTI Form 17C Query
                OutlinedButton(
                    onClick = {
                        val rtiDraft = """
To: Public Information Officer, Election Commission of India (Nirvachan Sadan, New Delhi)
Subject: RTI Application under Section 6(1) of RTI Act 2005 - Form 17C Polling Booth Records

Respected Sir/Madam,
Under the RTI Act 2005, please furnish the following information:
1. Certified copy of Form 17C (Part I - Account of Votes Recorded) for all polling stations in the recent parliamentary/assembly election.
2. Exact date and timestamp of the first turnout percentage broadcast versus final consolidated turnout percentage.
3. Details of all complaints received regarding EVM/VVPAT malfunction and corresponding replacement machine serial numbers.

Applicant: Citizen of India / Member of Indian Anti-Corruption Union (IACU)
                        """.trimIndent()
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("ECI RTI Draft", rtiDraft))
                        Toast.makeText(context, "RTI Draft copied to clipboard!", Toast.LENGTH_LONG).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("copy_eci_rti_button")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Copy Pre-drafted ECI RTI Query")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action 3: Report Election Malpractice
                Button(
                    onClick = {
                        onDismiss()
                        onReportEciIssue()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnionDarkNavy,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_eci_video_report_button")
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = JusticeGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Report Election Corruption / Send Video")
                }
            }
        }
    }
}

@Composable
private fun ConcernItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    detail: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AlertRed,
                modifier = Modifier
                    .size(20.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}
