package com.example.ui.screens.owner

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.BranchEntity
import com.example.ui.theme.Amber400
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900

@Composable
fun OwnerDashboardScreen(
    branches: List<BranchEntity>,
    onOpenAiForecast: () -> Unit,
    onOpenWebPortal: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val totalBranches = branches.size
    val totalBeds = branches.sumOf { it.totalBeds }
    val totalOccupied = branches.sumOf { it.occupiedBeds }
    val totalMonthlyRevenue = branches.sumOf { it.monthlyRevenue }
    val totalOperatingCost = totalMonthlyRevenue * 0.285 // 28.5% op-ex (Chef, utilities, housekeeping, WiFi)
    val netOperatingIncome = totalMonthlyRevenue - totalOperatingCost

    val tabs = listOf("Executive Overview", "Branch Portfolio", "P&L Statement", "GST & Taxation", "Ecosystem Architecture")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Owner Executive Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ENTERPRISE OWNER CONSOLE", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Dr. Vikramaditya Singhania", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Portfolio: 3 Cities • $totalBranches Prime PG Properties", color = Color(0xFF94A3B8), fontSize = 12.sp)
                        }

                        Button(
                            onClick = onOpenAiForecast,
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Yield", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Multi-Branch Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OwnerMetricTile("Monthly Gross", "₹${(totalMonthlyRevenue / 100000.0).format(2)}L", Emerald400, Modifier.weight(1f))
                        OwnerMetricTile("Net Margin", "₹${(netOperatingIncome / 100000.0).format(2)}L", Cyan400, Modifier.weight(1f))
                        OwnerMetricTile("Network Occupancy", "${if (totalBeds > 0) totalOccupied * 100 / totalBeds else 0}%", Amber400, Modifier.weight(1f))
                    }
                }
            }
        }

        // Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Navy900,
                contentColor = Cyan400,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Cyan400
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Cyan400 else Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        when (selectedTab) {
            0, 1 -> { // Branch Portfolio
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Active PG Branches",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$totalBranches Cities Live",
                            style = MaterialTheme.typography.labelSmall,
                            color = Cyan400
                        )
                    }
                }

                items(branches) { branch ->
                    val occPct = if (branch.totalBeds > 0) (branch.occupiedBeds * 100 / branch.totalBeds) else 0
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2264748B))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Indigo600.copy(alpha = 0.3f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.LocationCity, contentDescription = null, tint = Cyan400, modifier = Modifier.size(20.dp))
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(branch.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(branch.address, color = Color(0xFF94A3B8), fontSize = 10.sp, maxLines = 1)
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Emerald500.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text("$occPct% Occupied", color = Emerald400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Rooms: ${branch.totalRooms} | Beds: ${branch.occupiedBeds}/${branch.totalBeds}", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                Text("Revenue: ₹${(branch.monthlyRevenue / 100000.0).format(2)} L/m", color = Emerald400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Property Manager: ${branch.managerName} (${branch.managerPhone})", color = Color(0xFF94A3B8), fontSize = 10.sp)
                        }
                    }
                }
            }

            2 -> { // P&L Statement
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3310B981))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("FINANCIAL P&L STATEMENT (AUGUST 2026)", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(12.dp))

                            PlRow("Gross Rental & Booking Receipts", "+ ₹22,97,000", Emerald400, isBold = true)
                            PlRow("• Monthly Recurring Bed Rent", "₹20,40,000", Color(0xFFCBD5E1))
                            PlRow("• Flexible Hourly & Daily Stays", "₹1,45,000", Color(0xFFCBD5E1))
                            PlRow("• Food Plans & Laundry Hub Add-ons", "₹1,12,000", Color(0xFFCBD5E1))

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x3364748B)))
                            Spacer(modifier = Modifier.height(8.dp))

                            PlRow("Total Operational Overhead (OpEx)", "- ₹6,52,000", Amber400, isBold = true)
                            PlRow("• Gourmet Kitchen & Chef Ingredients", "₹2,80,000", Color(0xFF94A3B8))
                            PlRow("• Electricity & Water Smart Meter Grid", "₹1,65,000", Color(0xFF94A3B8))
                            PlRow("• 1 Gbps Mesh Fiber & CCTV Cloud Storage", "₹42,000", Color(0xFF94A3B8))
                            PlRow("• Housekeeping Staff & Security Guard Payroll", "₹1,65,000", Color(0xFF94A3B8))

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x3364748B)))
                            Spacer(modifier = Modifier.height(8.dp))

                            PlRow("Net Operating Profit (NOI)", "₹16,45,000 (71.6% Margin)", Emerald400, isBold = true)
                        }
                    }
                }
            }

            3 -> { // GST & Taxation
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("GST STATUTORY FILING SUMMARY (GSTR-1 / GSTR-3B)", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("GSTIN: 29AAAAA0000A1Z5 • State: Karnataka (29)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            PlRow("Taxable Supply Value", "₹20,50,892", Color(0xFFCBD5E1))
                            PlRow("CGST Output (6%)", "₹1,23,054", Color(0xFFCBD5E1))
                            PlRow("SGST Output (6%)", "₹1,23,054", Color(0xFFCBD5E1))
                            PlRow("Input Tax Credit (ITC Eligible)", "- ₹38,400", Emerald400)
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0x3364748B)))
                            Spacer(modifier = Modifier.height(6.dp))
                            PlRow("Net Tax Payable to Govt", "₹2,07,708", Amber400, isBold = true)
                        }
                    }
                }
            }

            4 -> { // Ecosystem Architecture
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("🌐 FULL-STACK ECOSYSTEM BLUEPRINT", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "PG Master provides full parity across:\n• Next.js + Tailwind Web Portal (Tenants & Corporate)\n• React Native + Expo iOS App\n• Android Native App (Kotlin + Compose + Room)\n• NestJS + PostgreSQL + Prisma + Redis + WebSockets Backend",
                                color = Color(0xFFCBD5E1),
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onOpenWebPortal,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                            ) {
                                Text("Inspect Architecture & API Schemas", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OwnerMetricTile(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2264748B))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, color = Color(0xFF94A3B8), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun PlRow(
    label: String,
    value: String,
    color: Color,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = if (isBold) Color.White else Color(0xFFCBD5E1),
            fontSize = if (isBold) 12.sp else 11.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            color = color,
            fontSize = if (isBold) 12.sp else 11.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)
