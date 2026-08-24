package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Balcony
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.BedEntity
import com.example.data.local.entities.RoomEntity
import com.example.model.BedStatus
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

@Composable
fun Building3DCanvasView(
    rooms: List<RoomEntity>,
    beds: List<BedEntity>,
    modifier: Modifier = Modifier
) {
    var selectedFloor by remember { mutableStateOf(1) }
    var selectedRoom by remember { mutableStateOf<RoomEntity?>(rooms.firstOrNull()) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Floor selector tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🏢 3D Building & Live Floor Maps",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(1, 2, 3, 4).forEach { floor ->
                    val isSel = selectedFloor == floor
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) Indigo600 else Navy800)
                            .border(1.dp, if (isSel) Cyan400 else Color(0x3364748B), RoundedCornerShape(8.dp))
                            .clickable { selectedFloor = floor }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("floor_btn_$floor"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "F$floor",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 3D Isometric Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(180.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw 3D Isometric building floors
                    val centerX = w * 0.45f
                    val floorHeight = 28f
                    val blockWidth = 140f
                    val blockDepth = 90f

                    for (f in 4 downTo 1) {
                        val isCurrentFloor = (f == selectedFloor)
                        val yOffset = h * 0.72f - (f * floorHeight)

                        val pTop = Path().apply {
                            moveTo(centerX, yOffset - blockDepth * 0.5f)
                            lineTo(centerX + blockWidth * 0.5f, yOffset)
                            lineTo(centerX, yOffset + blockDepth * 0.5f)
                            lineTo(centerX - blockWidth * 0.5f, yOffset)
                            close()
                        }

                        val pLeft = Path().apply {
                            moveTo(centerX - blockWidth * 0.5f, yOffset)
                            lineTo(centerX, yOffset + blockDepth * 0.5f)
                            lineTo(centerX, yOffset + blockDepth * 0.5f + floorHeight * 0.7f)
                            lineTo(centerX - blockWidth * 0.5f, yOffset + floorHeight * 0.7f)
                            close()
                        }

                        val pRight = Path().apply {
                            moveTo(centerX, yOffset + blockDepth * 0.5f)
                            lineTo(centerX + blockWidth * 0.5f, yOffset)
                            lineTo(centerX + blockWidth * 0.5f, yOffset + floorHeight * 0.7f)
                            lineTo(centerX, yOffset + blockDepth * 0.5f + floorHeight * 0.7f)
                            close()
                        }

                        // Colors
                        val topColor = if (isCurrentFloor) Color(0xFF06B6D4) else Color(0xFF1E293B)
                        val leftColor = if (isCurrentFloor) Color(0xFF4F46E5) else Color(0xFF0F172A)
                        val rightColor = if (isCurrentFloor) Color(0xFF4338CA) else Color(0xFF131B2E)

                        drawPath(pLeft, leftColor, style = Fill)
                        drawPath(pRight, rightColor, style = Fill)
                        drawPath(pTop, topColor, style = Fill)

                        drawPath(pTop, Color(0x66FFFFFF), style = Stroke(width = if (isCurrentFloor) 2.5f else 1f))
                        drawPath(pLeft, Color(0x44FFFFFF), style = Stroke(width = 1f))
                        drawPath(pRight, Color(0x44FFFFFF), style = Stroke(width = 1f))
                    }
                }

                // Overlay Info Badge
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "FLOOR $selectedFloor",
                        style = MaterialTheme.typography.titleMedium,
                        color = Cyan400,
                        fontWeight = FontWeight.Black
                    )
                    val floorRooms = rooms.filter { it.floorNumber == selectedFloor }
                    val totalBedsOnFloor = floorRooms.sumOf { it.totalBeds }
                    val vacantBedsOnFloor = floorRooms.sumOf { it.availableBeds }
                    Text(
                        text = "${totalBedsOnFloor - vacantBedsOnFloor}/$totalBedsOnFloor Beds Occupied",
                        style = MaterialTheme.typography.labelSmall,
                        color = Emerald400,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$vacantBedsOnFloor Vacant Available",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (vacantBedsOnFloor > 0) Cyan400 else Color(0xFF94A3B8)
                    )
                }
            }
        }

        // Room interactive chips for selected floor
        val floorRooms = rooms.filter { it.floorNumber == selectedFloor }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(floorRooms) { room ->
                val isSelected = selectedRoom?.id == room.id
                val isFullyOccupied = room.availableBeds == 0
                val statusColor = when {
                    isFullyOccupied -> Emerald500
                    room.availableBeds == room.totalBeds -> Cyan400
                    else -> Amber400
                }

                Card(
                    modifier = Modifier
                        .width(135.dp)
                        .clickable { selectedRoom = room }
                        .testTag("room_chip_${room.roomNumber}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Navy700 else Navy800
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Cyan400 else Color(0x3364748B)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Room ${room.roomNumber}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = room.roomType.label,
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "₹${room.baseRent.toInt()}/m",
                                color = Emerald400,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${room.availableBeds} Vacant",
                                color = if (room.availableBeds > 0) Cyan400 else Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        // Detailed Room & Bed Inspector
        selectedRoom?.let { room ->
            val roomBeds = beds.filter { it.roomId == room.id || it.roomNumber == room.roomNumber }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3364748B))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Room ${room.roomNumber} • ${room.roomType.label}",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Floor ${room.floorNumber} • Base Rent ₹${room.baseRent.toInt()}/month",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (room.hasAc) {
                                Icon(Icons.Default.AcUnit, contentDescription = "AC", tint = Cyan400, modifier = Modifier.size(18.dp))
                            }
                            if (room.hasBalcony) {
                                Icon(Icons.Default.Balcony, contentDescription = "Balcony", tint = Amber400, modifier = Modifier.size(18.dp))
                            }
                            if (room.hasAttachedWashroom) {
                                Icon(Icons.Default.Bathtub, contentDescription = "Washroom", tint = Emerald400, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "BED OCCUPANCY MATRIX",
                        style = MaterialTheme.typography.labelSmall,
                        color = Cyan400,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        roomBeds.forEach { bed ->
                            val isOccupied = bed.status == BedStatus.OCCUPIED
                            val isVacant = bed.status == BedStatus.VACANT
                            val statusColor = when (bed.status) {
                                BedStatus.OCCUPIED -> Emerald400
                                BedStatus.VACANT -> Cyan400
                                BedStatus.RESERVED -> Amber400
                                BedStatus.MAINTENANCE -> Rose500
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Navy900)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bed,
                                        contentDescription = "Bed",
                                        tint = statusColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Bed ${bed.bedCode}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                        Text(
                                            text = if (isOccupied) (bed.tenantName ?: "Occupied") else "Available for Allocation",
                                            color = if (isOccupied) Color(0xFFCBD5E1) else Cyan400,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(statusColor.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = bed.status.name,
                                        color = statusColor,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
