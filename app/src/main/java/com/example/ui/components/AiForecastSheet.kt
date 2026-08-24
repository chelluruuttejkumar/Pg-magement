package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber400
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiForecastSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Navy900,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Cyan400, Indigo600))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.TrendingUp, contentDescription = "Analytics", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("AI Predictive Analytics & Forecast", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Machine Learning Yield & Revenue Intelligence", color = Cyan400, fontSize = 11.sp)
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forecast Cards
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🔮 90-DAY OCCUPANCY & VACANCY TRAJECTORY", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Current Occupancy", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("85.3%", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        }
                        Column {
                            Text("AI Forecasted (Next Month)", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("94.8% (+9.5%)", color = Emerald400, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Insight: High hiring momentum in surrounding IT parks (Outer Ring Rd & Hitec City) indicates 100% occupancy for 2-sharing & Private Studios by October.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3310B981))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("💰 REVENUE & COLLECTION FORECAST", color = Emerald400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Expected Collections", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("₹22.97 Lakhs", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        }
                        Column {
                            Text("Collection Confidence", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("98.2% on-time", color = Emerald400, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Estimated Net Operating Income (NOI): ₹16.45 Lakhs after electricity, chef kitchen catering, high-speed fiber & housekeeping overheads.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FBBF24))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⚡ AI PRICING & INVENTORY OPTIMIZATION", color = Amber400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Dynamic Pricing: Increase Studio 401 tariff from ₹24,000 to ₹26,500 due to premium rooftop balcony demand.\n• EV Charging: Adding 2 extra 7.4kW AC chargers will yield ₹18,000/month recurring utility margin.\n• Food Plan: 92% tenant retention on 3-Meals Plan. Weekend special dinner drove 4.9/5 satisfaction score.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
