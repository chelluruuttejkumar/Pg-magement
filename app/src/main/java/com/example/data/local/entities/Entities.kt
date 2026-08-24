package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.AttendanceStatus
import com.example.model.BedStatus
import com.example.model.ComplaintStatus
import com.example.model.PaymentStatus
import com.example.model.RoomType
import com.example.model.StayType
import com.example.model.VehicleType
import com.example.model.VisitorStatus

@Entity(tableName = "branches")
data class BranchEntity(
    @PrimaryKey val id: String,
    val name: String,
    val city: String,
    val address: String,
    val totalRooms: Int,
    val totalBeds: Int,
    val occupiedBeds: Int,
    val monthlyRevenue: Double,
    val managerName: String,
    val managerPhone: String
)

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey val id: String,
    val branchId: String,
    val roomNumber: String,
    val floorNumber: Int,
    val roomType: RoomType,
    val totalBeds: Int,
    val availableBeds: Int,
    val baseRent: Double,
    val hasAc: Boolean,
    val hasBalcony: Boolean,
    val hasAttachedWashroom: Boolean
)

@Entity(tableName = "beds")
data class BedEntity(
    @PrimaryKey val id: String,
    val roomId: String,
    val roomNumber: String,
    val bedCode: String, // e.g. 101-A, 101-B
    val status: BedStatus,
    val tenantId: String? = null,
    val tenantName: String? = null,
    val rateMonthly: Double
)

@Entity(tableName = "tenants")
data class TenantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val roomNumber: String,
    val bedCode: String,
    val branchId: String,
    val stayType: StayType,
    val checkInDate: String,
    val monthlyRent: Double,
    val depositAmount: Double,
    val duesAmount: Double,
    val kycVerified: Boolean,
    val emergencyContact: String,
    val attendanceStatus: AttendanceStatus = AttendanceStatus.CHECKED_IN,
    val lastCheckInTime: String = "Today, 08:30 AM"
)

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey val id: String,
    val tenantId: String,
    val tenantName: String,
    val invoiceNumber: String,
    val title: String,
    val amount: Double,
    val taxGst: Double,
    val totalAmount: Double,
    val status: PaymentStatus,
    val dueDate: String,
    val paidDate: String? = null,
    val paymentMode: String? = null // UPI, Credit Card, NetBanking, Cash
)

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey val id: String,
    val tenantId: String,
    val tenantName: String,
    val roomNumber: String,
    val category: String, // Electrical, Plumbing, WiFi, Housekeeping, Food, Other
    val title: String,
    val description: String,
    val status: ComplaintStatus,
    val priority: String, // High, Medium, Normal
    val createdAt: String,
    val resolutionNotes: String? = null
)

@Entity(tableName = "visitors")
data class VisitorEntity(
    @PrimaryKey val id: String,
    val tenantId: String,
    val tenantName: String,
    val roomNumber: String,
    val visitorName: String,
    val visitorPhone: String,
    val purpose: String,
    val visitDate: String,
    val expectedInTime: String,
    val status: VisitorStatus,
    val checkInTime: String? = null,
    val checkOutTime: String? = null
)

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val id: String,
    val tenantId: String,
    val tenantName: String,
    val roomNumber: String,
    val vehicleType: VehicleType,
    val vehicleNumber: String,
    val modelName: String,
    val parkingSlotCode: String, // e.g. P1-B04, P1-C12
    val entryTime: String,
    val isParked: Boolean
)

@Entity(tableName = "food_orders")
data class FoodOrderEntity(
    @PrimaryKey val id: String,
    val tenantId: String,
    val planType: String, // Monthly 3-Meals, Weekend Special, Daily Lunch
    val mealDate: String,
    val mealType: String, // Breakfast, Lunch, Dinner
    val menuItems: String,
    val isOptedOut: Boolean = false,
    val status: String = "Confirmed"
)

@Entity(tableName = "laundry_orders")
data class LaundryOrderEntity(
    @PrimaryKey val id: String,
    val tenantId: String,
    val roomNumber: String,
    val clothesCount: Int,
    val serviceType: String, // Wash & Fold, Steam Iron, Dry Clean
    val pickupDate: String,
    val status: String, // Scheduled, In Progress, Ready, Delivered
    val charges: Double
)

@Entity(tableName = "utility_meters")
data class UtilityMeterEntity(
    @PrimaryKey val id: String,
    val roomNumber: String,
    val meterType: String, // Electricity, Water, Gas, High-Speed WiFi
    val currentReading: Double,
    val previousReading: Double,
    val unitRate: Double,
    val totalBill: Double,
    val lastRecordedDate: String
)

@Entity(tableName = "disclaimer_acceptances")
data class DisclaimerAcceptanceEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val deviceModel: String,
    val ipAddress: String,
    val latitude: Double,
    val longitude: Double,
    val locationName: String,
    val acceptedTimestamp: Long,
    val acceptedIsoDate: String,
    val allCheckboxesAcknowledged: Boolean
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val category: String, // RENT_DUE, VISITOR, FOOD, COMPLAINT, SOS, PARKING
    val timestamp: String,
    val isRead: Boolean = false
)
