import { Server, Shield, Cpu, Database, Network } from 'lucide-react';

export function ArchitectureView() {
  return (
    <div className="flex flex-col h-full bg-black overflow-y-auto">
      <div className="p-6 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10 flex items-center gap-3">
        <Server className="text-accent" size={28} />
        <div>
          <h2 className="text-2xl font-bold">خريطة البنية المعمارية</h2>
          <p className="text-text-secondary text-sm mt-1">خطة التنفيذ المعمارية لتطبيق SecureLine بناءً على المواصفات التقنية.</p>
        </div>
      </div>

      <div className="p-6 max-w-4xl space-y-8">
        
        {/* Architecture Diagram */}
        <section className="space-y-4">
          <h3 className="text-lg font-semibold text-accent border-b border-gray-800 pb-2">1. الهيكلية العامة (Architecture)</h3>
          <div className="bg-primary border border-gray-800 rounded-xl p-6 overflow-x-auto font-mono text-xs text-text-secondary whitespace-pre" dir="ltr">
{`graph TD
    A[User Device 1] <-->|WebRTC - Encrypted P2P| B[User Device 2]
    A <-->|WebSocket TLS 1.3| C[Signal Server]
    B <-->|WebSocket TLS 1.3| C
    C <-->|Cannot Decrypt| D[Server DB]
    
    subgraph "On-Device Encryption Layer"
        E[libsignal - Double Ratchet]
        F[AES-256-GCM / ChaCha20]
        G[SQLCipher (Message Storage)]
    end`}
          </div>
        </section>

        {/* Core Dependencies */}
        <section className="space-y-4">
          <h3 className="text-lg font-semibold text-accent border-b border-gray-800 pb-2">2. المشاريع مفتوحة المصدر (Dependencies)</h3>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2 flex items-center gap-2"><Shield size={16} className="text-accent" /> بروتوكول التشفير</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">libsignal:</span> التشفير الأساسي من طرف إلى طرف</li>
                <li><span className="text-white">Signal-Android/iOS:</span> واجهة المستخدم والمنطق</li>
              </ul>
            </div>
            
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2 flex items-center gap-2"><Cpu size={16} className="text-accent" /> محرك الصوتيات (A/V Engine)</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">libwebrtc:</span> محرك الصوت والفيديو</li>
                <li><span className="text-white">Opus:</span> ترميز جودة عالية واستهلاك منخفض</li>
                <li><span className="text-white">AEC3:</span> إلغاء الصدى</li>
              </ul>
            </div>
            
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2 flex items-center gap-2"><Database size={16} className="text-accent" /> التخزين المحلي والخصوصية</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">SQLCipher:</span> تشفير قاعدة البيانات 256-bit AES</li>
                <li><span className="text-white">Android Keystore:</span> تخزين المفاتيح (TEE)</li>
                <li><span className="text-white">Tor-Android:</span> التوجيه عبر الشبكة المخفية</li>
              </ul>
            </div>
            
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2 flex items-center gap-2"><Network size={16} className="text-accent" /> البنية التحتية للخوادم</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">Signal-Server:</span> توجيه الرسائل والإشارات</li>
                <li><span className="text-white">Matrix/Dendrite:</span> بديل لامركزي (اختياري)</li>
              </ul>
            </div>
          </div>
        </section>

        {/* Implementation Modules */}
        <section className="space-y-4">
          <h3 className="text-lg font-semibold text-accent border-b border-gray-800 pb-2">3. وحدات التنفيذ (Modules)</h3>
          <div className="space-y-3">
            {[
              { mod: "الوحدة 1", desc: "إعداد البيئة (Android Studio, Xcode, Docker)" },
              { mod: "الوحدة 2", desc: "تغيير الهوية (واجهة AMOLED سوداء، أيقونات مخصصة)" },
              { mod: "الوحدة 3", desc: "طبقة التعمية الفريدة (16-بايت وهمية + عكس البايتات)" },
              { mod: "الوحدة 4", desc: "خادم الاتصالات (Docker, Twilio, Redis, DynamoDB)" },
              { mod: "الوحدة 5", desc: "تكوين WebRTC (AEC3، تحسينات الشبكة)" },
              { mod: "الوحدة 6", desc: "طبقة الخصوصية (تكامل Tor SOCKS proxy)" },
              { mod: "الوحدة 7", desc: "تشفير قواعد البيانات (تكامل SQLCipher)" },
              { mod: "الوحدة 8", desc: "تحسين الأداء (ProGuard، إدارة الذاكرة)" },
            ].map((m, i) => (
              <div key={i} className="flex items-start gap-3 text-sm">
                <span className="px-2 py-1 bg-gray-800 text-accent rounded text-xs whitespace-nowrap">{m.mod}</span>
                <span className="text-text-secondary pt-0.5">{m.desc}</span>
              </div>
            ))}
          </div>
        </section>

      </div>
    </div>
  );
}
