package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Language
import com.example.model.Localization
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DisclaimerDialog(
    language: Language,
    onDismiss: () -> Unit = {},
    onAccept: (userName: String, device: String, ip: String, lat: Double, lng: Double, locName: String) -> Unit
) {
    val mandatoryRules = listOf(
        "I agree to all PG Rules & Regulations",
        "I understand and accept management policies",
        "Personal belongings remain my sole responsibility",
        "Vehicle parking is strictly at my own risk",
        "Visitor & guest activities are my responsibility",
        "I will comply with community & silent hour guidelines",
        "I consent to digital communication (WhatsApp / SMS / App)",
        "I acknowledge fire, medical & emergency procedures",
        "I agree to all platform terms, GST policies and conditions"
    )

    val checkedStates = remember { mutableStateListOf(*Array(mandatoryRules.size) { true }) }
    val allChecked = checkedStates.all { it }

    val currentDate = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()) }
    val currentTime = remember { SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date()) }
    val deviceModel = remember { "${android.os.Build.MANUFACTURER.uppercase()} ${android.os.Build.MODEL}" }
    val ipAddress = "192.168.1.108 (Public: 49.37.12.84)"
    val lat = 12.9141
    val lng = 77.6411
    val locName = "HSR Layout Sector 2, Bengaluru"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Navy900,
            tonalElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Close Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Indigo600, Cyan400))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF94A3B8)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = Localization.get("disclaimer_title", language),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = Localization.get("disclaimer_desc", language),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF94A3B8),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Mandatory Telemetry Audit Box
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3364748B))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "🔐 DIGITAL AUDIT & STATUTORY AUDIT TRAIL",
                            style = MaterialTheme.typography.labelSmall,
                            color = Cyan400,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Tenant: Aarav Sharma (PG-T001)", style = MaterialTheme.typography.bodySmall, color = Color.White)
                            Text("Room 101-A", style = MaterialTheme.typography.bodySmall, color = Emerald400, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Device: $deviceModel", style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1))
                            Text("IP: $ipAddress", style = MaterialTheme.typography.bodySmall, color = Color(0xFFCBD5E1))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GPS: $lat, $lng ($locName)", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA5B4FC))
                            Text("$currentDate $currentTime", style = MaterialTheme.typography.bodySmall, color = Color(0xFFA5B4FC))
                        }
                    }
                }

                // Checkboxes list
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    mandatoryRules.forEachIndexed { index, rule ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (checkedStates[index]) Color(0x2210B981) else Color(0x11FFFFFF))
                                .clickable { checkedStates[index] = !checkedStates[index] }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkedStates[index],
                                onCheckedChange = { checkedStates[index] = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Emerald500,
                                    uncheckedColor = Color(0xFF64748B),
                                    checkmarkColor = Navy900
                                ),
                                modifier = Modifier.testTag("disclaimer_checkbox_$index")
                            )
                            Text(
                                text = rule,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (checkedStates[index]) Color.White else Color(0xFFCBD5E1),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }

                // Select All Quick Action
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${checkedStates.count { it }} / ${mandatoryRules.size} Accepted",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (allChecked) Emerald400 else Color(0xFFFBBF24),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Accept All Terms",
                        style = MaterialTheme.typography.labelMedium,
                        color = Cyan400,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable {
                                for (i in checkedStates.indices) {
                                    checkedStates[i] = true
                                }
                            }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (allChecked) {
                            onAccept("Aarav Sharma", deviceModel, ipAddress, lat, lng, locName)
                        }
                    },
                    enabled = allChecked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("accept_disclaimer_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo600,
                        disabledContainerColor = Color(0xFF334155),
                        contentColor = Color.White,
                        disabledContentColor = Color(0xFF64748B)
                    )
                ) {
                    Icon(
                        imageVector = if (allChecked) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (allChecked) Localization.get("accept_continue", language) else "Select all 9 checkboxes to continue",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
