package com.example.ui.screens.web

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber400
import com.example.ui.theme.Cyan400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900

@Composable
fun WebPortalScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val apiEndpoints = listOf(
        Triple("POST", "/api/v1/auth/disclaimer/accept", "Records IP, GPS, device & 9 mandatory regulatory policy acknowledgments"),
        Triple("GET", "/api/v1/rooms/matrix/3d", "Returns isometric floors, rooms, and bed occupancy states"),
        Triple("POST", "/api/v1/payments/rent/checkout", "Generates UPI dynamic intent & GST tax breakdown invoice"),
        Triple("POST", "/api/v1/complaints/triage", "Dispatches SLA maintenance ticket with automated technician assignment"),
        Triple("POST", "/api/v1/visitors/pass/generate", "Issues digital cryptographic QR visitor entry pass"),
        Triple("GET", "/api/v1/parking/bays/live", "Returns real-time occupancy of 28 bays (Car, Bike, EV Fast Chargers)"),
        Triple("WS", "wss://api.pgmaster.io/live", "Real-time biometric turnstile access, live CCTV stream & SOS broadcasts")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("PG Master SaaS Architecture", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Full-Stack Web & Microservices Blueprint", color = Cyan400, fontSize = 11.sp)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x3338BDF8))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🏗️ MULTI-TIER ECOSYSTEM ARCHITECTURE", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "• Web Frontend: Next.js 14 App Router, React Server Components, Tailwind CSS, Three.js 3D isometric room visualizers, Framer Motion.\n• Mobile Suite: Android (Jetpack Compose + Kotlin + Room), iOS (React Native + Expo SDK 51 + NativeWind).\n• Backend Engine: NestJS Enterprise Microservices, Prisma ORM, PostgreSQL 16 database, Redis caching, Socket.io WebSockets for live gate turnstile events.\n• AI Infrastructure: Google Gemini 3.5 Flash for rent breakdown, ticket diagnosis & predictive occupancy yield algorithms.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Text("REST & WEBSOCKET API CATALOG", color = Cyan400, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }

        items(apiEndpoints) { (method, path, desc) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x2264748B))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when (method) {
                                        "POST" -> Indigo600
                                        "GET" -> Emerald500.copy(alpha = 0.3f)
                                        else -> Amber400.copy(alpha = 0.3f)
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                method,
                                color = when (method) {
                                    "POST" -> Color.White
                                    "GET" -> Emerald400
                                    else -> Amber400
                                },
                                fontWeight = FontWeight.Black,
                                fontSize = 10.sp
                            )
                        }
                        Text(path, color = Color.White, fontFamily = FontFamily.Monospace, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(desc, color = Color(0xFF94A3B8), fontSize = 11.sp)
                }
            }
        }
    }
}
