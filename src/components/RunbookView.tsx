import { Terminal, Code, CheckCircle, Smartphone, FolderGit2, AlertTriangle, ArrowLeft } from 'lucide-react';

export function RunbookView() {
  return (
    <div className="flex flex-col h-full bg-black overflow-y-auto">
      <div className="p-6 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10 flex items-center gap-3">
        <Terminal className="text-accent" size={28} />
        <div>
          <h2 className="text-2xl font-bold">دليل المطور (Runbook)</h2>
          <p className="text-text-secondary text-sm mt-1">المرحلة الأولى: إعداد البيئة والنواة الأساسية (Signal-Android)</p>
        </div>
      </div>

      <div className="p-6 max-w-4xl space-y-8">
        
        {/* Step 1 */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">1</div>
            <h3 className="font-semibold text-white">إعداد الأدوات (Setup)</h3>
          </div>
          <div className="p-5 space-y-4">
            <ul className="space-y-3 text-sm text-text-secondary">
              <li className="flex items-start"><CheckCircle size={16} className="text-accent me-2 mt-0.5 shrink-0" /> <span className="text-white font-medium me-1">Git:</span> نظام إدارة النسخ.</li>
              <li className="flex items-start"><CheckCircle size={16} className="text-accent me-2 mt-0.5 shrink-0" /> <span className="text-white font-medium me-1">Android Studio:</span> بيئة التطوير (إصدار Hedgehog أو أحدث).</li>
              <li className="flex items-start"><CheckCircle size={16} className="text-accent me-2 mt-0.5 shrink-0" /> <span className="text-white font-medium me-1">JDK 17+:</span> حزمة تطوير جافا.</li>
            </ul>
            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start" dir="ltr">
              <div className="text-gray-500 mb-1"># إنشاء مجلد العمل</div>
              <div>mkdir SecureLine_Project</div>
              <div>cd SecureLine_Project</div>
            </div>
          </div>
        </section>

        {/* Step 2 */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">2</div>
            <h3 className="font-semibold text-white">استنساخ الكود (Cloning)</h3>
          </div>
          <div className="p-5 space-y-4">
            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start" dir="ltr">
              <div className="text-gray-500 mb-1"># استنساخ المستودع الضخم</div>
              <div>git clone https://github.com/signalapp/Signal-Android.git</div>
              <div>cd Signal-Android</div>
            </div>
            <div className="flex items-start bg-blue-900/20 border border-blue-900/50 rounded-lg p-4 text-sm text-blue-200">
              <FolderGit2 size={18} className="me-3 shrink-0 mt-0.5 text-blue-400" />
              <p>افتح هذا المجلد في Android Studio. ستقوم عملية Gradle Sync الأولية بتحميل جميع المكتبات، وقد تستغرق العملية من 10 إلى 30 دقيقة.</p>
            </div>
          </div>
        </section>

        {/* Step 3 */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">3</div>
            <h3 className="font-semibold text-white">إعداد خدمة الإشعارات (Firebase)</h3>
          </div>
          <div className="p-5 space-y-4 text-sm text-text-secondary">
            <p>يعتمد Signal على Firebase لإيقاظ التطبيق عند استلام رسائل جديدة.</p>
            <ol className="list-decimal list-inside space-y-2 ms-2">
              <li>أنشئ مشروعاً باسم <span className="text-white">SecureLine</span> في لوحة تحكم Firebase.</li>
              <li>سجل تطبيق Android باسم الحزمة: <code className="bg-black px-1.5 py-0.5 rounded text-accent border border-gray-800" dir="ltr">org.thoughtcrime.securesms</code></li>
              <li>قم بتحميل ملف <code className="bg-black px-1.5 py-0.5 rounded text-accent border border-gray-800" dir="ltr">google-services.json</code>.</li>
              <li>ضعه داخل المسار <code className="bg-black px-1.5 py-0.5 rounded text-accent border border-gray-800" dir="ltr">SecureLine_Project/Signal-Android/app/</code></li>
              <li>اضغط على "Sync Project with Gradle Files" في Android Studio.</li>
            </ol>
          </div>
        </section>

        {/* Step 4 & 5 */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">4</div>
            <h3 className="font-semibold text-white">البناء، التشغيل واختبار الدخان (Smoke Test)</h3>
          </div>
          <div className="p-5 space-y-4">
            <div className="flex items-center gap-4">
              <Smartphone size={32} className="text-gray-500" />
              <div className="text-sm text-text-secondary">
                <p>فعّل <span className="text-white">تتبع أخطاء USB (USB Debugging)</span> على هاتفك.</p>
                <p>اضغط <span className="text-white font-medium">Run 'app'</span> في Android Studio.</p>
              </div>
            </div>
            
            <div className="mt-4 border-t border-gray-800 pt-4">
              <h4 className="text-sm font-semibold text-white mb-2 flex items-center"><CheckCircle size={14} className="text-accent me-2" /> قائمة التحقق (Checklist)</h4>
              <ul className="space-y-2 text-sm text-text-secondary ms-6 list-disc">
                <li>يفتح التطبيق بهوية Signal الزرقاء الافتراضية.</li>
                <li>تسجيل الدخول برقم الهاتف (مؤقتاً في هذه المرحلة).</li>
                <li>إرسال رسالة "ملاحظة للنفس" (Note to Self) للتأكد من عمل قاعدة البيانات المحلية وخطافات التشفير.</li>
              </ul>
            </div>
          </div>
        </section>

        {/* Step 3: Obfuscation Layer */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">5</div>
            <h3 className="font-semibold text-white">الوحدة 3: طبقة التعمية (Obfuscation Layer)</h3>
          </div>
          <div className="p-5 space-y-4 text-sm text-text-secondary">
            <p>تضيف هذه الطبقة حماية ضد التحليل المروري (Traffic Analysis) عن طريق عكس البايتات وإضافة بايتات وهمية.</p>
            
            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2"># File: app/src/main/java/com/secureline/secureline/crypto/ObfuscationLayer.java</div>
{`public class ObfuscationLayer {
    private static final int DUMMY_PREFIX_SIZE = 8;
    private static final int DUMMY_SUFFIX_SIZE = 12;

    public static byte[] obfuscate(byte[] originalEncryptedData) {
        SecureRandom random = new SecureRandom();
        byte[] reversed = reverseArray(originalEncryptedData);
        byte[] prefix = new byte[DUMMY_PREFIX_SIZE];
        byte[] suffix = new byte[DUMMY_SUFFIX_SIZE];
        random.nextBytes(prefix);
        random.nextBytes(suffix);
        // ... System.arraycopy to merge (Prefix + Reversed + Suffix)
        return result;
    }

    public static byte[] deobfuscate(byte[] obfuscatedData) {
        int dataStart = DUMMY_PREFIX_SIZE;
        int dataEnd = obfuscatedData.length - DUMMY_SUFFIX_SIZE;
        byte[] reversed = Arrays.copyOfRange(obfuscatedData, dataStart, dataEnd);
        return reverseArray(reversed);
    }
}`}
            </div>

            <h4 className="font-medium text-white mt-4 mb-2 flex items-center"><Code size={14} className="text-accent me-2" /> ربط الطبقة بخطافات الإرسال/الاستقبال</h4>
            <div className="bg-black/50 border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start" dir="ltr">
              <div className="text-gray-500 mb-1">// In SignalServiceMessageSender.java (Before sending)</div>
              <div>content = ObfuscationLayer.obfuscate(content);</div>
              <div className="text-gray-500 mt-2 mb-1">// In IncomingMessageProcessor.java (After receiving)</div>
              <div>content = ObfuscationLayer.deobfuscate(content);</div>
            </div>
          </div>
        </section>

        {/* Step 4: Self-Hosted Server */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">6</div>
            <h3 className="font-semibold text-white">الوحدة 4: خادم الاتصالات المستقل (Self-Hosted Server)</h3>
          </div>
          <div className="p-5 space-y-4 text-sm text-text-secondary">
            <p>تشغيل خادم مركزي خاص باستخدام Docker لضمان عدم مرور البيانات عبر خوادم Signal الأصلية.</p>
            
            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2"># 1. Clone & Setup Docker Compose</div>
              <div>git clone https://github.com/signalapp/Signal-Server.git</div>
              <div>cd Signal-Server</div>
              <div>nano docker-compose.yml</div>
            </div>

            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-blue-400 text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2"># docker-compose.yml (Core Services)</div>
{`version: '3.8'
services:
  postgres:
    image: postgres:latest
    environment:
      POSTGRES_USER: secureline
      POSTGRES_PASSWORD: SecureLine2024!
      POSTGRES_DB: secureline_db
  redis:
    image: redis:latest
  signal-server:
    build: .
    ports: ["8080:8080"]
    depends_on: [postgres, redis]`}
            </div>

            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-1"># 2. Start Services</div>
              <div>docker-compose up -d</div>
              <div className="text-gray-500 mt-2 mb-1"># 3. Check Health</div>
              <div>curl http://localhost:8080/health</div>
            </div>

            <h4 className="font-medium text-white mt-4 mb-2 flex items-center"><Code size={14} className="text-accent me-2" /> ربط تطبيق الأندرويد بالخادم المحلي</h4>
            <div className="bg-black/50 border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start" dir="ltr">
              <div className="text-gray-500 mb-1">// In Android Studio: Search for textsecure-service URL</div>
              <div className="line-through text-gray-600">https://textsecure-service.whispersystems.org</div>
              <div className="text-emerald-400">http://[YOUR_LOCAL_IP]:8080</div>
            </div>
          </div>
        </section>

        {/* Step 5: WebRTC Tuning */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">7</div>
            <h3 className="font-semibold text-white">الوحدة 5: ضبط محرك المكالمات (WebRTC & Opus)</h3>
          </div>
          <div className="p-5 space-y-4 text-sm text-text-secondary">
            <p>تحسين جودة الصوت للعمل بكفاءة على الشبكات الضعيفة مع تقليل استهلاك البيانات.</p>
            
            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2"># File: app/src/main/java/com/secureline/secureline/webrtc/CallQualityManager.java</div>
{`public class CallQualityManager {
    public enum QualityMode { HIGH_QUALITY, DATA_SAVER, EXTREME_SAVER }
    private QualityMode currentMode = QualityMode.DATA_SAVER;

    public String getOpusBitrate() {
        switch (currentMode) {
            case HIGH_QUALITY: return "32";
            case DATA_SAVER: return "16";
            case EXTREME_SAVER: return "8";
            default: return "16";
        }
    }
    public boolean useForwardErrorCorrection() { return true; }
    public boolean useEchoCancellation() { return true; }
}`}
            </div>

            <h4 className="font-medium text-white mt-4 mb-2 flex items-center"><Code size={14} className="text-accent me-2" /> إعدادات P2P و MediaConstraints</h4>
            <div className="bg-black/50 border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start" dir="ltr">
              <div className="text-gray-500 mb-1">// In PeerConnectionWrapper.java</div>
              <div>audioConstraints.optional.add(new MediaConstraints.KeyValuePair("opusMaxPbr", qualityManager.getOpusBitrate()));</div>
              <div>audioConstraints.optional.add(new MediaConstraints.KeyValuePair("opusFec", "true"));</div>
              <div>audioConstraints.optional.add(new MediaConstraints.KeyValuePair("googEchoCancellation", "true"));</div>
              <div>audioConstraints.optional.add(new MediaConstraints.KeyValuePair("googNoiseSuppression", "true"));</div>
              <div className="text-gray-500 mt-2 mb-1">// Ensure Public STUN Servers are configured</div>
              <div>iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());</div>
            </div>
          </div>
        </section>

        {/* Step 6: Tor Integration */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">8</div>
            <h3 className="font-semibold text-white">الوحدة 6: دمج شبكة Tor (إخفاء الهوية بالكامل)</h3>
          </div>
          <div className="p-5 space-y-4 text-sm text-text-secondary">
            <p>تمرير كافة الاتصالات عبر شبكة Tor لإخفاء عنوان IP الحقيقي ومنع تتبع مزودي الخدمة (ISP).</p>
            
            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-blue-400 text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2">// 1. build.gradle (Dependencies)</div>
              <div>implementation 'info.guardianproject:tor-android:0.4.7.8'</div>
              <div>implementation 'org.torproject:tor-android:0.4.7.8'</div>
            </div>

            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2"># File: app/src/main/java/com/secureline/secureline/network/TorManager.java</div>
{`public class TorManager {
    private TorService torService;
    private boolean isRunning = false;

    public void start(Context context) {
        torService = new TorService(context);
        torService.start();
        isRunning = true;
    }
    
    public String getProxyHost() { return "127.0.0.1"; }
    public int getProxyPort() { return 9050; }
}`}
            </div>

            <h4 className="font-medium text-white mt-4 mb-2 flex items-center"><Code size={14} className="text-accent me-2" /> توجيه عميل الشبكة (OkHttp) عبر Tor</h4>
            <div className="bg-black/50 border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start" dir="ltr">
{`TorManager torManager = new TorManager();
OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

if (torManager.isTorRunning()) {
    java.net.Proxy proxy = new java.net.Proxy(
        java.net.Proxy.Type.SOCKS,
        new java.net.InetSocketAddress(torManager.getProxyHost(), torManager.getProxyPort())
    );
    clientBuilder.proxy(proxy);
}`}
            </div>
          </div>
        </section>

        {/* Step 7: Database Encryption */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">9</div>
            <h3 className="font-semibold text-white">الوحدة 7: تشفير قاعدة البيانات (SQLCipher)</h3>
          </div>
          <div className="p-5 space-y-4 text-sm text-text-secondary">
            <p>تشفير كافة الرسائل والبيانات المخزنة محلياً باستخدام AES-256 لمنع استخراجها حتى مع صلاحيات الروت.</p>
            
            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-blue-400 text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2">// 1. build.gradle (Dependencies)</div>
              <div>implementation 'net.zetetic:android-database-sqlcipher:4.5.4'</div>
            </div>

            <div className="bg-black border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start overflow-x-auto" dir="ltr">
              <div className="text-gray-500 mb-2"># File: app/src/main/java/com/secureline/secureline/security/KeyManager.java</div>
{`public class KeyManager {
    private static final String KEY_ALIAS = "secureline_db_master_key";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";

    public static SecretKey getOrCreateDatabaseKey() {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        if (keyStore.containsAlias(KEY_ALIAS)) {
            return ((KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null)).getSecretKey();
        }
        
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
            KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
         .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
         .setKeySize(256);
         
        keyGenerator.init(builder.build());
        return keyGenerator.generateKey();
    }
}`}
            </div>

            <h4 className="font-medium text-white mt-4 mb-2 flex items-center"><Code size={14} className="text-accent me-2" /> استبدال SQLite بـ SQLCipher</h4>
            <div className="bg-black/50 border border-gray-800 rounded-lg p-3 font-mono text-xs text-accent text-start" dir="ltr">
              <div className="text-gray-500 mb-1">// In DatabaseHelper.java (or SignalDatabase)</div>
              <div>static {'{'} System.loadLibrary("sqlcipher"); {'}'}</div>
              <div className="text-gray-500 mt-2 mb-1">// Open database with key</div>
              <div>String dbKey = KeyManager.getDatabaseKeyAsString();</div>
              <div>SQLiteDatabase db = this.getWritableDatabase(dbKey);</div>
            </div>
          </div>
        </section>

        {/* Step 8: QA & Testing */}
        <section className="bg-primary border border-gray-800 rounded-xl overflow-hidden">
          <div className="bg-gray-900/50 p-4 border-b border-gray-800 flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-accent/10 flex items-center justify-center text-accent font-bold">10</div>
            <h3 className="font-semibold text-white">الوحدة 8: ضمان الجودة والاختبار النهائي (QA & Testing)</h3>
          </div>
          <div className="p-5 space-y-4 text-sm text-text-secondary">
            <p>إجراء 4 اختبارات حاسمة للتأكد من جاهزية التطبيق للاستخدام الميداني والإطلاق.</p>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-2">
              <div className="bg-black/50 border border-gray-800 rounded-lg p-3">
                <h5 className="font-medium text-white text-sm mb-1 text-accent flex items-center"><ShieldCheck size={14} className="me-2" /> اختبار الاختراق الذاتي</h5>
                <p className="text-xs">استخدام Wireshark لاعتراض الحزم. يجب أن تظهر البيانات عشوائية تماماً (بفضل طبقة التعمية وتشفير P2P).</p>
              </div>
              <div className="bg-black/50 border border-gray-800 rounded-lg p-3">
                <h5 className="font-medium text-white text-sm mb-1 text-accent flex items-center"><Monitor size={14} className="me-2" /> اختبار الأداء</h5>
                <p className="text-xs">استهلاك RAM أقل من 100MB، وسرعة تشغيل أقل من 3 ثوانٍ على الأجهزة الضعيفة.</p>
              </div>
              <div className="bg-black/50 border border-gray-800 rounded-lg p-3">
                <h5 className="font-medium text-white text-sm mb-1 text-accent flex items-center"><Globe size={14} className="me-2" /> اختبار الشبكات</h5>
                <p className="text-xs">التبديل المفاجئ بين Wi-Fi وبيانات الجوال 3G، واختبار وصول الرسائل عبر وضع Tor.</p>
              </div>
              <div className="bg-black/50 border border-gray-800 rounded-lg p-3">
                <h5 className="font-medium text-white text-sm mb-1 text-accent flex items-center"><CheckCircle size={14} className="me-2" /> ملف الإطلاق (Release APK)</h5>
                <p className="text-xs">إنشاء Signed APK مع تفعيل ProGuard/R8. الحجم النهائي المتوقع: 20-30 ميجابايت.</p>
              </div>
            </div>
          </div>
        </section>

        {/* Troubleshooting */}
        <section className="bg-red-900/10 border border-red-900/30 rounded-xl p-5">
          <h4 className="text-sm font-semibold text-red-400 mb-3 flex items-center"><AlertTriangle size={16} className="me-2" /> أشهر 3 أخطاء وحلولها</h4>
          <div className="space-y-4">
            <div className="bg-black/50 border border-red-900/20 p-3 rounded-lg">
              <h5 className="font-medium text-white text-sm mb-1">1. فشل Gradle Sync (مشكلة شبكة)</h5>
              <p className="text-xs text-text-secondary">إذا لم يتمكن Gradle من جلب الملفات، فقد تكون مزودات الخدمة لديك تحجب خدمات جوجل. الحل: استخدم VPN على جهاز الكمبيوتر أثناء المزامنة.</p>
            </div>
            <div className="bg-black/50 border border-red-900/20 p-3 rounded-lg">
              <h5 className="font-medium text-white text-sm mb-1">2. خطأ "ملف google-services.json غير موجود"</h5>
              <p className="text-xs text-text-secondary">تأكد تماماً أن الملف موضوع داخل مجلد <code className="text-accent bg-gray-900 px-1 rounded" dir="ltr">app/</code> الفرعي، وليس في المجلد الرئيسي للمشروع.</p>
            </div>
            <div className="bg-black/50 border border-red-900/20 p-3 rounded-lg">
              <h5 className="font-medium text-white text-sm mb-1">3. الهاتف لا يظهر في Android Studio</h5>
              <p className="text-xs text-text-secondary">تأكد من تفعيل "USB Debugging". إذا استمرت المشكلة، فقد يكون الكابل مخصصاً للشحن فقط. جرب استخدام كابل نقل بيانات.</p>
            </div>
          </div>
        </section>

        {/* Architecture Insights */}
        <section className="bg-gray-900/50 border border-gray-800 rounded-xl p-5">
          <h4 className="text-sm font-semibold text-white mb-3 flex items-center"><Code size={16} className="text-gray-400 me-2" /> أهم مجلدات المشروع</h4>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3 text-xs">
            <div className="bg-black border border-gray-800 p-3 rounded-lg"><span className="text-accent" dir="ltr">/src/main/java/.../</span><br/><span className="text-gray-500">المنطق الأساسي (Java/Kotlin)</span></div>
            <div className="bg-black border border-gray-800 p-3 rounded-lg"><span className="text-accent" dir="ltr">/src/main/res/layout/</span><br/><span className="text-gray-500">تصاميم الواجهات (XML)</span></div>
            <div className="bg-black border border-gray-800 p-3 rounded-lg"><span className="text-accent" dir="ltr">/src/main/res/values/</span><br/><span className="text-gray-500">الألوان، النصوص، الثيمات</span></div>
            <div className="bg-black border border-gray-800 p-3 rounded-lg"><span className="text-accent" dir="ltr">/libsignal/</span><br/><span className="text-gray-500">نواة التشفير</span></div>
          </div>
        </section>

        {/* Next Steps */}
        <section className="bg-emerald-900/20 border border-emerald-900/50 rounded-xl p-5 flex items-center justify-between">
          <div>
            <h4 className="text-emerald-400 font-semibold flex items-center"><CheckCheck size={18} className="me-2" /> اكتمل المشروع الأساسي!</h4>
            <p className="text-sm text-emerald-200/70 mt-1">المرحلة القادمة: إزالة رقم الهاتف واستبداله بنظام الهوية الرقمية المجهولة (DID).</p>
          </div>
        </section>

      </div>
    </div>
  );
}
