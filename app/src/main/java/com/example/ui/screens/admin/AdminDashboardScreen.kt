package com.example.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.entities.BedEntity
import com.example.data.local.entities.ComplaintEntity
import com.example.data.local.entities.PaymentEntity
import com.example.data.local.entities.RoomEntity
import com.example.data.local.entities.TenantEntity
import com.example.data.local.entities.UtilityMeterEntity
import com.example.data.local.entities.VisitorEntity
import com.example.model.BedStatus
import com.example.model.ComplaintStatus
import com.example.model.VisitorStatus
import com.example.ui.components.Building3DCanvasView
import com.example.ui.theme.Amber400
import com.example.ui.theme.Amber500
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Rose500
import com.example.ui.theme.Rose600

@Composable
fun AdminDashboardScreen(
    rooms: List<RoomEntity>,
    beds: List<BedEntity>,
    tenants: List<TenantEntity>,
    payments: List<PaymentEntity>,
    complaints: List<ComplaintEntity>,
    visitors: List<VisitorEntity>,
    utilityMeters: List<UtilityMeterEntity>,
    onUpdateComplaintStatus: (id: String, status: ComplaintStatus, notes: String?) -> Unit,
    onUpdateVisitorStatus: (id: String, status: VisitorStatus) -> Unit,
    onOpenAiForecast: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCctvChannel by remember { mutableIntStateOf(0) }
    var showResolutionDialog by remember { mutableStateOf<ComplaintEntity?>(null) }

    val totalBeds = beds.size
    val occupiedBeds = beds.count { it.status == BedStatus.OCCUPIED }
    val vacantBeds = beds.count { it.status == BedStatus.VACANT }
    val occupancyRate = if (totalBeds > 0) (occupiedBeds * 100 / totalBeds) else 0

    val tabs = listOf(
        "Operations",
        "3D Rooms Matrix",
        "CCTV Feeds (4)",
        "Tickets & SLA",
        "Tenant Directory",
        "Visitor Gate",
        "Smart Meters"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Admin Master Banner
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
                            Text("ADMIN CONTROL DESK", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Rajesh Sharma (Property Manager)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Branch: HSR Grand • Sector 2, Bengaluru", color = Color(0xFF94A3B8), fontSize = 12.sp)
                        }

                        Button(
                            onClick = onOpenAiForecast,
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("ai_forecast_button")
                        ) {
                            Text("AI Forecast", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Occupancy Progress
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Live Bed Occupancy ($occupancyRate%)", color = Color(0xFFCBD5E1), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("$occupiedBeds / $totalBeds Beds Assigned", color = Emerald400, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Navy900)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(occupancyRate / 100f)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Brush.horizontalGradient(listOf(Indigo600, Cyan400)))
                        )
                    }
                }
            }
        }

        // Tab Selector
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
            0 -> { // Operations
                item {
                    // KPI Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AdminStatCard("Vacant Beds", "$vacantBeds Available", Cyan400, Modifier.weight(1f))
                        AdminStatCard("Open Tickets", "${complaints.count { it.status == ComplaintStatus.OPEN }} Pending", Amber400, Modifier.weight(1f))
                        AdminStatCard("Active Visitors", "${visitors.count { it.status == VisitorStatus.APPROVED }} Approved", Emerald400, Modifier.weight(1f))
                    }
                }

                item {
                    Building3DCanvasView(rooms = rooms, beds = beds)
                }

                // CCTV Quick Grid
                item {
                    CctvSurveillanceCard(selectedChannel = selectedCctvChannel, onChannelSelected = { selectedCctvChannel = it })
                }
            }

            1 -> { // 3D Rooms
                item {
                    Building3DCanvasView(rooms = rooms, beds = beds)
                }
            }

            2 -> { // CCTV Feeds
                item {
                    CctvSurveillanceCard(selectedChannel = selectedCctvChannel, onChannelSelected = { selectedCctvChannel = it })
                }
            }

            3 -> { // Tickets & SLA
                item {
                    Text(
                        text = "Ticket Management & SLA Board",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(complaints) { comp ->
                    val isResolved = comp.status == ComplaintStatus.RESOLVED
                    val statusColor = when (comp.status) {
                        ComplaintStatus.OPEN -> Amber400
                        ComplaintStatus.IN_PROGRESS -> Cyan400
                        ComplaintStatus.RESOLVED -> Emerald400
                    }

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
                                Text("#TICKET-${comp.id.take(4).uppercase()} • ${comp.category}", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(statusColor.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(comp.status.name, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(comp.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(comp.description, color = Color(0xFFCBD5E1), fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))
                            Text("Raised by ${comp.tenantName} (Room ${comp.roomNumber}) • ${comp.createdAt}", color = Color(0xFF94A3B8), fontSize = 10.sp)

                            if (comp.resolutionNotes != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Resolution: ${comp.resolutionNotes}", color = Emerald400, fontSize = 11.sp)
                            }

                            if (!isResolved) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (comp.status == ComplaintStatus.OPEN) {
                                        Button(
                                            onClick = { onUpdateComplaintStatus(comp.id, ComplaintStatus.IN_PROGRESS, "Technician dispatched to Room ${comp.roomNumber}") },
                                            modifier = Modifier.weight(1f),
                                            colors = ButtonDefaults.buttonColors(containerColor = Cyan400),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Assign Tech", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        }
                                    }

                                    Button(
                                        onClick = { showResolutionDialog = comp },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Mark Resolved", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            4 -> { // Tenant Directory
                item {
                    Text(
                        text = "Active Tenant Directory & KYC",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(tenants) { t ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2264748B))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(t.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Emerald500.copy(alpha = 0.2f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text("KYC OK", color = Emerald400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Text("Room ${t.roomNumber} (Bed ${t.bedCode}) • ${t.phone}", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                Text("Rent: ₹${t.monthlyRent.toInt()}/m • Joined ${t.checkInDate}", color = Color(0xFF94A3B8), fontSize = 10.sp)
                                Text("ICE Contact: ${t.emergencyContact}", color = Amber400, fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (t.duesAmount > 0) "₹${t.duesAmount.toInt()} Due" else "No Dues",
                                    color = if (t.duesAmount > 0) Amber400 else Emerald400,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(t.attendanceStatus.name, color = Cyan400, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            5 -> { // Visitor Gate
                item {
                    Text(
                        text = "Visitor Gate Security & Check-in",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(visitors) { v ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${v.visitorName} (${v.visitorPhone})", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Emerald500.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(v.status.name, color = Emerald400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text("Visiting ${v.tenantName} in Room ${v.roomNumber} for ${v.purpose}", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                            Text("Expected: ${v.expectedInTime} • Date: ${v.visitDate}", color = Color(0xFF94A3B8), fontSize = 10.sp)

                            if (v.status == VisitorStatus.APPROVED) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { onUpdateVisitorStatus(v.id, VisitorStatus.CHECKED_IN) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Check In at Gate", fontSize = 11.sp)
                                    }
                                }
                            } else if (v.status == VisitorStatus.CHECKED_IN) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { onUpdateVisitorStatus(v.id, VisitorStatus.CHECKED_OUT) },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Mark Check Out", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            6 -> { // Smart Meters
                item {
                    Text(
                        text = "IoT Smart Sub-Meters (Electricity & Water)",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(utilityMeters) { meter ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2264748B))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (meter.meterType.contains("Elec")) Amber500.copy(alpha = 0.2f) else Cyan400.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (meter.meterType.contains("Elec")) Icons.Default.Bolt else Icons.Default.WaterDrop,
                                        contentDescription = null,
                                        tint = if (meter.meterType.contains("Elec")) Amber400 else Cyan400,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Room ${meter.roomNumber} • ${meter.meterType}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Current: ${meter.currentReading.toInt()} | Prev: ${meter.previousReading.toInt()} units", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                                    Text("Rate: ₹${meter.unitRate}/unit • ${meter.lastRecordedDate}", color = Color(0xFF94A3B8), fontSize = 10.sp)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹${meter.totalBill.toInt()}", color = Emerald400, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Auto Computed", color = Color(0xFF64748B), fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    showResolutionDialog?.let { comp ->
        ResolutionDialog(
            complaint = comp,
            onDismiss = { showResolutionDialog = null },
            onConfirm = { notes ->
                onUpdateComplaintStatus(comp.id, ComplaintStatus.RESOLVED, notes)
                showResolutionDialog = null
            }
        )
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(title, color = Color(0xFF94A3B8), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CctvSurveillanceCard(
    selectedChannel: Int,
    onChannelSelected: (Int) -> Unit
) {
    val channels = listOf("CAM 01: Main Gate Turnstile", "CAM 02: 1st Floor Corridor", "CAM 03: Dining Lounge", "CAM 04: B1 Parking EV Bay")

    val infiniteTransition = rememberInfiniteTransition(label = "cctvRec")
    val recAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "recAlpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Rose500.copy(alpha = recAlpha)))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("LIVE CCTV STREAM (1080p 60FPS)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Text("24/7 NVR RECORDING", color = Emerald400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Video Screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF020617))
                    .border(1.dp, Color(0x4438BDF8), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Videocam, contentDescription = null, tint = Color(0x3338BDF8), modifier = Modifier.size(48.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(channels[selectedChannel], color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("AI Object & Face Recognition Stream Online", color = Cyan400, fontSize = 10.sp)
                }

                // Channel watermark
                Text(
                    text = "CAM ${selectedChannel + 1} • LIVE",
                    color = Color(0x88FFFFFF),
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Channel selector
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                channels.forEachIndexed { idx, ch ->
                    val isSel = selectedChannel == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) Indigo600 else Navy800)
                            .clickable { onChannelSelected(idx) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("CAM ${idx + 1}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ResolutionDialog(
    complaint: ComplaintEntity,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var notes by remember { mutableStateOf("Resolved by on-site maintenance team.") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Resolve Ticket #${complaint.id.take(4).uppercase()}", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(complaint.title, color = Cyan400, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Resolution Notes & Technician Action") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Cyan400,
                        unfocusedBorderColor = Color(0xFF64748B)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(notes) },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500)
                    ) {
                        Text("Close Ticket")
                    }
                }
            }
        }
    }
}
