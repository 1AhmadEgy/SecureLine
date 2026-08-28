package com.secureline.secureline.network;

public class NetworkConfig {
    // ⚠️ إعدادات الاتصال بالخادم (Server)

    // إذا كنت ستجرب التطبيق على "محاكي أندرويد" (Emulator) على نفس جهازك، اترك هذا الـ IP كما هو:
    // الرقم 10.0.2.2 هو الـ Localhost الافتراضي للمحاكي.
    // أما إذا كنت ستجرب على هاتف حقيقي، فقم بتغييره إلى عنوان IP الخاص بحاسوبك (مثال: 192.168.1.5)
    public static final String SERVER_IP = "10.0.2.2"; 
    
    // المنفذ الذي يعمل عليه الخادم الخاص بنا (كما تم تعريفه في ServerMain)
    public static final int SERVER_PORT = 8080;
    
    // مهلة الاتصال بالمللي ثانية (10 ثوانٍ)
    public static final int CONNECTION_TIMEOUT = 10000;
}
