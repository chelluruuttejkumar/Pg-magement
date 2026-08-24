package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        BranchEntity::class,
        RoomEntity::class,
        BedEntity::class,
        TenantEntity::class,
        PaymentEntity::class,
        ComplaintEntity::class,
        VisitorEntity::class,
        VehicleEntity::class,
        FoodOrderEntity::class,
        LaundryOrderEntity::class,
        UtilityMeterEntity::class,
        DisclaimerAcceptanceEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pgDao(): PgDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pg_master_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
