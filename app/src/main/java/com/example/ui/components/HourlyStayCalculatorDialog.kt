package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.StayType
import com.example.ui.theme.Amber400
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900

@Composable
fun HourlyStayCalculatorDialog(
    onDismiss: () -> Unit
) {
    var selectedStayType by remember { mutableStateOf(StayType.HOURLY) }
    var durationCount by remember { mutableFloatStateOf(4f) }

    val baseRate = selectedStayType.baseRate
    val subTotal = baseRate * durationCount.toInt()
    val gstAmount = subTotal * 0.12 // 12% GST
    val cleaningFee = if (selectedStayType == StayType.HOURLY || selectedStayType == StayType.DAILY) 150.0 else 500.0
    val securityDeposit = when (selectedStayType) {
        StayType.HOURLY -> 0.0
        StayType.DAILY -> 500.0
        StayType.WEEKLY -> 2000.0
        StayType.MONTHLY -> 10000.0
    }
    val totalPayable = subTotal + gstAmount + cleaningFee + securityDeposit

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Calculate, contentDescription = "Calculator", tint = Cyan400, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Flexible Stay Rate Calculator",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stay Type Tabs
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    StayType.values().forEach { st ->
                        val isSel = selectedStayType == st
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) Indigo600 else Navy800)
                                .clickable {
                                    selectedStayType = st
                                    durationCount = when (st) {
                                        StayType.HOURLY -> 4f
                                        StayType.DAILY -> 3f
                                        StayType.WEEKLY -> 1f
                                        StayType.MONTHLY -> 1f
                                    }
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (st) {
                                    StayType.HOURLY -> "Hourly"
                                    StayType.DAILY -> "Daily"
                                    StayType.WEEKLY -> "Weekly"
                                    StayType.MONTHLY -> "Monthly"
                                },
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Duration Slider
                val (maxDuration, unitName) = when (selectedStayType) {
                    StayType.HOURLY -> 24f to "Hours"
                    StayType.DAILY -> 30f to "Days"
                    StayType.WEEKLY -> 8f to "Weeks"
                    StayType.MONTHLY -> 12f to "Months"
                }

                Text(
                    text = "Selected Duration: ${durationCount.toInt()} $unitName",
                    style = MaterialTheme.typography.bodySmall,
                    color = Cyan400,
                    fontWeight = FontWeight.Bold
                )

                Slider(
                    value = durationCount,
                    onValueChange = { durationCount = it },
                    valueRange = 1f..maxDuration,
                    steps = (maxDuration.toInt() - 2).coerceAtLeast(0),
                    colors = SliderDefaults.colors(
                        thumbColor = Cyan400,
                        activeTrackColor = Indigo600,
                        inactiveTrackColor = Navy800
                    )
                )

                // Cost Breakdown Table
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Base Tariff (${durationCount.toInt()} x ₹${baseRate.toInt()}):", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                            Text("₹${subTotal.toInt()}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GST (12% Statutory):", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                            Text("₹${gstAmount.toInt()}", color = Color.White, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Sanitization & Linen:", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                            Text("₹${cleaningFee.toInt()}", color = Color.White, fontSize = 12.sp)
                        }
                        if (securityDeposit > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Refundable Security Deposit:", color = Amber400, fontSize = 12.sp)
                                Text("₹${securityDeposit.toInt()}", color = Amber400, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x3364748B)))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Payable Estimate:", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("₹${totalPayable.toInt()}", color = Emerald400, fontSize = 15.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Instant Book Stay (₹${totalPayable.toInt()})", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
