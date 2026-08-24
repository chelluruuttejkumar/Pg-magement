package com.example.model

object Localization {

    private val translations = mapOf(
        "app_title" to mapOf(
            Language.ENGLISH to "PG Master Ecosystem",
            Language.HINDI to "पीजी मास्टर इकोसिस्टम",
            Language.TELUGU to "పీజీ మాస్టర్ ఎకోసిస్టమ్",
            Language.TAMIL to "பிஜி மாஸ்டர் சூழல்",
            Language.KANNADA to "ಪಿಜಿ ಮಾಸ್ಟರ್ ಪರಿಸರ",
            Language.MALAYALAM to "പിജി മാസ്റ്റർ ഇക്കോസിസ്റ്റം",
            Language.BENGALI to "পিজি মাস্টার ইকোসিস্টেম",
            Language.MARATHI to "पीजी मास्टर इकोसिस्टम"
        ),
        "disclaimer_title" to mapOf(
            Language.ENGLISH to "Mandatory PG Policies & Disclaimer",
            Language.HINDI to "अनिवार्य पीजी नियम एवं अस्वीकरण",
            Language.TELUGU to "తప్పనిసరి పీజీ నిబంధనలు & నిరాకరణ",
            Language.TAMIL to "கட்டாய பிஜி கொள்கைகள் & பொறுப்புத்துறப்பு",
            Language.KANNADA to "ಕಡ್ಡಾಯ ಪಿಜಿ ನೀತಿಗಳು ಮತ್ತು ಹಕ್ಕುತ್ಯಾಗ",
            Language.MALAYALAM to "നിർബന്ധിത പിജി നയങ്ങളും നിരാകരണവും",
            Language.BENGALI to "বাধ্যতামূলক পিজি নীতি ও অস্বীকৃতি",
            Language.MARATHI to "अनिवार्य पीजी धोरणे आणि अस्वीकरण"
        ),
        "disclaimer_desc" to mapOf(
            Language.ENGLISH to "You must read and acknowledge all 9 statutory terms before accessing your dashboard.",
            Language.HINDI to "डैशबोर्ड तक पहुंचने से पहले आपको सभी 9 वैधानिक शर्तों को स्वीकार करना होगा।",
            Language.TELUGU to "మీ డ్యాష్‌బోర్డ్‌ను యాక్సెస్ చేయడానికి ముందు మీరు మొత్తం 9 నిబంధనలను అంగీకరించాలి.",
            Language.TAMIL to "டாஷ்போர்டை அணுகுவதற்கு முன் 9 சட்டப்பூர்வ விதிமுறைகளையும் நீங்கள் ஏற்க வேண்டும்.",
            Language.KANNADA to "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್ ಪ್ರವೇಶಿಸುವ ಮೊದಲು ನೀವು ಎಲ್ಲಾ 9 ಶಾಸನಬದ್ಧ ನಿಯಮಗಳನ್ನು ಒಪ್ಪಿಕೊಳ್ಳಬೇಕು.",
            Language.MALAYALAM to "ഡാഷ്‌ബോർഡ് ആക്‌സസ് ചെയ്യുന്നതിന് മുമ്പ് നിങ്ങൾ 9 നിയമപരമായ നിബന്ധനകളും അംഗീകരിക്കണം.",
            Language.BENGALI to "ড্যাশবোর্ড অ্যাক্সেস করার আগে আপনাকে সমস্ত ৯টি সংবিধিবদ্ধ শর্ত স্বীকার করতে হবে।",
            Language.MARATHI to "डॅशबोर्ड ॲक्सेस करण्यापूर्वी तुम्ही सर्व 9 वैधानिक अटी मान्य केल्या पाहिजेत."
        ),
        "dashboard" to mapOf(
            Language.ENGLISH to "Dashboard",
            Language.HINDI to "डैशबोर्ड",
            Language.TELUGU to "డ్యాష్‌బోర్డ్",
            Language.TAMIL to "டாஷ்போர்டு",
            Language.KANNADA to "ಡ್ಯಾಶ್‌ಬೋರ್ಡ್",
            Language.MALAYALAM to "ഡാഷ്‌ബോർഡ്",
            Language.BENGALI to "ড্যাশবোর্ড",
            Language.MARATHI to "डॅशबोर्ड"
        ),
        "rooms" to mapOf(
            Language.ENGLISH to "3D Rooms & Beds",
            Language.HINDI to "3D कमरे और बिस्तर",
            Language.TELUGU to "3D గదులు & పడకలు",
            Language.TAMIL to "3D அறைகள் & படுக்கைகள்",
            Language.KANNADA to "3D ಕೊಠಡಿಗಳು & ಹಾಸಿಗೆಗಳು",
            Language.MALAYALAM to "3D മുറികളും കിടക്കകളും",
            Language.BENGALI to "3D রুম ও বিছানা",
            Language.MARATHI to "3D खोल्या आणि खाटा"
        ),
        "rent_bills" to mapOf(
            Language.ENGLISH to "Rent & Invoices",
            Language.HINDI to "किराया और बिल",
            Language.TELUGU to "అద్దె & ఇన్‌వాయిస్‌లు",
            Language.TAMIL to "வாடகை & விலைப்பட்டியல்கள்",
            Language.KANNADA to "ಬಾಡಿಗೆ & ಬಿಲ್‌ಗಳು",
            Language.MALAYALAM to "വാടകയും ഇൻവോയ്സുകളും",
            Language.BENGALI to "ভাড়া ও ইনভয়েস",
            Language.MARATHI to "भाडे आणि पावत्या"
        ),
        "complaints" to mapOf(
            Language.ENGLISH to "Complaints",
            Language.HINDI to "शिकायतें",
            Language.TELUGU to "ఫిర్యాదులు",
            Language.TAMIL to "புகார்கள்",
            Language.KANNADA to "ದೂರುಗಳು",
            Language.MALAYALAM to "പരാതികൾ",
            Language.BENGALI to "অভিযোগ",
            Language.MARATHI to "तक्रारी"
        ),
        "parking" to mapOf(
            Language.ENGLISH to "3D Parking",
            Language.HINDI to "3D पार्किंग",
            Language.TELUGU to "3D పార్కింగ్",
            Language.TAMIL to "3D பார்க்கிங்",
            Language.KANNADA to "3D ಪಾರ್ಕಿಂಗ್",
            Language.MALAYALAM to "3D പാർക്കിംഗ്",
            Language.BENGALI to "3D পার্কিং",
            Language.MARATHI to "3D पार्किंग"
        ),
        "visitors" to mapOf(
            Language.ENGLISH to "Visitor Pass",
            Language.HINDI to "आगंतुक पास",
            Language.TELUGU to "సందర్శకుల పాస్",
            Language.TAMIL to "பார்வையாளர் பாஸ்",
            Language.KANNADA to "ಸಂದರ್ಶಕರ ಪಾಸ್",
            Language.MALAYALAM to "സന്ദർശക പാസ്",
            Language.BENGALI to "দর্শনার্থী পাস",
            Language.MARATHI to "अभ्यागत पास"
        ),
        "food_laundry" to mapOf(
            Language.ENGLISH to "Food & Laundry",
            Language.HINDI to "भोजन और लॉन्ड्री",
            Language.TELUGU to "ఆహారం & లాండ్రీ",
            Language.TAMIL to "உணவு & சலவை",
            Language.KANNADA to "ಆಹಾರ & ಲಾಂಡ್ರಿ",
            Language.MALAYALAM to "ഭക്ഷണവും അലക്കും",
            Language.BENGALI to "খাবার ও লন্ড্রি",
            Language.MARATHI to "अन्न आणि लाँड्री"
        ),
        "attendance_checkin" to mapOf(
            Language.ENGLISH to "Smart Check-In",
            Language.HINDI to "स्मार्ट चेक-इन",
            Language.TELUGU to "స్మార్ట్ చెక్-ఇన్",
            Language.TAMIL to "ஸ்மார்ட் செக்-இன்",
            Language.KANNADA to "ಸ್ಮಾರ್ಟ್ ಚೆಕ್-ಇನ್",
            Language.MALAYALAM to "സ്മാർട്ട് ചെക്ക്-ഇൻ",
            Language.BENGALI to "স্মার্ট চেক-ইন",
            Language.MARATHI to "स्मार्ट चेक-इन"
        ),
        "sos_emergency" to mapOf(
            Language.ENGLISH to "Emergency SOS",
            Language.HINDI to "आपातकालीन एसओएस",
            Language.TELUGU to "అత్యవసర SOS",
            Language.TAMIL to "அவசர SOS",
            Language.KANNADA to "ತುರ್ತು SOS",
            Language.MALAYALAM to "അടിയന്തര SOS",
            Language.BENGALI to "জরুরী এসওএস",
            Language.MARATHI to "आपत्कालीन SOS"
        ),
        "ai_assistant" to mapOf(
            Language.ENGLISH to "PG AI Assistant",
            Language.HINDI to "पीजी एआई सहायक",
            Language.TELUGU to "పీజీ AI అసిస్టెంట్",
            Language.TAMIL to "பிஜி AI உதவியாளர்",
            Language.KANNADA to "ಪಿಜಿ AI ಸಹಾಯಕ",
            Language.MALAYALAM to "പിജി AI അസിസ്റ്റന്റ്",
            Language.BENGALI to "পিজি এআই সহকারী",
            Language.MARATHI to "पीजी AI सहाय्यक"
        ),
        "hourly_calculator" to mapOf(
            Language.ENGLISH to "Hourly/Flexible Stay",
            Language.HINDI to "प्रति घंटा/लचीला ठहराव",
            Language.TELUGU to "గంటల వారీ/సౌకర్యవంతమైన బస",
            Language.TAMIL to "மணிநேர/நெகிழ்வான தங்குமிடம்",
            Language.KANNADA to "ಗಂಟೆಯ/ಹೊಂದಿಕೊಳ್ಳುವ ವಾಸ್ತವ್ಯ",
            Language.MALAYALAM to "മണിക്കൂർ അടിസ്ഥാനത്തിലുള്ള താമസം",
            Language.BENGALI to "প্রতি ঘণ্টার/নমনীয় অবস্থান",
            Language.MARATHI to "तासनिहाय/लवचिक मुक्काम"
        ),
        "accept_continue" to mapOf(
            Language.ENGLISH to "Accept All & Access Dashboard",
            Language.HINDI to "सभी स्वीकार करें और आगे बढ़ें",
            Language.TELUGU to "అన్నీ అంగీకరించి డాష్‌బోర్డ్ తెరవండి",
            Language.TAMIL to "அனைத்தையும் ஏற்று டாஷ்போர்டை அணுகவும்",
            Language.KANNADA to "ಎಲ್ಲವನ್ನೂ ಒಪ್ಪಿಕೊಳ್ಳಿ & ಮುಂದುವರಿಯಿರಿ",
            Language.MALAYALAM to "എല്ലാം അംഗീകരിച്ച് തുടരുക",
            Language.BENGALI to "সব গ্রহণ করুন এবং চালিয়ে যান",
            Language.MARATHI to "सर्व स्वीकारा आणि पुढे जा"
        )
    )

    fun get(key: String, language: Language): String {
        return translations[key]?.get(language) ?: translations[key]?.get(Language.ENGLISH) ?: key
    }
}
