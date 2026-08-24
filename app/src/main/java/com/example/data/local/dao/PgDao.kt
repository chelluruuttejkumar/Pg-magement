package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.BranchEntity
import com.example.data.local.entities.BedEntity
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
import kotlinx.coroutines.flow.Flow

@Dao
interface PgDao {

    // Branches
    @Query("SELECT * FROM branches")
    fun getAllBranches(): Flow<List<BranchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBranches(branches: List<BranchEntity>)

    // Rooms & Beds
    @Query("SELECT * FROM rooms ORDER BY floorNumber ASC, roomNumber ASC")
    fun getAllRooms(): Flow<List<RoomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(rooms: List<RoomEntity>)

    @Query("SELECT * FROM beds")
    fun getAllBeds(): Flow<List<BedEntity>>

    @Query("SELECT * FROM beds WHERE roomId = :roomId")
    fun getBedsForRoom(roomId: String): Flow<List<BedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeds(beds: List<BedEntity>)

    @Update
    suspend fun updateBed(bed: BedEntity)

    // Tenants
    @Query("SELECT * FROM tenants")
    fun getAllTenants(): Flow<List<TenantEntity>>

    @Query("SELECT * FROM tenants WHERE id = :id LIMIT 1")
    fun getTenantById(id: String): Flow<TenantEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenants(tenants: List<TenantEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTenant(tenant: TenantEntity)

    @Update
    suspend fun updateTenant(tenant: TenantEntity)

    // Payments & Invoices
    @Query("SELECT * FROM payments ORDER BY dueDate DESC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE tenantId = :tenantId ORDER BY dueDate DESC")
    fun getPaymentsForTenant(tenantId: String): Flow<List<PaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<PaymentEntity>)

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    // Complaints
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Query("SELECT * FROM complaints WHERE tenantId = :tenantId ORDER BY createdAt DESC")
    fun getComplaintsForTenant(tenantId: String): Flow<List<ComplaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaints(complaints: List<ComplaintEntity>)

    @Update
    suspend fun updateComplaint(complaint: ComplaintEntity)

    // Visitors
    @Query("SELECT * FROM visitors ORDER BY visitDate DESC")
    fun getAllVisitors(): Flow<List<VisitorEntity>>

    @Query("SELECT * FROM visitors WHERE tenantId = :tenantId ORDER BY visitDate DESC")
    fun getVisitorsForTenant(tenantId: String): Flow<List<VisitorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitor(visitor: VisitorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitors(visitors: List<VisitorEntity>)

    @Update
    suspend fun updateVisitor(visitor: VisitorEntity)

    // Vehicles
    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicles(vehicles: List<VehicleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    // Food Orders
    @Query("SELECT * FROM food_orders")
    fun getAllFoodOrders(): Flow<List<FoodOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodOrders(orders: List<FoodOrderEntity>)

    // Laundry
    @Query("SELECT * FROM laundry_orders")
    fun getAllLaundryOrders(): Flow<List<LaundryOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaundryOrder(order: LaundryOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaundryOrders(orders: List<LaundryOrderEntity>)

    // Utility Meters
    @Query("SELECT * FROM utility_meters")
    fun getAllUtilityMeters(): Flow<List<UtilityMeterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUtilityMeters(meters: List<UtilityMeterEntity>)

    // Disclaimer Acceptance
    @Query("SELECT * FROM disclaimer_acceptances WHERE userId = :userId LIMIT 1")
    suspend fun getDisclaimerAcceptance(userId: String): DisclaimerAcceptanceEntity?

    @Query("SELECT * FROM disclaimer_acceptances")
    fun getAllDisclaimerAcceptances(): Flow<List<DisclaimerAcceptanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDisclaimerAcceptance(disclaimer: DisclaimerAcceptanceEntity)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)
}
