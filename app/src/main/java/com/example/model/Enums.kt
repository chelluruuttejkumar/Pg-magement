package com.example.model

enum class UserRole(val displayName: String) {
    TENANT("Tenant"),
    ADMIN("Admin"),
    OWNER("Owner")
}

enum class Language(val code: String, val nativeName: String, val englishName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "हिन्दी", "Hindi"),
    TELUGU("te", "తెలుగు", "Telugu"),
    TAMIL("ta", "தமிழ்", "Tamil"),
    KANNADA("kn", "ಕನ್ನಡ", "Kannada"),
    MALAYALAM("ml", "മലയാളം", "Malayalam"),
    BENGALI("bn", "বাংলা", "Bengali"),
    MARATHI("mr", "मराठी", "Marathi")
}

enum class StayType(val label: String, val baseRate: Double, val durationUnit: String) {
    HOURLY("Hourly Stay", 150.0, "hrs"),
    DAILY("Daily Stay", 900.0, "days"),
    WEEKLY("Weekly Stay", 4500.0, "weeks"),
    MONTHLY("Monthly Stay", 11500.0, "months")
}

enum class RoomType(val label: String, val bedsCount: Int) {
    SINGLE("1-Sharing Private", 1),
    DOUBLE_SHARING("2-Sharing Executive", 2),
    TRIPLE_SHARING("3-Sharing Standard", 3),
    FOUR_SHARING("4-Sharing Economy", 4),
    PREMIUM_STUDIO("Premium Studio Suite", 1)
}

enum class BedStatus {
    VACANT, OCCUPIED, RESERVED, MAINTENANCE
}

enum class PaymentStatus {
    PAID, PENDING, OVERDUE
}

enum class ComplaintStatus {
    OPEN, IN_PROGRESS, RESOLVED
}

enum class VehicleType(val label: String, val iconName: String) {
    BIKE("Two Wheeler (Bike)", "bike"),
    CAR("Car / Sedan / SUV", "car"),
    EV("Electric Vehicle (EV)", "ev"),
    BICYCLE("Bicycle", "bicycle")
}

enum class VisitorStatus {
    PENDING, APPROVED, REJECTED, CHECKED_IN, CHECKED_OUT
}

enum class AttendanceStatus {
    CHECKED_IN, CHECKED_OUT, ON_LEAVE
}
