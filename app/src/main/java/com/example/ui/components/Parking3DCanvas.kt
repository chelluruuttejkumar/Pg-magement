package com.example.ui.components

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entities.VehicleEntity
import com.example.model.VehicleType
import com.example.ui.theme.Amber400
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.Rose400

@Composable
fun Parking3DCanvasView(
    vehicles: List<VehicleEntity>,
    onRegisterVehicle: (type: VehicleType, number: String, model: String, slot: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddVehicleDialog by remember { mutableStateOf(false) }
    var selectedBayType by remember { mutableStateOf<VehicleType?>(null) }

    val totalSlots = 28
    val occupiedSlots = vehicles.size
    val availableSlots = totalSlots - occupiedSlots

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🚗 3D Smart Parking Matrix",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$occupiedSlots/$totalSlots Parked • $availableSlots Slots Available",
                    style = MaterialTheme.typography.labelSmall,
                    color = Cyan400
                )
            }

            Button(
                onClick = { showAddVehicleDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("add_vehicle_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Vehicle", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Register", fontSize = 12.sp)
            }
        }

        // 3D Parking Grid Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(170.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw 3D Isometric Parking Lot Ground
                    val groundPath = Path().apply {
                        moveTo(w * 0.5f, 20f)
                        lineTo(w - 20f, h * 0.5f)
                        lineTo(w * 0.5f, h - 20f)
                        lineTo(20f, h * 0.5f)
                        close()
                    }
                    drawPath(groundPath, Color(0xFF0F172A), style = Fill)
                    drawPath(groundPath, Color(0x4438BDF8), style = Stroke(width = 2f))

                    // Draw isometric parking slots grid
                    val rows = 3
                    val cols = 4
                    val slotW = 55f
                    val slotH = 30f

                    for (r in 0 until rows) {
                        for (c in 0 until cols) {
                            val slotIndex = r * cols + c
                            val isOccupied = slotIndex < vehicles.size
                            val slotColor = if (isOccupied) Color(0xFF10B981) else Color(0x3338BDF8)

                            val x = w * 0.28f + (c * 42f) - (r * 32f)
                            val y = h * 0.30f + (r * 24f) + (c * 12f)

                            val bayPath = Path().apply {
                                moveTo(x, y)
                                lineTo(x + slotW, y + 10f)
                                lineTo(x + slotW - 20f, y + slotH + 10f)
                                lineTo(x - 20f, y + slotH)
                                close()
                            }

                            drawPath(bayPath, slotColor.copy(alpha = if (isOccupied) 0.6f else 0.2f), style = Fill)
                            drawPath(bayPath, slotColor, style = Stroke(width = 1.5f))
                        }
                    }
                }

                // Legend
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Emerald400))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Parked", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Cyan400))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Available", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                    }
                }
            }
        }

        // Vehicle List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            vehicles.forEach { vehicle ->
                val (icon, color) = when (vehicle.vehicleType) {
                    VehicleType.BIKE -> Icons.Default.DirectionsBike to Cyan400
                    VehicleType.CAR -> Icons.Default.DirectionsCar to Indigo400
                    VehicleType.EV -> Icons.Default.ElectricCar to Emerald400
                    VehicleType.BICYCLE -> Icons.Default.PedalBike to Amber400
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(12.dp),
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
                                    .background(color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = icon, contentDescription = vehicle.vehicleType.label, tint = color, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = vehicle.vehicleNumber,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${vehicle.modelName} • ${vehicle.tenantName} (Room ${vehicle.roomNumber})",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Navy900)
                                    .border(1.dp, Color(0x3338BDF8), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = vehicle.parkingSlotCode,
                                    color = Cyan400,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Entry: ${vehicle.entryTime}",
                                color = Color(0xFF64748B),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddVehicleDialog) {
        AddVehicleDialog(
            onDismiss = { showAddVehicleDialog = false },
            onConfirm = { type, num, model, slot ->
                onRegisterVehicle(type, num, model, slot)
                showAddVehicleDialog = false
            }
        )
    }
}

@Composable
fun AddVehicleDialog(
    onDismiss: () -> Unit,
    onConfirm: (VehicleType, String, String, String) -> Unit
) {
    var selectedType by remember { mutableStateOf(VehicleType.BIKE) }
    var vehicleNumber by remember { mutableStateOf("") }
    var modelName by remember { mutableStateOf("") }
    var slotName by remember { mutableStateOf("B-08 (Basement 1)") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4438BDF8))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Register Tenant Vehicle",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Vehicle Type selector
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    VehicleType.values().forEach { vType ->
                        val isSel = selectedType == vType
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) Indigo600 else Navy800)
                                .clickable { selectedType = vType }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (vType) {
                                    VehicleType.BIKE -> "Bike"
                                    VehicleType.CAR -> "Car"
                                    VehicleType.EV -> "EV"
                                    VehicleType.BICYCLE -> "Cycle"
                                },
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = vehicleNumber,
                    onValueChange = { vehicleNumber = it },
                    label = { Text("Vehicle Plate No. (e.g. KA 01 AB 1234)") },
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
                    value = modelName,
                    onValueChange = { modelName = it },
                    label = { Text("Model & Make (e.g. Royal Enfield 350)") },
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
                    value = slotName,
                    onValueChange = { slotName = it },
                    label = { Text("Assigned Parking Bay") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Cyan400,
                        unfocusedBorderColor = Color(0xFF64748B)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (vehicleNumber.isNotBlank()) {
                                onConfirm(selectedType, vehicleNumber, modelName.ifBlank { "Standard Vehicle" }, slotName)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600)
                    ) {
                        Text("Assign Bay")
                    }
                }
            }
        }
    }
}
