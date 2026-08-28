import { ShieldCheck, EyeOff, Key, Server, Database, Globe, Timer, Monitor, Smartphone, Laptop, QrCode, Activity } from 'lucide-react';

export function SettingsView() {
  const settingsGroups = [
    {
      title: 'طبقة التعمية (Obfuscation)',
      icon: <EyeOff size={18} className="text-accent" />,
      settings: [
        { name: 'بايتات وهمية (Dummy Padding)', description: 'إضافة 16-بايت من البيانات العشوائية لإخفاء حجم الرسالة', active: true },
        { name: 'عكس البايتات (Byte Reversal)', description: 'عكس ترتيب البايتات قبل الإرسال لمنع التحليل', active: true },
      ]
    },
    {
      title: 'خصوصية الشبكة',
      icon: <Globe size={18} className="text-accent" />,
      settings: [
        { name: 'وضع الخفاء الكامل (Tor)', description: 'تمرير كل الاتصالات عبر شبكة Tor. يخفي عنوان IP بالكامل.', active: false },
        { name: 'خادم إشارات Signal', description: 'استخدام خادم Signal للإشارات فقط، بدون وصول للبيانات', active: true },
      ]
    },
    {
      title: 'التخزين المحلي (Data-at-Rest)',
      icon: <Database size={18} className="text-accent" />,
      settings: [
        { name: 'تشفير قاعدة البيانات', description: 'يشفر كل الرسائل المخزنة على الجهاز باستخدام SQLCipher و AES-256. يتطلب إعادة تشغيل التطبيق.', active: true },
        { name: 'مخزن المفاتيح (Keystore)', description: 'تخزين المفاتيح في الشريحة الآمنة (TEE / Secure Enclave)', active: true },
      ]
    },
    {
      title: 'خصوصية الرسائل',
      icon: <Timer size={18} className="text-accent" />,
      settings: [
        { 
          name: 'رسائل ذاتية الاختفاء', 
          description: 'حذف الرسائل تلقائياً بعد مدة محددة', 
          active: true,
          type: 'toggle-with-select',
          options: ['1 ساعة', '24 ساعة', '1 أسبوع'],
          currentValue: '24 ساعة'
        },
      ]
    },
    {
      title: 'جودة المكالمات (WebRTC)',
      icon: <Activity size={18} className="text-accent" />,
      settings: [
        { 
          name: 'وضع توفير البيانات', 
          description: 'التحكم في استهلاك البيانات أثناء المكالمات الصوتية', 
          active: true,
          type: 'toggle-with-select',
          options: ['جودة عالية (32 kbps)', 'توفير البيانات (16 kbps)', 'توفير أقصى (8 kbps)'],
          currentValue: 'توفير البيانات (16 kbps)'
        },
        { name: 'تصحيح الأخطاء (FEC)', description: 'استعادة حزم الصوت المفقودة على الشبكات الضعيفة (Opus FEC)', active: true },
        { name: 'إلغاء الصدى والضوضاء', description: 'تصفية الصوت وتفعيل التحكم التلقائي بمستوى الصوت (AGC & AEC3)', active: true },
      ]
    }
  ];

  return (
    <div className="flex flex-col h-full bg-black overflow-y-auto">
      <div className="p-6 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10">
        <h2 className="text-2xl font-bold">إعدادات الأمان</h2>
        <p className="text-text-secondary text-sm mt-1">تكوين بروتوكولات الخصوصية والتشفير المتقدمة.</p>
      </div>

      <div className="p-6 max-w-3xl space-y-8">
        
        <div className="bg-primary/50 border border-gray-800 rounded-xl p-5 flex items-start gap-4">
          <div className="p-3 bg-accent/10 rounded-full shrink-0">
            <ShieldCheck size={24} className="text-accent" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-text-primary">بروتوكول السقاطة المزدوجة (Double Ratchet)</h3>
            <p className="text-sm text-text-secondary mt-1">
              نستخدم حالياً مكتبة libsignal لتوفير تشفير (X25519, AES-256, HMAC-SHA256) من طرف إلى طرف.
              يتم تدوير مفاتيح التشفير بشكل آمن بعد كل رسالة.
            </p>
          </div>
        </div>

        <div className="space-y-6">
          {settingsGroups.map((group, idx) => (
            <div key={idx} className="space-y-3">
              <h3 className="text-sm font-medium text-text-secondary uppercase tracking-wider flex items-center gap-2">
                {group.icon}
                <span>{group.title}</span>
              </h3>
              <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden divide-y divide-gray-800">
                {group.settings.map((setting, sIdx) => (
                  <div key={sIdx} className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                    <div>
                      <h4 className="font-medium text-text-primary">{setting.name}</h4>
                      <p className="text-xs text-text-secondary mt-1">{setting.description}</p>
                    </div>
                    {((setting as any).type === 'toggle-with-select') ? (
                      <div className="flex items-center gap-3">
                        <select 
                          className="bg-black border border-gray-700 text-text-primary text-sm rounded-lg focus:ring-accent focus:border-accent block p-2 outline-none cursor-pointer"
                          defaultValue={(setting as any).currentValue}
                        >
                          {(setting as any).options.map((opt: string) => (
                            <option key={opt} value={opt}>{opt}</option>
                          ))}
                        </select>
                        <div className="relative inline-flex items-center cursor-pointer">
                          <div className={`w-11 h-6 rounded-full peer relative outline-none transition-colors ${setting.active ? 'bg-accent' : 'bg-gray-700'}`}>
                            <div className={`absolute top-[2px] start-[2px] bg-black rounded-full h-5 w-5 transition-transform ${setting.active ? '-translate-x-[100%]' : ''}`}></div>
                          </div>
                        </div>
                      </div>
                    ) : (
                      <div className="relative inline-flex items-center cursor-pointer">
                        <div className={`w-11 h-6 rounded-full peer relative outline-none transition-colors ${setting.active ? 'bg-accent' : 'bg-gray-700'}`}>
                          <div className={`absolute top-[2px] start-[2px] bg-black rounded-full h-5 w-5 transition-transform ${setting.active ? '-translate-x-[100%]' : ''}`}></div>
                        </div>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>

        <div className="space-y-3 pt-6 border-t border-gray-800">
          <h3 className="text-sm font-medium text-text-secondary uppercase tracking-wider flex items-center gap-2">
            <Monitor size={18} className="text-accent" />
            <span>الأجهزة المرتبطة</span>
          </h3>
          <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden divide-y divide-gray-800">
            
            <div className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                <div className="flex items-center gap-4">
                  <div className="p-2 bg-gray-800 rounded-lg">
                    <Smartphone className="text-text-primary" size={20} />
                  </div>
                  <div>
                    <h4 className="font-medium text-text-primary">iPhone 14 Pro (هذا الجهاز)</h4>
                    <p className="text-xs text-accent mt-0.5">نشط الآن • الموقع مخفي</p>
                  </div>
                </div>
            </div>
            
            <div className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                <div className="flex items-center gap-4">
                  <div className="p-2 bg-gray-800 rounded-lg">
                    <Laptop className="text-text-primary" size={20} />
                  </div>
                  <div>
                    <h4 className="font-medium text-text-primary">SecureLine Desktop (macOS)</h4>
                    <p className="text-xs text-text-secondary mt-0.5">آخر نشاط: قبل ساعتين</p>
                  </div>
                </div>
                <button className="text-xs font-medium text-red-400 hover:text-red-300 transition-colors px-3 py-1.5 rounded-lg border border-red-400/20 hover:bg-red-400/10">إلغاء الربط</button>
            </div>
            
            <button className="w-full p-4 bg-gray-900/40 hover:bg-gray-800/60 transition-colors flex items-center justify-center gap-2 text-accent group">
                <QrCode size={18} className="group-hover:scale-110 transition-transform" />
                <span className="font-medium text-sm">مسح رمز QR لربط جهاز جديد</span>
            </button>
          </div>
        </div>

      </div>
    </div>
  );
}
