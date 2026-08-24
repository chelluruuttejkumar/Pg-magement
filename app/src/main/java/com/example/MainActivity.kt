package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.components.AiChatbotSheet
import com.example.ui.components.AiForecastSheet
import com.example.ui.components.DisclaimerDialog
import com.example.ui.components.EmergencySosDialog
import com.example.ui.components.HourlyStayCalculatorDialog
import com.example.ui.components.LanguageSelectorRow
import com.example.ui.components.LiveWebSocketBanner
import com.example.ui.components.QrScannerFaceDialog
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.owner.OwnerDashboardScreen
import com.example.ui.screens.tenant.TenantDashboardScreen
import com.example.ui.screens.web.WebPortalScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.PgViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PgViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val currentRole by viewModel.currentRole.collectAsState()
                val currentLanguage by viewModel.currentLanguage.collectAsState()
                val isDisclaimerAccepted by viewModel.isDisclaimerAccepted.collectAsState()
                val showDisclaimerModal by viewModel.showDisclaimerModal.collectAsState()

                val branches by viewModel.branches.collectAsState()
                val rooms by viewModel.rooms.collectAsState()
                val beds by viewModel.beds.collectAsState()
                val tenants by viewModel.tenants.collectAsState()
                val payments by viewModel.payments.collectAsState()
                val complaints by viewModel.complaints.collectAsState()
                val visitors by viewModel.visitors.collectAsState()
                val vehicles by viewModel.vehicles.collectAsState()
                val foodOrders by viewModel.foodOrders.collectAsState()
                val laundryOrders by viewModel.laundryOrders.collectAsState()
                val utilityMeters by viewModel.utilityMeters.collectAsState()
                val liveTicker by viewModel.liveTicker.collectAsState()

                val showSosDialog by viewModel.showSosDialog.collectAsState()
                val showQrScanDialog by viewModel.showQrScanDialog.collectAsState()
                val showStayCalculator by viewModel.showStayCalculator.collectAsState()
                val showAiChatSheet by viewModel.showAiChatSheet.collectAsState()
                val showAiForecastSheet by viewModel.showAiForecastSheet.collectAsState()
                val showWebPortalViewer by viewModel.showWebPortalViewer.collectAsState()

                val chatMessages by viewModel.chatMessages.collectAsState()
                val isAiGenerating by viewModel.isAiGenerating.collectAsState()

                val aiSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                val forecastSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                // Statutory Disclaimer Popup (On-demand or initial)
                if (showDisclaimerModal) {
                    DisclaimerDialog(
                        language = currentLanguage,
                        onDismiss = { viewModel.setShowDisclaimerModal(false) },
                        onAccept = { name, dev, ip, lat, lng, loc ->
                            viewModel.acceptDisclaimer(name, dev, ip, lat, lng, loc)
                        }
                    )
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Navy900,
                    topBar = {
                        Column(modifier = Modifier.background(Navy900)) {
                            TopAppBar(
                                title = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Indigo600)
                                                .border(1.dp, Indigo400, RoundedCornerShape(10.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Apartment,
                                                contentDescription = "PG Master",
                                                tint = Indigo100,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "PG Master",
                                                color = Color(0xFFE6E1E5),
                                                fontWeight = FontWeight.Black,
                                                fontSize = 17.sp
                                            )
                                            Text(
                                                text = "Smart PG Management Ecosystem",
                                                color = Indigo400,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                },
                                actions = {
                                    // AI Assistant Quick Trigger
                                    IconButton(
                                        onClick = { viewModel.setShowAiChatSheet(true) },
                                        modifier = Modifier.testTag("ai_header_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = "AI Assistant",
                                            tint = Indigo400
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Navy900
                                )
                            )

                            // Language Switcher Row (Native Scripts)
                            LanguageSelectorRow(
                                selectedLanguage = currentLanguage,
                                onLanguageSelected = { viewModel.setLanguage(it) }
                            )

                            // Live WebSocket & SOS Bar
                            LiveWebSocketBanner(
                                tickerText = liveTicker,
                                onOpenSos = { viewModel.triggerEmergencySos("HSR Layout PG Premises") }
                            )
                        }
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = Navy900,
                            tonalElevation = 8.dp,
                            modifier = Modifier.border(
                                width = 1.dp,
                                color = Navy500,
                                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
                            )
                        ) {
                            NavigationBarItem(
                                selected = currentRole == UserRole.TENANT && !showWebPortalViewer,
                                onClick = {
                                    viewModel.setRole(UserRole.TENANT)
                                    viewModel.setShowWebPortalViewer(false)
                                },
                                icon = { Icon(Icons.Default.Person, contentDescription = "Tenant") },
                                label = { Text("Tenant", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Indigo100,
                                    selectedTextColor = Indigo400,
                                    unselectedIconColor = Color(0xFF938F99),
                                    unselectedTextColor = Color(0xFF938F99),
                                    indicatorColor = Indigo600
                                ),
                                modifier = Modifier.testTag("nav_tenant_app")
                            )

                            NavigationBarItem(
                                selected = currentRole == UserRole.ADMIN && !showWebPortalViewer,
                                onClick = {
                                    viewModel.setRole(UserRole.ADMIN)
                                    viewModel.setShowWebPortalViewer(false)
                                },
                                icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                                label = { Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Indigo100,
                                    selectedTextColor = Indigo400,
                                    unselectedIconColor = Color(0xFF938F99),
                                    unselectedTextColor = Color(0xFF938F99),
                                    indicatorColor = Indigo600
                                ),
                                modifier = Modifier.testTag("nav_admin_app")
                            )

                            NavigationBarItem(
                                selected = currentRole == UserRole.OWNER && !showWebPortalViewer,
                                onClick = {
                                    viewModel.setRole(UserRole.OWNER)
                                    viewModel.setShowWebPortalViewer(false)
                                },
                                icon = { Icon(Icons.Default.CorporateFare, contentDescription = "Owner") },
                                label = { Text("Owner", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Indigo100,
                                    selectedTextColor = Indigo400,
                                    unselectedIconColor = Color(0xFF938F99),
                                    unselectedTextColor = Color(0xFF938F99),
                                    indicatorColor = Indigo600
                                ),
                                modifier = Modifier.testTag("nav_owner_app")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (showWebPortalViewer) {
                            WebPortalScreen(
                                onBack = { viewModel.setShowWebPortalViewer(false) }
                            )
                        } else {
                            when (currentRole) {
                                UserRole.TENANT -> {
                                    TenantDashboardScreen(
                                        rooms = rooms,
                                        beds = beds,
                                        tenants = tenants,
                                        payments = payments,
                                        complaints = complaints,
                                        visitors = visitors,
                                        vehicles = vehicles,
                                        foodOrders = foodOrders,
                                        laundryOrders = laundryOrders,
                                        onPayRent = { pId, mode -> viewModel.payRent(pId, mode) },
                                        onRaiseComplaint = { cat, t, d, p -> viewModel.raiseComplaint(cat, t, d, p) },
                                        onRequestVisitorPass = { name, ph, pur, dt, tm -> viewModel.requestVisitorPass(name, ph, pur, dt, tm) },
                                        onRegisterVehicle = { t, n, m, s -> viewModel.registerVehicle(t, n, m, s) },
                                        onRequestLaundry = { c, s -> viewModel.requestLaundry(c, s) },
                                        onToggleAttendance = { viewModel.toggleAttendance() },
                                        onOpenAiChat = { viewModel.setShowAiChatSheet(true) },
                                        onOpenStayCalculator = { viewModel.setShowStayCalculator(true) },
                                        onOpenQrScan = { viewModel.setShowQrScanDialog(true) },
                                        onOpenSos = { viewModel.triggerEmergencySos("Room 101, HSR Grand") }
                                    )
                                }
                                UserRole.ADMIN -> {
                                    AdminDashboardScreen(
                                        rooms = rooms,
                                        beds = beds,
                                        tenants = tenants,
                                        payments = payments,
                                        complaints = complaints,
                                        visitors = visitors,
                                        utilityMeters = utilityMeters,
                                        onUpdateComplaintStatus = { id, st, notes -> viewModel.updateComplaintStatus(id, st, notes) },
                                        onUpdateVisitorStatus = { id, st -> viewModel.updateVisitorStatus(id, st) },
                                        onOpenAiForecast = { viewModel.setShowAiForecastSheet(true) }
                                    )
                                }
                                UserRole.OWNER -> {
                                    OwnerDashboardScreen(
                                        branches = branches,
                                        onOpenAiForecast = { viewModel.setShowAiForecastSheet(true) },
                                        onOpenWebPortal = { viewModel.setShowWebPortalViewer(true) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Modals & Bottom Sheets
                if (showAiChatSheet) {
                    AiChatbotSheet(
                        messages = chatMessages,
                        isLoading = isAiGenerating,
                        onSendMessage = { viewModel.sendAiMessage(it) },
                        onDismiss = { viewModel.setShowAiChatSheet(false) },
                        sheetState = aiSheetState
                    )
                }

                if (showAiForecastSheet) {
                    AiForecastSheet(
                        onDismiss = { viewModel.setShowAiForecastSheet(false) },
                        sheetState = forecastSheetState
                    )
                }

                if (showStayCalculator) {
                    HourlyStayCalculatorDialog(
                        onDismiss = { viewModel.setShowStayCalculator(false) }
                    )
                }

                if (showQrScanDialog) {
                    QrScannerFaceDialog(
                        onDismiss = { viewModel.setShowQrScanDialog(false) },
                        onSuccess = { viewModel.toggleAttendance() }
                    )
                }

                if (showSosDialog) {
                    EmergencySosDialog(
                        onDismiss = { viewModel.setShowSosDialog(false) }
                    )
                }
            }
        }
    }
}
