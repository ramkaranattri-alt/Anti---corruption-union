package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.SatireMemeEntity
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.JusticeGold
import com.example.ui.theme.SaffronPrimary
import com.example.ui.theme.UnionDarkNavy
import kotlinx.coroutines.launch

@Composable
fun SatireMemesScreen(
    memes: List<SatireMemeEntity>,
    onPlayAudio: (SatireMemeEntity) -> Unit,
    onLaugh: (Long) -> Unit,
    onAntiGyaneshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("satire_memes_screen")
    ) {
        // Satire Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(UnionDarkNavy, Color(0xFF162534))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🎭", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "POLITICAL SATIRE & SOUNDBOARD",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = SaffronPrimary,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "Laughter is Resistance",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Because when education ministers send children abroad and leaders dodge unscripted questions, Indian citizens fight back with unsparing political humor.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFCBD5E1),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Quick shortcut to Anti Gyanesh Kumar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.12f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = AlertRed)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Looking for election watchdog? Check the Anti Gyanesh Kumar hub.",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                        )
                    }

                    Button(
                        onClick = onAntiGyaneshClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("satire_anti_gyanesh_btn")
                    ) {
                        Text("ANTI GYANESH", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // List of Memes & Audio Cards
        items(memes, key = { it.id }) { meme ->
            MemeCard(
                meme = meme,
                onPlayAudio = { onPlayAudio(meme) },
                onLaugh = { onLaugh(meme.id) },
                onCopy = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(
                        ClipData.newPlainText("Satire Quote", "\"${meme.quote}\" - ${meme.speaker} (${meme.role})")
                    )
                    Toast.makeText(context, "Quote copied to clipboard!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun MemeCard(
    meme: SatireMemeEntity,
    onPlayAudio: () -> Unit,
    onLaugh: () -> Unit,
    onCopy: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    val isDharmendra = meme.speaker.contains("Dharmendra", ignoreCase = true)
    val isModi = meme.speaker.contains("Modi", ignoreCase = true)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDharmendra || isModi) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDharmendra || isModi) 4.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("meme_card_${meme.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Speaker Info Header
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
                            .background(
                                if (isModi) SaffronPrimary.copy(alpha = 0.2f)
                                else if (isDharmendra) AlertRed.copy(alpha = 0.15f)
                                else UnionDarkNavy.copy(alpha = 0.1f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = meme.avatarEmoji, fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = meme.speaker,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isModi) SaffronPrimary else if (isDharmendra) AlertRed else MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = meme.role,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (isModi) SaffronPrimary.copy(alpha = 0.15f)
                            else if (isDharmendra) AlertRed.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = meme.category,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isModi) SaffronPrimary else if (isDharmendra) AlertRed else MaterialTheme.colorScheme.onSurface,
                            fontSize = 9.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // The Quote Box
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isModi) Color(0xFFFFF7ED)
                    else if (isDharmendra) Color(0xFFFEF2F2)
                    else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "“${meme.quote}”",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 18.sp,
                            color = if (isModi) Color(0xFF9A3412)
                            else if (isDharmendra) Color(0xFF991B1B)
                            else MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Context: ${meme.context}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Actions: Voice Player, Laugh Counter, Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Audio Voice Speech Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            scale.animateTo(1.15f, animationSpec = tween(100))
                            scale.animateTo(1f, animationSpec = tween(100))
                        }
                        onPlayAudio()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isModi) SaffronPrimary else if (isDharmendra) AlertRed else UnionDarkNavy,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .scale(scale.value)
                        .testTag("hear_voice_button_${meme.id}")
                ) {
                    Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Play Voice", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isModi) "Hear \"Hello Frends\" 🎙️"
                        else if (isDharmendra) "Hear Dharmendra Quote 🔊"
                        else "Hear Voice 🔊",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Laugh Counter Button
                    OutlinedButton(
                        onClick = onLaugh,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("laugh_button_${meme.id}")
                    ) {
                        Text(text = "😂", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${meme.laughsCount}",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Copy Quote Button
                    OutlinedButton(
                        onClick = onCopy,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("copy_quote_button_${meme.id}")
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Quote", modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}
