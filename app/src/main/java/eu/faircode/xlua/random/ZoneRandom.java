package eu.faircode.xlua.random;

public class ZoneRandom {
    public static final String[] TIMEZONE_IDS = {
            "Pacific/Midway",
            "America/Adak",
            "Pacific/Honolulu",
            "America/Anchorage",
            "America/Los_Angeles",
            "America/Denver",
            "America/Chicago",
            "America/New_York",
            "America/Caracas",
            "America/Santiago",
            "America/Buenos_Aires",
            "Atlantic/South_Georgia",
            "Atlantic/Azores",
            "Europe/London",
            "Europe/Berlin",
            "Europe/Moscow",
            "Asia/Dubai",
            "Asia/Karachi",
            "Asia/Kolkata",
            "Asia/Shanghai",
            "Asia/Tokyo",
            "Australia/Sydney",
            "Pacific/Auckland",
            "Pacific/Fiji",
            "Pacific/Tongatapu",
            "Africa/Cairo",
            "Europe/Paris",
            "America/Montevideo",
            "Asia/Jerusalem",
            "Africa/Johannesburg",
            "Europe/Istanbul",
            "Europe/Kiev",
            "America/Mexico_City",
            "Asia/Bangkok",
            "Asia/Singapore",
            "Asia/Seoul",
            "Australia/Perth",
            "America/Toronto",
            "America/Havana",
            "Atlantic/Reykjavik",
            "Europe/Athens",
            "Africa/Nairobi",
            "Europe/Stockholm",
            "Asia/Beirut",
            "Pacific/Guam",
            "America/Lima",
            "Asia/Tehran",
            "Europe/Zurich",
            "Asia/Yakutsk",
            "America/Halifax"
    };

    public static final String[] TIMEZONE_ISO = {
            "UM", // Pacific/Midway
            "US", // America/Adak
            "US", // Pacific/Honolulu
            "US", // America/Anchorage
            "US", // America/Los_Angeles
            "US", // America/Denver
            "US", // America/Chicago
            "US", // America/New_York
            "VE", // America/Caracas
            "CL", // America/Santiago
            "AR", // America/Buenos_Aires
            "GS", // Atlantic/South_Georgia
            "PT", // Atlantic/Azores
            "GB", // Europe/London
            "DE", // Europe/Berlin
            "RU", // Europe/Moscow
            "AE", // Asia/Dubai
            "PK", // Asia/Karachi
            "IN", // Asia/Kolkata
            "CN", // Asia/Shanghai
            "JP", // Asia/Tokyo
            "AU", // Australia/Sydney
            "NZ", // Pacific/Auckland
            "FJ", // Pacific/Fiji
            "TO", // Pacific/Tongatapu
            "EG", // Africa/Cairo
            "FR", // Europe/Paris
            "UY", // America/Montevideo
            "IL", // Asia/Jerusalem
            "ZA", // Africa/Johannesburg
            "TR", // Europe/Istanbul
            "UA", // Europe/Kiev
            "MX", // America/Mexico_City
            "TH", // Asia/Bangkok
            "SG", // Asia/Singapore
            "KR", // Asia/Seoul
            "AU", // Australia/Perth
            "CA", // America/Toronto
            "CU", // America/Havana
            "IS", // Atlantic/Reykjavik
            "GR", // Europe/Athens
            "KE", // Africa/Nairobi
            "SE", // Europe/Stockholm
            "LB", // Asia/Beirut
            "GU", // Pacific/Guam
            "PE", // America/Lima
            "IR", // Asia/Tehran
            "CH", // Europe/Zurich
            "RU", // Asia/Yakutsk
            "CA"  // America/Halifax
    };

    public static final String[] TIMEZONE_COUNTRIES = {
            "United States Minor Outlying Islands", // Pacific/Midway
            "United States", // America/Adak
            "United States", // Pacific/Honolulu
            "United States", // America/Anchorage
            "United States", // America/Los_Angeles
            "United States", // America/Denver
            "United States", // America/Chicago
            "United States", // America/New_York
            "Venezuela", // America/Caracas
            "Chile", // America/Santiago
            "Argentina", // America/Buenos_Aires
            "South Georgia and the South Sandwich Islands", // Atlantic/South_Georgia
            "Portugal", // Atlantic/Azores
            "United Kingdom", // Europe/London
            "Germany", // Europe/Berlin
            "Russia", // Europe/Moscow
            "United Arab Emirates", // Asia/Dubai
            "Pakistan", // Asia/Karachi
            "India", // Asia/Kolkata
            "China", // Asia/Shanghai
            "Japan", // Asia/Tokyo
            "Australia", // Australia/Sydney
            "New Zealand", // Pacific/Auckland
            "Fiji", // Pacific/Fiji
            "Tonga", // Pacific/Tongatapu
            "Egypt", // Africa/Cairo
            "France", // Europe/Paris
            "Uruguay", // America/Montevideo
            "Israel", // Asia/Jerusalem
            "South Africa", // Africa/Johannesburg
            "Turkey", // Europe/Istanbul
            "Ukraine", // Europe/Kiev
            "Mexico", // America/Mexico_City
            "Thailand", // Asia/Bangkok
            "Singapore", // Asia/Singapore
            "South Korea", // Asia/Seoul
            "Australia", // Australia/Perth
            "Canada", // America/Toronto
            "Cuba", // America/Havana
            "Iceland", // Atlantic/Reykjavik
            "Greece", // Europe/Athens
            "Kenya", // Africa/Nairobi
            "Sweden", // Europe/Stockholm
            "Lebanon", // Asia/Beirut
            "Guam", // Pacific/Guam
            "Peru", // America/Lima
            "Iran", // Asia/Tehran
            "Switzerland", // Europe/Zurich
            "Russia", // Asia/Yakutsk
            "Canada"  // America/Halifax
    };

    public static final String[] TIMEZONE_LANGUAGES = {
            "English", // United States Minor Outlying Islands
            "English", // America/Adak, United States
            "English", // Pacific/Honolulu, United States
            "English", // America/Anchorage, United States
            "English", // America/Los_Angeles, United States
            "English", // America/Denver, United States
            "English", // America/Chicago, United States
            "English", // America/New_York, United States
            "Spanish", // America/Caracas, Venezuela
            "Spanish", // America/Santiago, Chile
            "Spanish", // America/Buenos_Aires, Argentina
            "English", // Atlantic/South_Georgia, South Georgia and the South Sandwich Islands
            "Portuguese", // Atlantic/Azores, Portugal
            "English", // Europe/London, United Kingdom
            "German", // Europe/Berlin, Germany
            "Russian", // Europe/Moscow, Russia
            "Arabic", // Asia/Dubai, United Arab Emirates
            "Urdu", // Asia/Karachi, Pakistan
            "Hindi", // Asia/Kolkata, India
            "Mandarin", // Asia/Shanghai, China
            "Japanese", // Asia/Tokyo, Japan
            "English", // Australia/Sydney, Australia
            "English", // Pacific/Auckland, New Zealand
            "Fijian", // Pacific/Fiji, Fiji
            "Tongan", // Pacific/Tongatapu, Tonga
            "Arabic", // Africa/Cairo, Egypt
            "French", // Europe/Paris, France
            "Spanish", // America/Montevideo, Uruguay
            "Hebrew", // Asia/Jerusalem, Israel
            "English", // Africa/Johannesburg, South Africa
            "Turkish", // Europe/Istanbul, Turkey
            "Ukrainian", // Europe/Kiev, Ukraine
            "Spanish", // America/Mexico_City, Mexico
            "Thai", // Asia/Bangkok, Thailand
            "English", // Asia/Singapore, Singapore
            "Korean", // Asia/Seoul, South Korea
            "English", // Australia/Perth, Australia
            "English", // America/Toronto, Canada
            "Spanish", // America/Havana, Cuba
            "Icelandic", // Atlantic/Reykjavik, Iceland
            "Greek", // Europe/Athens, Greece
            "Swahili", // Africa/Nairobi, Kenya
            "Swedish", // Europe/Stockholm, Sweden
            "Arabic", // Asia/Beirut, Lebanon
            "English", // Pacific/Guam, Guam
            "Spanish", // America/Lima, Peru
            "Persian", // Asia/Tehran, Iran
            "German", // Europe/Zurich, Switzerland
            "Russian", // Asia/Yakutsk, Russia
            "English", // America/Halifax, Canada
    };

    public static final String[] LANGUAGE_TAGS = {
            "en-UM", // United States Minor Outlying Islands
            "en-US", // America/Adak, United States
            "en-US", // Pacific/Honolulu, United States
            "en-US", // America/Anchorage, United States
            "en-US", // America/Los_Angeles, United States
            "en-US", // America/Denver, United States
            "en-US", // America/Chicago, United States
            "en-US", // America/New_York, United States
            "es-VE", // America/Caracas, Venezuela
            "es-CL", // America/Santiago, Chile
            "es-AR", // America/Buenos_Aires, Argentina
            "en-GS", // Atlantic/South_Georgia, South Georgia and the South Sandwich Islands
            "pt-PT", // Atlantic/Azores, Portugal
            "en-GB", // Europe/London, United Kingdom
            "de-DE", // Europe/Berlin, Germany
            "ru-RU", // Europe/Moscow, Russia
            "ar-AE", // Asia/Dubai, United Arab Emirates
            "ur-PK", // Asia/Karachi, Pakistan
            "hi-IN", // Asia/Kolkata, India
            "zh-CN", // Asia/Shanghai, China
            "ja-JP", // Asia/Tokyo, Japan
            "en-AU", // Australia/Sydney, Australia
            "en-NZ", // Pacific/Auckland, New Zealand
            "fj-FJ", // Pacific/Fiji, Fiji
            "to-TO", // Pacific/Tongatapu, Tonga
            "ar-EG", // Africa/Cairo, Egypt
            "fr-FR", // Europe/Paris, France
            "es-UY", // America/Montevideo, Uruguay
            "he-IL", // Asia/Jerusalem, Israel
            "en-ZA", // Africa/Johannesburg, South Africa
            "tr-TR", // Europe/Istanbul, Turkey
            "uk-UA", // Europe/Kiev, Ukraine
            "es-MX", // America/Mexico_City, Mexico
            "th-TH", // Asia/Bangkok, Thailand
            "en-SG", // Asia/Singapore, Singapore
            "ko-KR", // Asia/Seoul, South Korea
            "en-AU", // Australia/Perth, Australia
            "en-CA", // America/Toronto, Canada
            "es-CU", // America/Havana, Cuba
            "is-IS", // Atlantic/Reykjavik, Iceland
            "el-GR", // Europe/Athens, Greece
            "sw-KE", // Africa/Nairobi, Kenya
            "sv-SE", // Europe/Stockholm, Sweden
            "ar-LB", // Asia/Beirut, Lebanon
            "en-GU", // Pacific/Guam, Guam
            "es-PE", // America/Lima, Peru
            "fa-IR", // Asia/Tehran, Iran
            "de-CH", // Europe/Zurich, Switzerland
            "ru-RU", // Asia/Yakutsk, Russia
            "en-CA"  // America/Halifax, Canada
    };

    public static final String[] TIMEZONE_OFFSETS = {
            "GMT-11", // Pacific/Midway
            "GMT-10", // America/Adak, United States
            "GMT-10", // Pacific/Honolulu, United States
            "GMT-9",  // America/Anchorage, United States
            "GMT-8",  // America/Los_Angeles, United States
            "GMT-7",  // America/Denver, United States
            "GMT-6",  // America/Chicago, United States
            "GMT-5",  // America/New_York, United States
            "GMT-4:30", // America/Caracas, Venezuela
            "GMT-4",  // America/Santiago, Chile
            "GMT-3",  // America/Buenos_Aires, Argentina
            "GMT-2",  // Atlantic/South_Georgia, South Georgia and the South Sandwich Islands
            "GMT-1",  // Atlantic/Azores, Portugal
            "GMT+0",  // Europe/London, United Kingdom
            "GMT+1",  // Europe/Berlin, Germany
            "GMT+3",  // Europe/Moscow, Russia
            "GMT+4",  // Asia/Dubai, United Arab Emirates
            "GMT+5",  // Asia/Karachi, Pakistan
            "GMT+5:30", // Asia/Kolkata, India
            "GMT+8",  // Asia/Shanghai, China
            "GMT+9",  // Asia/Tokyo, Japan
            "GMT+10", // Australia/Sydney, Australia
            "GMT+12", // Pacific/Auckland, New Zealand
            "GMT+12", // Pacific/Fiji, Fiji
            "GMT+13", // Pacific/Tongatapu, Tonga
            "GMT+2",  // Africa/Cairo, Egypt
            "GMT+1",  // Europe/Paris, France
            "GMT-3",  // America/Montevideo, Uruguay
            "GMT+2",  // Asia/Jerusalem, Israel
            "GMT+2",  // Africa/Johannesburg, South Africa
            "GMT+3",  // Europe/Istanbul, Turkey
            "GMT+2",  // Europe/Kiev, Ukraine
            "GMT-6",  // America/Mexico_City, Mexico
            "GMT+7",  // Asia/Bangkok, Thailand
            "GMT+8",  // Asia/Singapore, Singapore
            "GMT+9",  // Asia/Seoul, South Korea
            "GMT+8",  // Australia/Perth, Australia
            "GMT-5",  // America/Toronto, Canada
            "GMT-5",  // America/Havana, Cuba
            "GMT+0",  // Atlantic/Reykjavik, Iceland
            "GMT+2",  // Europe/Athens, Greece
            "GMT+3",  // Africa/Nairobi, Kenya
            "GMT+1",  // Europe/Stockholm, Sweden
            "GMT+2",  // Asia/Beirut, Lebanon
            "GMT+10", // Pacific/Guam, Guam
            "GMT-5",  // America/Lima, Peru
            "GMT+3:30", // Asia/Tehran, Iran
            "GMT+1",  // Europe/Zurich, Switzerland
            "GMT+9",  // Asia/Yakutsk, Russia
            "GMT-4"   // America/Halifax, Canada
    };

    public static final String[] TIMEZONE_DISPLAY_NAMES = {
            "Midway Islands Time", // Pacific/Midway
            "Hawaii-Aleutian Standard Time", // America/Adak, United States
            "Hawaii Standard Time", // Pacific/Honolulu, United States
            "Alaska Standard Time", // America/Anchorage, United States
            "Pacific Standard Time", // America/Los_Angeles, United States
            "Mountain Standard Time", // America/Denver, United States
            "Central Standard Time", // America/Chicago, United States
            "Eastern Standard Time", // America/New_York, United States
            "Venezuelan Standard Time", // America/Caracas, Venezuela
            "Chile Standard Time", // America/Santiago, Chile
            "Argentina Standard Time", // America/Buenos_Aires, Argentina
            "South Georgia Standard Time", // Atlantic/South_Georgia
            "Azores Standard Time", // Atlantic/Azores
            "Greenwich Mean Time", // Europe/London
            "Central European Time", // Europe/Berlin
            "Moscow Standard Time", // Europe/Moscow
            "Gulf Standard Time", // Asia/Dubai
            "Pakistan Standard Time", // Asia/Karachi
            "India Standard Time", // Asia/Kolkata
            "China Standard Time", // Asia/Shanghai
            "Japan Standard Time", // Asia/Tokyo
            "Australian Eastern Standard Time", // Australia/Sydney
            "New Zealand Standard Time", // Pacific/Auckland
            "Fiji Time", // Pacific/Fiji
            "Tonga Time", // Pacific/Tongatapu
            "Eastern European Time", // Africa/Cairo
            "Central European Time", // Europe/Paris
            "Uruguay Standard Time", // America/Montevideo
            "Israel Standard Time", // Asia/Jerusalem
            "South Africa Standard Time", // Africa/Johannesburg
            "Turkey Time", // Europe/Istanbul
            "Eastern European Time", // Europe/Kiev
            "Central Standard Time", // America/Mexico_City
            "Indochina Time", // Asia/Bangkok
            "Singapore Standard Time", // Asia/Singapore
            "Korea Standard Time", // Asia/Seoul
            "Australian Western Standard Time", // Australia/Perth
            "Eastern Standard Time", // America/Toronto
            "Cuba Standard Time", // America/Havana
            "Greenwich Mean Time", // Atlantic/Reykjavik
            "Eastern European Time", // Europe/Athens
            "East Africa Time", // Africa/Nairobi
            "Central European Time", // Europe/Stockholm
            "Eastern European Time", // Asia/Beirut
            "Chamorro Standard Time", // Pacific/Guam
            "Peru Standard Time", // America/Lima
            "Iran Standard Time", // Asia/Tehran
            "Central European Time", // Europe/Zurich
            "Yakutsk Time", // Asia/Yakutsk
            "Atlantic Standard Time"  // America/Halifax, Canada
    };

    public static final String[] LANGUAGE_ISO = {
            "en", // United States Minor Outlying Islands
            "en", // America/Adak, United States
            "en", // Pacific/Honolulu, United States
            "en", // America/Anchorage, United States
            "en", // America/Los_Angeles, United States
            "en", // America/Denver, United States
            "en", // America/Chicago, United States
            "en", // America/New_York, United States
            "es", // America/Caracas, Venezuela
            "es", // America/Santiago, Chile
            "es", // America/Buenos_Aires, Argentina
            "en", // Atlantic/South_Georgia
            "pt", // Atlantic/Azores
            "en", // Europe/London
            "de", // Europe/Berlin
            "ru", // Europe/Moscow
            "ar", // Asia/Dubai
            "ur", // Asia/Karachi
            "hi", // Asia/Kolkata
            "zh", // Asia/Shanghai
            "ja", // Asia/Tokyo
            "en", // Australia/Sydney
            "en", // Pacific/Auckland
            "fj", // Pacific/Fiji
            "to", // Pacific/Tongatapu
            "ar", // Africa/Cairo
            "fr", // Europe/Paris
            "es", // America/Montevideo
            "he", // Asia/Jerusalem
            "en", // Africa/Johannesburg
            "tr", // Europe/Istanbul
            "uk", // Europe/Kiev
            "es", // America/Mexico_City
            "th", // Asia/Bangkok
            "en", // Asia/Singapore
            "ko", // Asia/Seoul
            "en", // Australia/Perth
            "en", // America/Toronto
            "es", // America/Havana
            "is", // Atlantic/Reykjavik
            "el", // Europe/Athens
            "sw", // Africa/Nairobi
            "sv", // Europe/Stockholm
            "ar", // Asia/Beirut
            "en", // Pacific/Guam
            "es", // America/Lima
            "fa", // Asia/Tehran
            "de", // Europe/Zurich
            "ru", // Asia/Yakutsk
            "en"  // America/Halifax
    };
}
