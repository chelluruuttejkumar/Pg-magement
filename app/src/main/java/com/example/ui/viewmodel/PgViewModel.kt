package com.example.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiAiService
import com.example.data.local.AppDatabase
import com.example.data.local.entities.BedEntity
import com.example.data.local.entities.BranchEntity
import com.example.data.local.entities.ComplaintEntity
import com.example.data.local.entities.DisclaimerAcceptanceEntity
import com.example.data.local.entities.FoodOrderEntity
import com.example.data.local.entities.LaundryOrderEntity
import com.example.data.local.entities.NotificationEntity
import com.example.data.local.entities.PaymentEntity
import com.example.data.local.entities.RoomEntity
import com.example.data.local.entities.TenantEntity
import com.example.data.local.entities.UtilityMeterEntity
import com.example.data.local.entities.VehicleEntity
import com.example.data.local.entities.VisitorEntity
import com.example.data.repository.PgRepository
import com.example.model.ComplaintStatus
import com.example.model.Language
import com.example.model.Localization
import com.example.model.StayType
import com.example.model.UserRole
import com.example.model.VehicleType
import com.example.model.VisitorStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val time: String = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
)

class PgViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PgRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PgRepository(database.pgDao())
    }

    val branches: StateFlow<List<BranchEntity>> = repository.branches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rooms: StateFlow<List<RoomEntity>> = repository.rooms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val beds: StateFlow<List<BedEntity>> = repository.beds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tenants: StateFlow<List<TenantEntity>> = repository.tenants
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val payments: StateFlow<List<PaymentEntity>> = repository.payments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val complaints: StateFlow<List<ComplaintEntity>> = repository.complaints
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val visitors: StateFlow<List<VisitorEntity>> = repository.visitors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vehicles: StateFlow<List<VehicleEntity>> = repository.vehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val foodOrders: StateFlow<List<FoodOrderEntity>> = repository.foodOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val laundryOrders: StateFlow<List<LaundryOrderEntity>> = repository.laundryOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val utilityMeters: StateFlow<List<UtilityMeterEntity>> = repository.utilityMeters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI State
    private val _currentRole = MutableStateFlow(UserRole.TENANT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _selectedBranchId = MutableStateFlow("branch-1")
    val selectedBranchId: StateFlow<String> = _selectedBranchId.asStateFlow()

    private val _isDisclaimerAccepted = MutableStateFlow(true)
    val isDisclaimerAccepted: StateFlow<Boolean> = _isDisclaimerAccepted.asStateFlow()

    private val _showDisclaimerModal = MutableStateFlow(false)
    val showDisclaimerModal: StateFlow<Boolean> = _showDisclaimerModal.asStateFlow()

    private val _showSosDialog = MutableStateFlow(false)
    val showSosDialog: StateFlow<Boolean> = _showSosDialog.asStateFlow()

    private val _showQrScanDialog = MutableStateFlow(false)
    val showQrScanDialog: StateFlow<Boolean> = _showQrScanDialog.asStateFlow()

    private val _showStayCalculator = MutableStateFlow(false)
    val showStayCalculator: StateFlow<Boolean> = _showStayCalculator.asStateFlow()

    private val _showAiChatSheet = MutableStateFlow(false)
    val showAiChatSheet: StateFlow<Boolean> = _showAiChatSheet.asStateFlow()

    private val _showAiForecastSheet = MutableStateFlow(false)
    val showAiForecastSheet: StateFlow<Boolean> = _showAiForecastSheet.asStateFlow()

    private val _showWebPortalViewer = MutableStateFlow(false)
    val showWebPortalViewer: StateFlow<Boolean> = _showWebPortalViewer.asStateFlow()

    private val _liveTicker = MutableStateFlow("⚡ Live System: All Smart Locks & Surveillance Systems Active • WiFi 1Gbps Healthy")
    val liveTicker: StateFlow<String> = _liveTicker.asStateFlow()

    // AI Chat State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                isUser = false,
                text = "Hello Aarav! I'm PG Master AI. You can ask me about rent breakdown, food menu, visitor passes, WiFi status, or room availability."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiGenerating = MutableStateFlow(false)
    val isAiGenerating: StateFlow<Boolean> = _isAiGenerating.asStateFlow()

    init {
        viewModelScope.launch {
            repository.liveEventFeed.collect { event ->
                _liveTicker.value = "⚡ $event"
            }
        }
        checkDisclaimerStatus()
    }

    private fun checkDisclaimerStatus() {
        viewModelScope.launch {
            val existing = repository.getDisclaimerAcceptance("t-001")
            _isDisclaimerAccepted.value = true
            _showDisclaimerModal.value = false
        }
    }

    fun setShowDisclaimerModal(show: Boolean) {
        _showDisclaimerModal.value = show
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setLanguage(language: Language) {
        _currentLanguage.value = language
    }

    fun setSelectedBranch(branchId: String) {
        _selectedBranchId.value = branchId
    }

    fun setShowSosDialog(show: Boolean) {
        _showSosDialog.value = show
    }

    fun setShowQrScanDialog(show: Boolean) {
        _showQrScanDialog.value = show
    }

    fun setShowStayCalculator(show: Boolean) {
        _showStayCalculator.value = show
    }

    fun setShowAiChatSheet(show: Boolean) {
        _showAiChatSheet.value = show
    }

    fun setShowAiForecastSheet(show: Boolean) {
        _showAiForecastSheet.value = show
    }

    fun setShowWebPortalViewer(show: Boolean) {
        _showWebPortalViewer.value = show
    }

    fun acceptDisclaimer(
        userName: String,
        deviceInfo: String,
        ipAddress: String,
        lat: Double,
        lng: Double,
        locationName: String
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val isoDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date(now))
            val acceptance = DisclaimerAcceptanceEntity(
                id = UUID.randomUUID().toString(),
                userId = "t-001",
                userName = userName,
                deviceModel = "${Build.MANUFACTURER} ${Build.MODEL} ($deviceInfo)",
                ipAddress = ipAddress,
                latitude = lat,
                longitude = lng,
                locationName = locationName,
                acceptedTimestamp = now,
                acceptedIsoDate = isoDate,
                allCheckboxesAcknowledged = true
            )
            repository.saveDisclaimerAcceptance(acceptance)
            _isDisclaimerAccepted.value = true
            _showDisclaimerModal.value = false
        }
    }

    fun sendAiMessage(promptText: String) {
        if (promptText.isBlank()) return
        val userMsg = ChatMessage(isUser = true, text = promptText)
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiGenerating.value = true

        viewModelScope.launch {
            val response = GeminiAiService.queryAssistant(promptText, _currentRole.value.displayName)
            val botMsg = ChatMessage(isUser = false, text = response)
            _chatMessages.value = _chatMessages.value + botMsg
            _isAiGenerating.value = false
        }
    }

    fun payRent(paymentId: String, paymentMode: String) {
        viewModelScope.launch {
            repository.payRent(paymentId, paymentMode)
        }
    }

    fun raiseComplaint(category: String, title: String, description: String, priority: String) {
        viewModelScope.launch {
            repository.raiseComplaint("t-001", "Aarav Sharma", "101", category, title, description, priority)
        }
    }

    fun updateComplaintStatus(complaintId: String, status: ComplaintStatus, notes: String? = null) {
        viewModelScope.launch {
            repository.updateComplaintStatus(complaintId, status, notes)
        }
    }

    fun requestVisitorPass(visitorName: String, phone: String, purpose: String, date: String, time: String) {
        viewModelScope.launch {
            repository.requestVisitorPass("t-001", "Aarav Sharma", "101", visitorName, phone, purpose, date, time)
        }
    }

    fun updateVisitorStatus(visitorId: String, status: VisitorStatus) {
        viewModelScope.launch {
            repository.updateVisitorStatus(visitorId, status)
        }
    }

    fun registerVehicle(type: VehicleType, number: String, model: String, slot: String) {
        viewModelScope.launch {
            repository.registerVehicle("t-001", "Aarav Sharma", "101", type, number, model, slot)
        }
    }

    fun requestLaundry(clothesCount: Int, service: String) {
        viewModelScope.launch {
            repository.requestLaundry("t-001", "101", clothesCount, service)
        }
    }

    fun toggleAttendance() {
        viewModelScope.launch {
            repository.toggleAttendance("t-001")
        }
    }

    fun triggerEmergencySos(locationText: String) {
        viewModelScope.launch {
            repository.triggerEmergencySos("Aarav Sharma", "101", locationText)
            _showSosDialog.value = true
        }
    }

    fun tr(key: String): String {
        return Localization.get(key, _currentLanguage.value)
    }
}
