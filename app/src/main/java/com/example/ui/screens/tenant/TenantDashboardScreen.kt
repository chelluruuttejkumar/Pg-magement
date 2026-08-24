package com.example.ui.screens.tenant

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entities.BedEntity
import com.example.data.local.entities.ComplaintEntity
import com.example.data.local.entities.FoodOrderEntity
import com.example.data.local.entities.LaundryOrderEntity
import com.example.data.local.entities.PaymentEntity
import com.example.data.local.entities.RoomEntity
import com.example.data.local.entities.TenantEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VisitorEntity
import com.example.model.AttendanceStatus
import com.example.model.ComplaintStatus
import com.example.model.PaymentStatus
import com.example.model.VehicleType
import com.example.model.VisitorStatus
import com.example.ui.components.Building3DCanvasView
import com.example.ui.components.Parking3DCanvasView
import com.example.ui.theme.*

@Composable
fun TenantDashboardScreen(
    rooms: List<RoomEntity>,
    beds: List<BedEntity>,
    tenants: List<TenantEntity>,
    payments: List<PaymentEntity>,
    complaints: List<ComplaintEntity>,
    visitors: List<VisitorEntity>,
    vehicles: List<VehicleEntity>,
    foodOrders: List<FoodOrderEntity>,
    laundryOrders: List<LaundryOrderEntity>,
    onPayRent: (paymentId: String, mode: String) -> Unit,
    onRaiseComplaint: (category: String, title: String, desc: String, priority: String) -> Unit,
    onRequestVisitorPass: (name: String, phone: String, purpose: String, date: String, time: String) -> Unit,
    onRegisterVehicle: (type: VehicleType, num: String, model: String, slot: String) -> Unit,
    onRequestLaundry: (count: Int, service: String) -> Unit,
    onToggleAttendance: () -> Unit,
    onOpenAiChat: () -> Unit,
    onOpenStayCalculator: () -> Unit,
    onOpenQrScan: () -> Unit,
    onOpenSos: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSectionTab by remember { mutableIntStateOf(0) }
    var showComplaintDialog by remember { mutableStateOf(false) }
    var showVisitorDialog by remember { mutableStateOf(false) }
    var showLaundryDialog by remember { mutableStateOf(false) }
    var paymentProcessingId by remember { mutableStateOf<String?>(null) }

    val tenant = tenants.find { it.id == "t-001" } ?: tenants.firstOrNull()

    val sectionTitles = listOf(
        "Overview",
        "3D Rooms",
        "Rent & Bills",
        "Complaints",
        "3D Parking",
        "Visitors",
        "Food & Laundry"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tenant Header Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy500)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Indigo600)
                                    .border(1.5.dp, Indigo400, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("AS", color = Indigo100, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Royal Heights • Room ${tenant?.roomNumber ?: "302-B"}",
                                    color = Color(0xFFCAC4D0),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Welcome, ${tenant?.name?.split(" ")?.firstOrNull() ?: "Aryan"}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Indigo100,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Emerald500.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("KYC OK", color = Emerald400, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Smart Check-in / Notification pill
                        val isCheckedIn = tenant?.attendanceStatus != AttendanceStatus.CHECKED_OUT
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isCheckedIn) Indigo600 else Navy700)
                                .border(1.dp, if (isCheckedIn) Indigo400 else Navy500, RoundedCornerShape(20.dp))
                                .clickable { onToggleAttendance() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("attendance_toggle_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(if (isCheckedIn) Emerald400 else Amber400)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isCheckedIn) "In PG" else "Out",
                                    color = if (isCheckedIn) Indigo100 else Color(0xFFCAC4D0),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Action Tiles (Immersive 4-column service grid)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionTile(
                            icon = Icons.Default.AutoAwesome,
                            label = "AI Help",
                            onClick = onOpenAiChat,
                            tint = Indigo400,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            icon = Icons.Default.QrCodeScanner,
                            label = "Smart Key",
                            onClick = onOpenQrScan,
                            tint = Indigo400,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            icon = Icons.Default.LocalLaundryService,
                            label = "Laundry",
                            onClick = { showLaundryDialog = true },
                            tint = Indigo400,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            icon = Icons.Default.Warning,
                            label = "SOS",
                            tint = Rose400,
                            isSos = true,
                            onClick = onOpenSos,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Section Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedSectionTab,
                containerColor = Navy900,
                contentColor = Cyan400,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedSectionTab]),
                        color = Cyan400
                    )
                }
            ) {
                sectionTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSectionTab == index,
                        onClick = { selectedSectionTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedSectionTab == index) Cyan400 else Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = if (selectedSectionTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Tab Content
        when (selectedSectionTab) {
            0 -> { // Overview
                item {
                    val pendingRent = payments.find { it.status == PaymentStatus.PENDING }
                    val rentAmount = pendingRent?.totalAmount?.toInt() ?: 12500
                    val isPending = pendingRent != null

                    // Signature Immersive UI Monthly Rent Hero Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy500)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            // Top Right Atmospheric Glow Effect
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .align(Alignment.TopEnd)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Indigo400.copy(alpha = 0.18f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )

                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text(
                                            text = "MONTHLY RENT",
                                            color = Indigo400,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "₹$rentAmount",
                                            color = Color(0xFFE6E1E5),
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (isPending) Indigo600 else Emerald500.copy(alpha = 0.2f))
                                            .border(1.dp, if (isPending) Indigo400 else Emerald400, RoundedCornerShape(20.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = if (isPending) "DUE IN 4 DAYS" else "ALL CLEAR",
                                            color = if (isPending) Indigo100 else Emerald400,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Base: ₹${(rentAmount * 0.88).toInt()}",
                                        color = Color(0xFFCAC4D0),
                                        fontSize = 12.sp
                                    )
                                    Text("•", color = Color(0xFF938F99))
                                    Text(
                                        text = "Electricity/Water: ₹${(rentAmount * 0.12).toInt()}",
                                        color = Color(0xFFCAC4D0),
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            if (pendingRent != null) {
                                                onPayRent(pendingRent.id, "UPI (Instant)")
                                            } else {
                                                onPayRent("pay-001", "UPI (Instant)")
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .testTag("hero_pay_now_button"),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Indigo400,
                                            contentColor = Indigo600
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text(
                                            text = if (isPending) "PAY NOW" else "VIEW RECEIPT",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 13.sp,
                                            letterSpacing = 0.5.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Navy700)
                                            .border(1.dp, Navy500, RoundedCornerShape(16.dp))
                                            .clickable { selectedSectionTab = 2 },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ConfirmationNumber,
                                            contentDescription = "Invoices",
                                            tint = Indigo100,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    // KPI Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val pendingRent = payments.find { it.status == PaymentStatus.PENDING }
                        KpiSummaryCard(
                            title = "Rent Status",
                            value = if (pendingRent != null) "₹${pendingRent.totalAmount.toInt()} Due" else "All Paid",
                            subText = if (pendingRent != null) "Due: ${pendingRent.dueDate}" else "Up to date",
                            color = if (pendingRent != null) Amber400 else Emerald400,
                            modifier = Modifier.weight(1f)
                        )
                        KpiSummaryCard(
                            title = "Active Parking",
                            value = vehicles.firstOrNull()?.parkingSlotCode ?: "B-04",
                            subText = vehicles.firstOrNull()?.vehicleNumber ?: "KA 01 EK 4589",
                            color = Indigo400,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Building3DCanvasView(rooms = rooms, beds = beds)
                }

                item {
                    Parking3DCanvasView(vehicles = vehicles, onRegisterVehicle = onRegisterVehicle)
                }
            }

            1 -> { // 3D Rooms
                item {
                    Building3DCanvasView(rooms = rooms, beds = beds)
                }
            }

            2 -> { // Rent & Invoices
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Invoices & Receipts",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "GSTIN: 29AAAAA0000A1Z5",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                items(payments) { payment ->
                    val isPending = payment.status == PaymentStatus.PENDING
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isPending) Amber400.copy(alpha = 0.5f) else Color(0x2264748B)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(payment.invoiceNumber, color = Cyan400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(payment.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isPending) Amber500.copy(alpha = 0.2f) else Emerald500.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = payment.status.name,
                                        color = if (isPending) Amber400 else Emerald400,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Base Amount: ₹${payment.amount.toInt()}", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                                Text("GST (Statutory): ₹${payment.taxGst.toInt()}", color = Color(0xFFCBD5E1), fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total: ₹${payment.totalAmount.toInt()}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
                                Text("Due Date: ${payment.dueDate}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            }

                            if (payment.paidDate != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Paid on ${payment.paidDate} via ${payment.paymentMode}", color = Emerald400, fontSize = 11.sp)
                            }

                            if (isPending) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { onPayRent(payment.id, "UPI (Google Pay / PhonePe)") },
                                        modifier = Modifier.weight(1f).testTag("pay_upi_button"),
                                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Pay ₹${payment.totalAmount.toInt()} UPI", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> { // Complaints
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Maintenance & Support Tickets",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { showComplaintDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("raise_complaint_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Raise Ticket", fontSize = 12.sp)
                        }
                    }
                }

                items(complaints) { comp ->
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
                                Text("Category: ${comp.category}", color = Cyan400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                            Text(comp.title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(comp.description, color = Color(0xFFCBD5E1), fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))
                            Text("Logged on ${comp.createdAt} • Priority: ${comp.priority}", color = Color(0xFF94A3B8), fontSize = 10.sp)

                            if (comp.resolutionNotes != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Update: ${comp.resolutionNotes}", color = Emerald400, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            4 -> { // 3D Parking
                item {
                    Parking3DCanvasView(vehicles = vehicles, onRegisterVehicle = onRegisterVehicle)
                }
            }

            5 -> { // Visitors
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Digital Visitor Passes",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { showVisitorDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("request_visitor_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Pass", fontSize = 12.sp)
                        }
                    }
                }

                items(visitors) { visitor ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2264748B))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(visitor.visitorName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Phone: ${visitor.visitorPhone}", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                                Text("Purpose: ${visitor.purpose} • Date: ${visitor.visitDate}", color = Color(0xFF94A3B8), fontSize = 11.sp)
                                Text("Expected: ${visitor.expectedInTime}", color = Cyan400, fontSize = 10.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Emerald500.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(visitor.status.name, color = Emerald400, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            6 -> { // Food & Laundry
                item {
                    Text(
                        text = "Dining Subscriptions & Laundry Hub",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                // Food Meal Card
                items(foodOrders) { food ->
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
                                    Icon(Icons.Default.Fastfood, contentDescription = null, tint = Amber400, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("${food.mealDate} • ${food.mealType}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Text(food.planType, color = Cyan400, fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(food.menuItems, color = Color(0xFFCBD5E1), fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }

                // Laundry Schedule Section
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Laundry Pickups",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { showLaundryDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("request_laundry_button")
                        ) {
                            Icon(Icons.Default.LocalLaundryService, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Schedule", fontSize = 11.sp)
                        }
                    }
                }

                items(laundryOrders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Navy800),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("${order.serviceType} (${order.clothesCount} Clothes)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Pickup: ${order.pickupDate} • Room ${order.roomNumber}", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹${order.charges.toInt()}", color = Emerald400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(order.status, color = Cyan400, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showComplaintDialog) {
        RaiseComplaintDialog(
            onDismiss = { showComplaintDialog = false },
            onConfirm = { cat, title, desc, prio ->
                onRaiseComplaint(cat, title, desc, prio)
                showComplaintDialog = false
            }
        )
    }

    if (showVisitorDialog) {
        RequestVisitorPassDialog(
            onDismiss = { showVisitorDialog = false },
            onConfirm = { name, phone, purpose, date, time ->
                onRequestVisitorPass(name, phone, purpose, date, time)
                showVisitorDialog = false
            }
        )
    }

    if (showLaundryDialog) {
        ScheduleLaundryDialog(
            onDismiss = { showLaundryDialog = false },
            onConfirm = { count, serv ->
                onRequestLaundry(count, serv)
                showLaundryDialog = false
            }
        )
    }
}

@Composable
fun QuickActionTile(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = Indigo400,
    isSos: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bg = if (isSos) Rose600 else Navy700
    val borderCol = if (isSos) Rose500 else Navy500
    val textCol = if (isSos) Rose400 else Color(0xFFE6E1E5)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .border(1.dp, borderCol, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(label, color = textCol, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        }
    }
}

@Composable
fun KpiSummaryCard(
    title: String,
    value: String,
    subText: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy500)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = Color(0xFFCAC4D0), fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = color, fontSize = 17.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subText, color = Color(0xFF938F99), fontSize = 10.sp)
        }
    }
}

@Composable
fun RaiseComplaintDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var category by remember { mutableStateOf("WiFi & Internet") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("High") }

    val categories = listOf("WiFi & Internet", "Electrical", "Plumbing", "Housekeeping", "Food", "Noise/Other")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Raise Maintenance Complaint", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Select Category", color = Cyan400, fontSize = 11.sp)
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    categories.take(3).forEach { cat ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (category == cat) Indigo600 else Navy800)
                                .clickable { category = cat }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(cat.split(" ").first(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Issue Title (e.g. WiFi Router Not Connecting)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Cyan400,
                        unfocusedBorderColor = Color(0xFF64748B)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detailed Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
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
                        onClick = {
                            if (title.isNotBlank()) {
                                onConfirm(category, title, description.ifBlank { "Standard Issue" }, priority)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Text("Submit Ticket")
                    }
                }
            }
        }
    }
}

@Composable
fun RequestVisitorPassDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var visitorName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("Family Visit") }
    var date by remember { mutableStateOf("Today, 24 Aug 2026") }
    var time by remember { mutableStateOf("06:00 PM") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Generate Digital Visitor Pass", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = visitorName,
                    onValueChange = { visitorName = it },
                    label = { Text("Visitor Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Cyan400,
                        unfocusedBorderColor = Color(0xFF64748B)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Visitor Contact Number") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Cyan400,
                        unfocusedBorderColor = Color(0xFF64748B)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = { Text("Purpose of Visit") },
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
                        onClick = {
                            if (visitorName.isNotBlank()) {
                                onConfirm(visitorName, phone.ifBlank { "+91 90000 00000" }, purpose, date, time)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Text("Issue QR Pass")
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleLaundryDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, String) -> Unit
) {
    var count by remember { mutableStateOf("6") }
    var service by remember { mutableStateOf("Wash & Fold") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Schedule Laundry Service", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = count,
                    onValueChange = { count = it },
                    label = { Text("Number of Clothes") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Cyan400,
                        unfocusedBorderColor = Color(0xFF64748B)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Wash & Fold", "Steam Iron", "Dry Clean").forEach { serv ->
                        val isSel = service == serv
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) Indigo600 else Navy800)
                                .clickable { service = serv }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(serv, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val c = count.toIntOrNull() ?: 5
                            onConfirm(c, service)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Text("Confirm Pickup")
                    }
                }
            }
        }
    }
}
