# SecureLine ProGuard Rules

# Keep Signal Protocol classes
-keep class org.whispersystems.** { *; }
-dontwarn org.whispersystems.**

# Keep SQLCipher
-keep class net.zetetic.database.** { *; }
-dontwarn net.zetetic.**

# Keep WebRTC
-keep class org.webrtc.** { *; }
-dontwarn org.webrtc.**

# Keep Tor
-keep class org.torproject.** { *; }
-dontwarn org.torproject.**

# Keep BouncyCastle
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# Keep Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep our crypto classes
-keep class com.secureline.secureline.crypto.** { *; }
-keep class com.secureline.secureline.security.** { *; }

# Keep OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# Keep ZXing
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }
