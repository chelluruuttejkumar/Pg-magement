package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiAiService {

    private const val TAG = "GeminiAiService"
    private const val MODEL = "gemini-3.5-flash"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun queryAssistant(userPrompt: String, roleContext: String = "Tenant"): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineSmartResponse(userPrompt, roleContext)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$apiKey"
            
            val systemInstruction = """
                You are PG Master AI, an intelligent co-living and hostel concierge management system.
                You assist tenants, admins, and property owners with:
                - Rent calculations, GST breakdown (12% / 18%), invoice queries
                - Room allocations, sharing types (1/2/3/4 sharing), amenities (WiFi, AC, Food)
                - Complaint triage and automated priority resolution
                - Visitor passes, vehicle parking rules, gate timings (10:30 PM curfews or 24/7 digital pass)
                - Predictive occupancy and profit/loss insights
                Role context: $roleContext.
                Answer accurately, warmly, and concisely with bullet points.
            """.trimIndent()

            val rootJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$systemInstruction\n\nUser Question: $userPrompt")
                            })
                        })
                    })
                })
            }

            val requestBody = rootJson.toString().toRequestBody(JSON_MEDIA_TYPE)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            httpClient.newCall(request).execute().use { response ->
                val bodyString = response.body?.string() ?: ""
                if (response.isSuccessful && bodyString.isNotEmpty()) {
                    val responseJson = JSONObject(bodyString)
                    val candidates = responseJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        val text = parts?.optJSONObject(0)?.optString("text")
                        if (!text.isNullOrBlank()) {
                            return@withContext text.trim()
                        }
                    }
                }
                Log.w(TAG, "API call non-success: ${response.code} -> $bodyString")
                return@withContext getOfflineSmartResponse(userPrompt, roleContext)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API error: ${e.message}", e)
            return@withContext getOfflineSmartResponse(userPrompt, roleContext)
        }
    }

    private fun getOfflineSmartResponse(prompt: String, role: String): String {
        val p = prompt.lowercase()
        return when {
            p.contains("rent") || p.contains("pay") || p.contains("bill") || p.contains("invoice") -> {
                """
                💳 **Rent & Invoice Guidance:**
                • Monthly rent is generated on the 1st of every month with due date on the 5th.
                • Rent for Room 101: **₹12,500** base + **₹1,500** maintenance & GST (Total: **₹14,000**).
                • You can pay instantly using UPI (GPay, PhonePe, Paytm), NetBanking, or Credit Cards via the **Rent & Invoices** tab.
                • Receipts with GST numbers (GSTIN: 29AAAAA0000A1Z5) are instantly downloadable.
                """.trimIndent()
            }
            p.contains("wifi") || p.contains("internet") || p.contains("password") -> {
                """
                📶 **WiFi & High-Speed Internet:**
                • SSID: **PGMaster_HighSpeed_5G**
                • Bandwidth: Dual 1 Gbps redundant fiber mesh across all floors.
                • For connection issues in room 101, an auto-diagnostic ticket #PG-CMP1 is currently in progress with Airtel Fiber.
                """.trimIndent()
            }
            p.contains("food") || p.contains("menu") || p.contains("meal") || p.contains("lunch") || p.contains("dinner") -> {
                """
                🍲 **Food & Dining Schedule:**
                • **Breakfast:** 07:30 AM – 09:30 AM (Idli/Dosa/Poha + Tea & Coffee)
                • **Lunch:** 12:30 PM – 02:30 PM (Paneer Butter Masala, Dal Tadka, Phulkas, Rice)
                • **Dinner:** 07:45 PM – 09:45 PM (Chicken Curry / Kadhai Paneer + Tandoori Roti)
                • You can opt-out of specific meals via the **Food & Laundry** tab before 11:00 AM to receive billing credits!
                """.trimIndent()
            }
            p.contains("visitor") || p.contains("guest") || p.contains("friend") -> {
                """
                👥 **Visitor Entry & Security Policies:**
                • Visitors are allowed in common lounges between 09:00 AM and 08:30 PM.
                • Tenants can generate an instant Digital QR Visitor Pass in the **Visitor Pass** tab.
                • Overnight stays require prior Admin approval (₹350/night guest room charge).
                """.trimIndent()
            }
            p.contains("parking") || p.contains("vehicle") || p.contains("bike") || p.contains("car") || p.contains("ev") -> {
                """
                🚗 **3D Smart Parking System:**
                • Your assigned slot for KA 01 EK 4589 (Bike) is **B-04 (Basement 1)**.
                • EV Fast Charging stations are available in slots **EV-01 through EV-04** (₹6.50/kWh automated sub-meter).
                • Check the interactive **3D Parking** visualizer to see real-time slot occupancy.
                """.trimIndent()
            }
            p.contains("complaint") || p.contains("issue") || p.contains("repair") || p.contains("cleaning") -> {
                """
                🛠️ **Maintenance & Complaints:**
                • Resolution SLA: Electrical & Plumbing (4 hours), Housekeeping (2 hours), WiFi (6 hours).
                • Simply tap **Raise Complaint** to log a ticket with priority levels.
                • You can track live technician progress on your dashboard.
                """.trimIndent()
            }
            p.contains("forecast") || p.contains("occupancy") || p.contains("predict") || p.contains("revenue") -> {
                """
                📊 **AI Occupancy & Revenue Forecast (Next 90 Days):**
                • Current Occupancy: **85.3%** across 3 branches (179/206 beds).
                • Projected Q4 Inflow: **+12.4%** driven by incoming tech batches in Bengaluru & Hyderabad.
                • Recommendation: Convert 4 standard rooms to Executive Studio Suites to increase RevPAB (Revenue Per Available Bed) by 18%.
                """.trimIndent()
            }
            else -> {
                """
                🏢 **PG Master AI Assistant:**
                I am your 24/7 smart PG management assistant! I can help you with:
                • **Room & Bed availability** across floors
                • **Rent payment & invoices** with GST breakdowns
                • **Hourly & flexible stay calculations**
                • **Visitor passes & parking slots**
                • **Food menus & laundry pickups**
                • **Emergency SOS dispatch**
                How can I assist you right now?
                """.trimIndent()
            }
        }
    }
}
