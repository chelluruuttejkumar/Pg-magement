package com.example.data.repository

import com.example.data.local.dao.PgDao
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
import com.example.model.AttendanceStatus
import com.example.model.BedStatus
import com.example.model.ComplaintStatus
import com.example.model.PaymentStatus
import com.example.model.RoomType
import com.example.model.StayType
import com.example.model.VehicleType
import com.example.model.VisitorStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PgRepository(private val dao: PgDao) {

    private val _liveEventFeed = MutableSharedFlow<String>(extraBufferCapacity = 20)
    val liveEventFeed = _liveEventFeed.asSharedFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedInitialDataIfEmpty()
        }
    }

    val branches: Flow<List<BranchEntity>> = dao.getAllBranches()
    val rooms: Flow<List<RoomEntity>> = dao.getAllRooms()
    val beds: Flow<List<BedEntity>> = dao.getAllBeds()
    val tenants: Flow<List<TenantEntity>> = dao.getAllTenants()
    val payments: Flow<List<PaymentEntity>> = dao.getAllPayments()
    val complaints: Flow<List<ComplaintEntity>> = dao.getAllComplaints()
    val visitors: Flow<List<VisitorEntity>> = dao.getAllVisitors()
    val vehicles: Flow<List<VehicleEntity>> = dao.getAllVehicles()
    val foodOrders: Flow<List<FoodOrderEntity>> = dao.getAllFoodOrders()
    val laundryOrders: Flow<List<LaundryOrderEntity>> = dao.getAllLaundryOrders()
    val utilityMeters: Flow<List<UtilityMeterEntity>> = dao.getAllUtilityMeters()
    val notifications: Flow<List<NotificationEntity>> = dao.getAllNotifications()

    suspend fun getDisclaimerAcceptance(userId: String): DisclaimerAcceptanceEntity? {
        return dao.getDisclaimerAcceptance(userId)
    }

    suspend fun saveDisclaimerAcceptance(acceptance: DisclaimerAcceptanceEntity) {
        dao.insertDisclaimerAcceptance(acceptance)
        _liveEventFeed.tryEmit("Statutory Disclaimer Accepted by ${acceptance.userName} (${acceptance.deviceModel})")
    }

    suspend fun payRent(paymentId: String, mode: String) {
        val paymentList = dao.getAllPayments().firstOrNull() ?: return
        val item = paymentList.find { it.id == paymentId } ?: return
        val updated = item.copy(
            status = PaymentStatus.PAID,
            paidDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date()),
            paymentMode = mode
        )
        dao.updatePayment(updated)

        // update tenant dues
        val tenantList = dao.getAllTenants().firstOrNull() ?: return
        val tenant = tenantList.find { it.id == item.tenantId }
        if (tenant != null) {
            val newDues = (tenant.duesAmount - item.totalAmount).coerceAtLeast(0.0)
            dao.updateTenant(tenant.copy(duesAmount = newDues))
        }

        val notif = NotificationEntity(
            id = UUID.randomUUID().toString(),
            title = "Payment Successful",
            message = "Received ₹${item.totalAmount.toInt()} via $mode for ${item.title}",
            category = "PAYMENT",
            timestamp = "Just now"
        )
        dao.insertNotification(notif)
        _liveEventFeed.tryEmit("Rent Payment ₹${item.totalAmount.toInt()} confirmed for Room ${tenant?.roomNumber ?: "Unit"}")
    }

    suspend fun raiseComplaint(
        tenantId: String,
        tenantName: String,
        roomNumber: String,
        category: String,
        title: String,
        description: String,
        priority: String
    ) {
        val complaint = ComplaintEntity(
            id = UUID.randomUUID().toString(),
            tenantId = tenantId,
            tenantName = tenantName,
            roomNumber = roomNumber,
            category = category,
            title = title,
            description = description,
            status = ComplaintStatus.OPEN,
            priority = priority,
            createdAt = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        )
        dao.insertComplaint(complaint)

        val notif = NotificationEntity(
            id = UUID.randomUUID().toString(),
            title = "New Ticket #PG-${complaint.id.take(4).uppercase()}",
            message = "$category complaint registered by $tenantName (Room $roomNumber)",
            category = "COMPLAINT",
            timestamp = "Just now"
        )
        dao.insertNotification(notif)
        _liveEventFeed.tryEmit("New Ticket Registered: $title in Room $roomNumber (Priority: $priority)")
    }

    suspend fun updateComplaintStatus(complaintId: String, newStatus: ComplaintStatus, notes: String?) {
        val complaintsList = dao.getAllComplaints().firstOrNull() ?: return
        val comp = complaintsList.find { it.id == complaintId } ?: return
        dao.updateComplaint(comp.copy(status = newStatus, resolutionNotes = notes ?: comp.resolutionNotes))
        _liveEventFeed.tryEmit("Ticket #${comp.id.take(4).uppercase()} status updated to $newStatus")
    }

    suspend fun requestVisitorPass(
        tenantId: String,
        tenantName: String,
        roomNumber: String,
        visitorName: String,
        visitorPhone: String,
        purpose: String,
        visitDate: String,
        expectedTime: String
    ) {
        val visitor = VisitorEntity(
            id = UUID.randomUUID().toString(),
            tenantId = tenantId,
            tenantName = tenantName,
            roomNumber = roomNumber,
            visitorName = visitorName,
            visitorPhone = visitorPhone,
            purpose = purpose,
            visitDate = visitDate,
            expectedInTime = expectedTime,
            status = VisitorStatus.APPROVED // auto-approved pass with QR
        )
        dao.insertVisitor(visitor)

        val notif = NotificationEntity(
            id = UUID.randomUUID().toString(),
            title = "Visitor Pass Approved",
            message = "Digital QR pass generated for $visitorName visiting Room $roomNumber on $visitDate",
            category = "VISITOR",
            timestamp = "Just now"
        )
        dao.insertNotification(notif)
        _liveEventFeed.tryEmit("Visitor Pass Issued: $visitorName for Room $roomNumber")
    }

    suspend fun updateVisitorStatus(visitorId: String, status: VisitorStatus) {
        val list = dao.getAllVisitors().firstOrNull() ?: return
        val v = list.find { it.id == visitorId } ?: return
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val updated = when (status) {
            VisitorStatus.CHECKED_IN -> v.copy(status = status, checkInTime = timeNow)
            VisitorStatus.CHECKED_OUT -> v.copy(status = status, checkOutTime = timeNow)
            else -> v.copy(status = status)
        }
        dao.updateVisitor(updated)
        _liveEventFeed.tryEmit("Visitor ${v.visitorName} marked as $status at $timeNow")
    }

    suspend fun registerVehicle(
        tenantId: String,
        tenantName: String,
        roomNumber: String,
        type: VehicleType,
        number: String,
        model: String,
        slot: String
    ) {
        val v = VehicleEntity(
            id = UUID.randomUUID().toString(),
            tenantId = tenantId,
            tenantName = tenantName,
            roomNumber = roomNumber,
            vehicleType = type,
            vehicleNumber = number.uppercase(),
            modelName = model,
            parkingSlotCode = slot,
            entryTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
            isParked = true
        )
        dao.insertVehicle(v)
        _liveEventFeed.tryEmit("Vehicle $number (${type.label}) assigned to slot $slot")
    }

    suspend fun requestLaundry(tenantId: String, roomNumber: String, clothes: Int, service: String) {
        val rate = when (service) {
            "Steam Iron" -> 15.0
            "Dry Clean" -> 70.0
            else -> 25.0 // Wash & Fold
        }
        val order = LaundryOrderEntity(
            id = UUID.randomUUID().toString(),
            tenantId = tenantId,
            roomNumber = roomNumber,
            clothesCount = clothes,
            serviceType = service,
            pickupDate = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date()),
            status = "Scheduled",
            charges = clothes * rate
        )
        dao.insertLaundryOrder(order)
        _liveEventFeed.tryEmit("Laundry Pickup Scheduled for Room $roomNumber ($clothes clothes)")
    }

    suspend fun toggleAttendance(tenantId: String) {
        val list = dao.getAllTenants().firstOrNull() ?: return
        val tenant = list.find { it.id == tenantId } ?: return
        val newStatus = if (tenant.attendanceStatus == AttendanceStatus.CHECKED_IN) {
            AttendanceStatus.CHECKED_OUT
        } else {
            AttendanceStatus.CHECKED_IN
        }
        val timeNow = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        dao.updateTenant(tenant.copy(attendanceStatus = newStatus, lastCheckInTime = "Today, $timeNow"))
        _liveEventFeed.tryEmit("Smart Access: ${tenant.name} ${if (newStatus == AttendanceStatus.CHECKED_IN) "Entered PG (Checked-In)" else "Exited PG (Checked-Out)"}")
    }

    suspend fun triggerEmergencySos(tenantName: String, roomNumber: String, locationText: String) {
        val notif = NotificationEntity(
            id = UUID.randomUUID().toString(),
            title = "🚨 EMERGENCY SOS BROADCAST",
            message = "Tenant $tenantName in Room $roomNumber triggered emergency alert ($locationText). Emergency response dispatched!",
            category = "SOS",
            timestamp = "Right now"
        )
        dao.insertNotification(notif)
        _liveEventFeed.tryEmit("🚨 CRITICAL SOS ALERT: Room $roomNumber ($tenantName) requested immediate security/medical assistance!")
    }

    private suspend fun seedInitialDataIfEmpty() {
        val existingBranches = dao.getAllBranches().firstOrNull()
        if (!existingBranches.isNullOrEmpty()) return

        // 1. Branches
        val seedBranches = listOf(
            BranchEntity(
                id = "branch-1",
                name = "PG Master - HSR Grand",
                city = "Bengaluru",
                address = "Sector 2, 27th Main Rd, HSR Layout, Bengaluru, Karnataka 560102",
                totalRooms = 24,
                totalBeds = 68,
                occupiedBeds = 58,
                monthlyRevenue = 782000.0,
                managerName = "Rajesh Sharma",
                managerPhone = "+91 98765 43210"
            ),
            BranchEntity(
                id = "branch-2",
                name = "PG Master - Cyber Nest",
                city = "Hyderabad",
                address = "Hitec City, Madhapur, Hyderabad, Telangana 500081",
                totalRooms = 32,
                totalBeds = 90,
                occupiedBeds = 82,
                monthlyRevenue = 1025000.0,
                managerName = "Praveen Rao",
                managerPhone = "+91 98765 43211"
            ),
            BranchEntity(
                id = "branch-3",
                name = "PG Master - Hinjewadi Heights",
                city = "Pune",
                address = "Phase 1, Hinjewadi Rajiv Gandhi Infotech Park, Pune 411057",
                totalRooms = 18,
                totalBeds = 48,
                occupiedBeds = 39,
                monthlyRevenue = 490000.0,
                managerName = "Anil Kulkarni",
                managerPhone = "+91 98765 43212"
            )
        )
        dao.insertBranches(seedBranches)

        // 2. Rooms
        val seedRooms = listOf(
            RoomEntity("r-101", "branch-1", "101", 1, RoomType.DOUBLE_SHARING, 2, 0, 12500.0, true, true, true),
            RoomEntity("r-102", "branch-1", "102", 1, RoomType.DOUBLE_SHARING, 2, 1, 12500.0, true, false, true),
            RoomEntity("r-103", "branch-1", "103", 1, RoomType.SINGLE, 1, 0, 18500.0, true, true, true),
            RoomEntity("r-104", "branch-1", "104", 1, RoomType.TRIPLE_SHARING, 3, 1, 9500.0, false, false, true),
            RoomEntity("r-201", "branch-1", "201", 2, RoomType.DOUBLE_SHARING, 2, 0, 13000.0, true, true, true),
            RoomEntity("r-202", "branch-1", "202", 2, RoomType.PREMIUM_STUDIO, 1, 0, 22000.0, true, true, true),
            RoomEntity("r-203", "branch-1", "203", 2, RoomType.DOUBLE_SHARING, 2, 2, 12500.0, true, false, true),
            RoomEntity("r-204", "branch-1", "204", 2, RoomType.FOUR_SHARING, 4, 1, 8000.0, false, true, true),
            RoomEntity("r-301", "branch-1", "301", 3, RoomType.DOUBLE_SHARING, 2, 0, 13000.0, true, true, true),
            RoomEntity("r-302", "branch-1", "302", 3, RoomType.SINGLE, 1, 1, 19000.0, true, true, true),
            RoomEntity("r-303", "branch-1", "303", 3, RoomType.DOUBLE_SHARING, 2, 0, 12500.0, true, false, true),
            RoomEntity("r-401", "branch-1", "401", 4, RoomType.PREMIUM_STUDIO, 1, 0, 24000.0, true, true, true)
        )
        dao.insertRooms(seedRooms)

        // 3. Beds
        val seedBeds = listOf(
            BedEntity("b-101A", "r-101", "101", "101-A", BedStatus.OCCUPIED, "t-001", "Aarav Sharma (You)", 12500.0),
            BedEntity("b-101B", "r-101", "101", "101-B", BedStatus.OCCUPIED, "t-002", "Rohan Mehta", 12500.0),
            BedEntity("b-102A", "r-102", "102", "102-A", BedStatus.OCCUPIED, "t-003", "Vikram Reddy", 12500.0),
            BedEntity("b-102B", "r-102", "102", "102-B", BedStatus.VACANT, null, null, 12500.0),
            BedEntity("b-103A", "r-103", "103", "103-A", BedStatus.OCCUPIED, "t-004", "Siddharth Verma", 18500.0),
            BedEntity("b-104A", "r-104", "104", "104-A", BedStatus.OCCUPIED, "t-005", "Kiran Nair", 9500.0),
            BedEntity("b-104B", "r-104", "104", "104-B", BedStatus.OCCUPIED, "t-006", "Deepak Patel", 9500.0),
            BedEntity("b-104C", "r-104", "104", "104-C", BedStatus.RESERVED, null, null, 9500.0),
            BedEntity("b-201A", "r-201", "201", "201-A", BedStatus.OCCUPIED, "t-007", "Aditya Joshi", 13000.0),
            BedEntity("b-201B", "r-201", "201", "201-B", BedStatus.OCCUPIED, "t-008", "Manish Sen", 13000.0),
            BedEntity("b-202A", "r-202", "202", "202-A", BedStatus.OCCUPIED, "t-009", "Rahul Roy", 22000.0),
            BedEntity("b-203A", "r-203", "203", "203-A", BedStatus.VACANT, null, null, 12500.0),
            BedEntity("b-203B", "r-203", "203", "203-B", BedStatus.VACANT, null, null, 12500.0),
            BedEntity("b-301A", "r-301", "301", "301-A", BedStatus.OCCUPIED, "t-010", "Gautam Rao", 13000.0),
            BedEntity("b-302A", "r-302", "302", "302-A", BedStatus.VACANT, null, null, 19000.0),
            BedEntity("b-401A", "r-401", "401", "401-A", BedStatus.OCCUPIED, "t-011", "Kabir Gupta", 24000.0)
        )
        dao.insertBeds(seedBeds)

        // 4. Primary Tenant & Peers
        val seedTenants = listOf(
            TenantEntity(
                id = "t-001",
                name = "Aarav Sharma",
                email = "aarav.sharma@example.com",
                phone = "+91 98450 11223",
                roomNumber = "101",
                bedCode = "101-A",
                branchId = "branch-1",
                stayType = StayType.MONTHLY,
                checkInDate = "15 Jan 2026",
                monthlyRent = 12500.0,
                depositAmount = 25000.0,
                duesAmount = 12500.0,
                kycVerified = true,
                emergencyContact = "+91 98450 99887 (Father)"
            ),
            TenantEntity(
                id = "t-002",
                name = "Rohan Mehta",
                email = "rohan.m@example.com",
                phone = "+91 97410 44556",
                roomNumber = "101",
                bedCode = "101-B",
                branchId = "branch-1",
                stayType = StayType.MONTHLY,
                checkInDate = "01 Feb 2026",
                monthlyRent = 12500.0,
                depositAmount = 25000.0,
                duesAmount = 0.0,
                kycVerified = true,
                emergencyContact = "+91 97410 88990"
            ),
            TenantEntity(
                id = "t-003",
                name = "Vikram Reddy",
                email = "vikram.r@example.com",
                phone = "+91 99880 77665",
                roomNumber = "102",
                bedCode = "102-A",
                branchId = "branch-1",
                stayType = StayType.MONTHLY,
                checkInDate = "10 Nov 2025",
                monthlyRent = 12500.0,
                depositAmount = 25000.0,
                duesAmount = 0.0,
                kycVerified = true,
                emergencyContact = "+91 99880 11223"
            )
        )
        dao.insertTenants(seedTenants)

        // 5. Invoices & Payments
        val seedPayments = listOf(
            PaymentEntity(
                id = "pay-001",
                tenantId = "t-001",
                tenantName = "Aarav Sharma",
                invoiceNumber = "INV-2026-08-01",
                title = "Monthly Rent + Maintenance (Aug 2026)",
                amount = 12500.0,
                taxGst = 1500.0,
                totalAmount = 14000.0,
                status = PaymentStatus.PENDING,
                dueDate = "05 Sep 2026"
            ),
            PaymentEntity(
                id = "pay-002",
                tenantId = "t-001",
                tenantName = "Aarav Sharma",
                invoiceNumber = "INV-2026-07-01",
                title = "Monthly Rent + Electricity (Jul 2026)",
                amount = 12500.0,
                taxGst = 1500.0,
                totalAmount = 14000.0,
                status = PaymentStatus.PAID,
                dueDate = "05 Aug 2026",
                paidDate = "02 Aug 2026, 11:30 AM",
                paymentMode = "UPI (Google Pay)"
            ),
            PaymentEntity(
                id = "pay-003",
                tenantId = "t-001",
                tenantName = "Aarav Sharma",
                invoiceNumber = "INV-2026-06-01",
                title = "Security Deposit & Onboarding Fee",
                amount = 25000.0,
                taxGst = 0.0,
                totalAmount = 25000.0,
                status = PaymentStatus.PAID,
                dueDate = "15 Jan 2026",
                paidDate = "15 Jan 2026, 09:15 AM",
                paymentMode = "Net Banking"
            )
        )
        dao.insertPayments(seedPayments)

        // 6. Complaints
        val seedComplaints = listOf(
            ComplaintEntity(
                id = "cmp-001",
                tenantId = "t-001",
                tenantName = "Aarav Sharma",
                roomNumber = "101",
                category = "WiFi & Internet",
                title = "5GHz WiFi disconnecting frequently in 101",
                description = "Primary router in 1st floor corridor drops connection during evening peak hours.",
                status = ComplaintStatus.IN_PROGRESS,
                priority = "High",
                createdAt = "22 Aug 2026, 04:15 PM",
                resolutionNotes = "Network technician assigned (Airtel Fiber ticket #94821)."
            ),
            ComplaintEntity(
                id = "cmp-002",
                tenantId = "t-001",
                tenantName = "Aarav Sharma",
                roomNumber = "101",
                category = "Housekeeping",
                title = "Attached washroom deep cleaning request",
                description = "Requesting weekly deep cleaning & sanitization for room 101 washroom.",
                status = ComplaintStatus.RESOLVED,
                priority = "Normal",
                createdAt = "18 Aug 2026, 10:00 AM",
                resolutionNotes = "Completed on 18 Aug by housekeeping supervisor Suman."
            )
        )
        dao.insertComplaints(seedComplaints)

        // 7. Visitors
        val seedVisitors = listOf(
            VisitorEntity(
                id = "vis-001",
                tenantId = "t-001",
                tenantName = "Aarav Sharma",
                roomNumber = "101",
                visitorName = "Kunal Kapoor",
                visitorPhone = "+91 98111 22334",
                purpose = "College Project Work",
                visitDate = "24 Aug 2026",
                expectedInTime = "05:00 PM",
                status = VisitorStatus.APPROVED
            )
        )
        dao.insertVisitors(seedVisitors)

        // 8. Vehicles & Parking
        val seedVehicles = listOf(
            VehicleEntity(
                id = "veh-001",
                tenantId = "t-001",
                tenantName = "Aarav Sharma",
                roomNumber = "101",
                vehicleType = VehicleType.BIKE,
                vehicleNumber = "KA 01 EK 4589",
                modelName = "Royal Enfield Hunter 350",
                parkingSlotCode = "B-04 (Basement 1)",
                entryTime = "08:15 AM",
                isParked = true
            ),
            VehicleEntity(
                id = "veh-002",
                tenantId = "t-003",
                tenantName = "Vikram Reddy",
                roomNumber = "102",
                vehicleType = VehicleType.EV,
                vehicleNumber = "KA 05 EM 1204",
                modelName = "Ather 450X (EV Charging Slot)",
                parkingSlotCode = "EV-02 (Fast Charger)",
                entryTime = "07:30 AM",
                isParked = true
            ),
            VehicleEntity(
                id = "veh-003",
                tenantId = "t-004",
                tenantName = "Siddharth Verma",
                roomNumber = "103",
                vehicleType = VehicleType.CAR,
                vehicleNumber = "KA 03 MN 7812",
                modelName = "Tata Nexon EV",
                parkingSlotCode = "C-01 (Ground Floor)",
                entryTime = "Yesterday, 09:40 PM",
                isParked = true
            )
        )
        dao.insertVehicles(seedVehicles)

        // 9. Food & Laundry
        val seedFood = listOf(
            FoodOrderEntity("f-001", "t-001", "Monthly 3-Meals Plan (Veg + Non-Veg)", "Today", "Lunch", "Paneer Butter Masala, Dal Tadka, Phulkas, Steamed Rice, Gulab Jamun"),
            FoodOrderEntity("f-002", "t-001", "Monthly 3-Meals Plan (Veg + Non-Veg)", "Today", "Dinner", "Chicken Curry / Kadhai Paneer, Jeera Rice, Tandoori Roti, Salad")
        )
        dao.insertFoodOrders(seedFood)

        val seedLaundry = listOf(
            LaundryOrderEntity("l-001", "t-001", "101", 8, "Wash & Fold", "21 Aug 2026", "Ready", 200.0),
            LaundryOrderEntity("l-002", "t-001", "101", 4, "Steam Iron", "24 Aug 2026", "In Progress", 60.0)
        )
        dao.insertLaundryOrders(seedLaundry)

        // 10. Utility Meters
        val seedMeters = listOf(
            UtilityMeterEntity("m-101-elec", "101", "Electricity (Sub-Meter)", 1482.0, 1340.0, 8.50, 1207.0, "20 Aug 2026"),
            UtilityMeterEntity("m-101-water", "101", "Smart Water Meter (L)", 6200.0, 5800.0, 0.05, 20.0, "20 Aug 2026"),
            UtilityMeterEntity("m-201-elec", "201", "Electricity (Sub-Meter)", 2105.0, 1920.0, 8.50, 1572.5, "20 Aug 2026"),
            UtilityMeterEntity("m-wifi", "All Floors", "Fiber WiFi Mesh (1 Gbps)", 840.0, 0.0, 0.0, 0.0, "Live 99.9% Uptime")
        )
        dao.insertUtilityMeters(seedMeters)

        // 11. Notifications
        val seedNotifs = listOf(
            NotificationEntity("n-001", "Rent Due Reminder", "Your rent for August 2026 (₹14,000 incl. GST) is due on 05 Sep.", "RENT_DUE", "1 hour ago"),
            NotificationEntity("n-002", "Today's Chef Menu Updated", "Special Biryani & Gulab Jamun for Sunday Dinner.", "FOOD", "3 hours ago"),
            NotificationEntity("n-003", "High-Speed WiFi Speed Boosted", "1 Gbps redundant Airtel & Jio fiber links active.", "ANNOUNCEMENT", "Yesterday")
        )
        dao.insertNotifications(seedNotifs)

        // 12. Disclaimer Acceptance (pre-acknowledged)
        val defaultDisclaimer = DisclaimerAcceptanceEntity(
            id = "disc-001",
            userId = "t-001",
            userName = "Aarav Sharma",
            deviceModel = "Android Device",
            ipAddress = "192.168.1.108",
            latitude = 12.9141,
            longitude = 77.6411,
            locationName = "HSR Layout Sector 2, Bengaluru",
            acceptedTimestamp = System.currentTimeMillis(),
            acceptedIsoDate = "2026-08-24T10:00:00Z",
            allCheckboxesAcknowledged = true
        )
        dao.insertDisclaimerAcceptance(defaultDisclaimer)
    }
}
