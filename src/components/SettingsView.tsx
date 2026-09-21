import { useState, useEffect, type ReactNode } from 'react';
import { 
  ShieldCheck, 
  EyeOff, 
  Database, 
  Globe, 
  Timer, 
  Monitor, 
  Smartphone, 
  Laptop, 
  QrCode, 
  Activity, 
  Check, 
  ShieldAlert, 
  Cpu,
  Lock
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import type { SecurityAuditEntry } from '../types';

interface SettingItem {
  id: string;
  name: string;
  description: string;
  active: boolean;
  type?: 'toggle' | 'toggle-with-select';
  options?: string[];
  currentValue?: string;
}

interface SettingGroup {
  title: string;
  icon: ReactNode;
  settings: SettingItem[];
}

export function SettingsView() {
  const { token } = useAuth();
  const [saveSuccess, setSaveSuccess] = useState(false);
  const [auditLogs, setAuditLogs] = useState<SecurityAuditEntry[]>([]);
  const [activeTab, setActiveTab] = useState<'settings' | 'audit'>('settings');

  const [settingsGroups, setSettingsGroups] = useState<SettingGroup[]>([
    {
      title: 'طبقة التعمية (Traffic Obfuscation)',
      icon: <EyeOff size={18} className="text-accent" />,
      settings: [
        { id: 'padding', name: 'بايتات وهمية (Dummy Padding)', description: 'إضافة 16-64 بايت عشوائية لإخفاء حجم وتوزيع الحزم', active: true },
        { id: 'reverse', name: 'عكس البايتات (Byte Reversal)', description: 'عكس ترتيب البايتات المشفرة قبل الإرسال لمنع بصمة البروتوكول', active: true },
      ]
    },
    {
      title: 'خصوصية الشبكة والتوجيه المجهول',
      icon: <Globe size={18} className="text-accent" />,
      settings: [
        { id: 'tor_mode', name: 'وضع الخفاء الكامل (Tor Routing)', description: 'تمرير كافة الإشارات والبيانات عبر شبكة Tor (SOCKS5 بدون تسريب DNS)', active: true },
        { id: 'signal_server', name: 'خادم إشارات مجهول (Blind Routing)', description: 'الخادم يرى العناوين المجهولة فقط ولا يخزن أي مفاتيح تشفير', active: true },
      ]
    },
    {
      title: 'التخزين المشفر على الجهاز (Data-at-Rest)',
      icon: <Database size={18} className="text-accent" />,
      settings: [
        { id: 'sqlcipher', name: 'تشفير قاعدة البيانات (SQLCipher)', description: 'تشفير صفحات SQLite بـ 256-bit AES و 256,000 دورة PBKDF2', active: true },
        { id: 'keystore', name: 'مخزن المفاتيح في العتاد (TEE / StrongBox)', description: 'توليد وتخزين مفاتيح الهوية داخل معالج الأمان المستقل للأجهزة', active: true },
      ]
    },
    {
      title: 'خصوصية الرسائل والتدمير الذاتي',
      icon: <Timer size={18} className="text-accent" />,
      settings: [
        { 
          id: 'ephemeral',
          name: 'رسائل ذاتية الاختفاء (Crypto-Shredding)', 
          description: 'تصفير المفتاح المشتق فور انقضاء المهلة وحذف البايتات نهائياً', 
          active: true,
          type: 'toggle-with-select',
          options: ['15 ثانية', '60 ثانية', '24 ساعة', '1 أسبوع'],
          currentValue: '60 ثانية'
        },
      ]
    },
    {
      title: 'جودة المكالمات الآمنة (WebRTC Audio)',
      icon: <Activity size={18} className="text-accent" />,
      settings: [
        { 
          id: 'bitrate',
          name: 'معدل بث الصوت (Opus Mode)', 
          description: 'التحكم في استهلاك البيانات أثناء المكالمات الصوتية المشفرة', 
          active: true,
          type: 'toggle-with-select',
          options: ['جودة عالية (32 kbps)', 'توفير البيانات (16 kbps)', 'توفير أقصى (8 kbps)'],
          currentValue: 'توفير البيانات (16 kbps)'
        },
        { id: 'fec', name: 'تصحيح الأخطاء الاستباقي (Opus FEC)', description: 'استعادة الحزم الصوتية المفقودة تلقائياً على الشبكات الضعيفة', active: true },
        { id: 'aec3', name: 'إلغاء الصدى والضجيج (AEC3 & AGC)', description: 'معالجة الإشارة الصوتية لمنع ارتداد الصوت والضوضاء المحيطة', active: true },
      ]
    }
  ]);

  // Fetch security audit trail when user visits audit tab
  useEffect(() => {
    if (activeTab === 'audit' && token) {
      fetch('/api/security/audit', {
        headers: { Authorization: `Bearer ${token}` }
      })
        .then(res => res.json())
        .then(data => {
          if (data.logs) setAuditLogs(data.logs);
        })
        .catch(err => console.error('Failed to load audit logs:', err));
    }
  }, [activeTab, token]);

  const toggleSetting = (groupIdx: number, settingId: string) => {
    setSettingsGroups(prev => {
      const copy = [...prev];
      const targetGroup = { ...copy[groupIdx] };
      targetGroup.settings = targetGroup.settings.map(s => {
        if (s.id === settingId) {
          return { ...s, active: !s.active };
        }
        return s;
      });
      copy[groupIdx] = targetGroup;
      return copy;
    });

    setSaveSuccess(true);
    setTimeout(() => setSaveSuccess(false), 2000);
  };

  const changeOption = (groupIdx: number, settingId: string, val: string) => {
    setSettingsGroups(prev => {
      const copy = [...prev];
      const targetGroup = { ...copy[groupIdx] };
      targetGroup.settings = targetGroup.settings.map(s => {
        if (s.id === settingId) {
          return { ...s, currentValue: val };
        }
        return s;
      });
      copy[groupIdx] = targetGroup;
      return copy;
    });

    setSaveSuccess(true);
    setTimeout(() => setSaveSuccess(false), 2000);
  };

  return (
    <div className="flex flex-col h-full bg-black overflow-y-auto">
      {/* Header with quick tabs */}
      <div className="p-6 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h2 className="text-2xl font-bold">إعدادات الأمان ومصفوفة التدقيق</h2>
          <p className="text-text-secondary text-sm mt-1">تكوين طبقات التشفير والتعمية والمراقبة الميدانية.</p>
        </div>

        <div className="flex items-center gap-2">
          <div className="bg-gray-900 p-1 rounded-lg border border-gray-800 flex text-xs">
            <button
              onClick={() => setActiveTab('settings')}
              className={`px-3 py-1.5 rounded-md transition-colors ${
                activeTab === 'settings' ? 'bg-accent text-black font-semibold' : 'text-text-secondary hover:text-white'
              }`}
            >
              البروتوكولات
            </button>
            <button
              onClick={() => setActiveTab('audit')}
              className={`px-3 py-1.5 rounded-md transition-colors ${
                activeTab === 'audit' ? 'bg-accent text-black font-semibold' : 'text-text-secondary hover:text-white'
              }`}
            >
              سجل التدقيق الأمني
            </button>
          </div>
          {saveSuccess && (
            <span className="flex items-center text-xs text-accent bg-accent/10 border border-accent/20 px-2 py-1 rounded-md">
              <Check size={12} className="me-1" /> تم التحديث
            </span>
          )}
        </div>
      </div>

      <div className="p-6 max-w-4xl space-y-8">
        {activeTab === 'settings' ? (
          <>
            {/* Status Card */}
            <div className="bg-primary/50 border border-gray-800 rounded-xl p-5 flex items-start gap-4">
              <div className="p-3 bg-accent/10 rounded-full shrink-0 border border-accent/20">
                <ShieldCheck size={24} className="text-accent" />
              </div>
              <div className="space-y-1">
                <h3 className="text-lg font-semibold text-text-primary">بروتوكول السقاطة المزدوجة ومفتاح الجذر المتجدد</h3>
                <p className="text-sm text-text-secondary leading-relaxed">
                  نظام التشفير يعمل وفق مواصفة libsignal مع تدوير مفاتيح X25519 بعد كل رسالة (Forward Secrecy & Break-in Recovery).
                  تشفير البيانات المخزنة يعمل بـ SQLCipher AES-256 مع تخزين المفاتيح داخل Android StrongBox Keymaster.
                </p>
              </div>
            </div>

            {/* Config Toggles */}
            <div className="space-y-6">
              {settingsGroups.map((group, gIdx) => (
                <div key={gIdx} className="space-y-3">
                  <h3 className="text-sm font-medium text-text-secondary uppercase tracking-wider flex items-center gap-2">
                    {group.icon}
                    <span>{group.title}</span>
                  </h3>
                  <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden divide-y divide-gray-800">
                    {group.settings.map((setting) => (
                      <div key={setting.id} className="p-4 flex flex-col sm:flex-row sm:items-center justify-between gap-3 hover:bg-gray-800/30 transition-colors">
                        <div>
                          <h4 className="font-medium text-text-primary">{setting.name}</h4>
                          <p className="text-xs text-text-secondary mt-1 max-w-xl">{setting.description}</p>
                        </div>

                        <div className="flex items-center gap-3 shrink-0 self-end sm:self-center">
                          {setting.type === 'toggle-with-select' && setting.options && (
                            <select
                              value={setting.currentValue}
                              onChange={(e) => changeOption(gIdx, setting.id, e.target.value)}
                              className="bg-black border border-gray-700 text-text-primary text-xs rounded-lg focus:ring-accent focus:border-accent p-2 outline-none cursor-pointer"
                            >
                              {setting.options.map((opt) => (
                                <option key={opt} value={opt}>{opt}</option>
                              ))}
                            </select>
                          )}

                          <button
                            onClick={() => toggleSetting(gIdx, setting.id)}
                            type="button"
                            className="relative inline-flex items-center cursor-pointer p-0 border-0 bg-transparent"
                            aria-label={`تبديل ${setting.name}`}
                          >
                            <div className={`w-12 h-6 rounded-full transition-colors relative ${setting.active ? 'bg-accent' : 'bg-gray-800 border border-gray-700'}`}>
                              <div className={`absolute top-[2px] w-5 h-5 rounded-full bg-black transition-transform ${setting.active ? 'start-[2px]' : 'start-[26px]'}`}></div>
                            </div>
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>

            {/* Linked Devices */}
            <div className="space-y-3 pt-6 border-t border-gray-800">
              <h3 className="text-sm font-medium text-text-secondary uppercase tracking-wider flex items-center gap-2">
                <Monitor size={18} className="text-accent" />
                <span>الأجهزة المرتبطة (Multi-Device Provisioning)</span>
              </h3>
              <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden divide-y divide-gray-800">
                <div className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                  <div className="flex items-center gap-4">
                    <div className="p-2 bg-gray-800 rounded-lg">
                      <Smartphone className="text-accent" size={20} />
                    </div>
                    <div>
                      <h4 className="font-medium text-text-primary">Pixel 8 Pro (الجهاز الأساسي Master)</h4>
                      <p className="text-xs text-accent mt-0.5">نشط الآن • مفتاح التوقيع مثبت في Hardware StrongBox</p>
                    </div>
                  </div>
                  <span className="text-[10px] bg-accent/10 border border-accent/20 text-accent px-2 py-0.5 rounded">رئيسي</span>
                </div>

                <div className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                  <div className="flex items-center gap-4">
                    <div className="p-2 bg-gray-800 rounded-lg">
                      <Laptop className="text-text-primary" size={20} />
                    </div>
                    <div>
                      <h4 className="font-medium text-text-primary">SecureLine Workstation (Linux Desktop)</h4>
                      <p className="text-xs text-text-secondary mt-0.5">جلسة مشفرة عبر WebRTC DataChannel • نشط قبل 15 دقيقة</p>
                    </div>
                  </div>
                  <button className="text-xs font-medium text-red-400 hover:text-red-300 transition-colors px-3 py-1.5 rounded-lg border border-red-400/20 hover:bg-red-400/10">
                    إلغاء التفويض
                  </button>
                </div>

                <button className="w-full p-4 bg-gray-900/40 hover:bg-gray-800/60 transition-colors flex items-center justify-center gap-2 text-accent group">
                  <QrCode size={18} className="group-hover:scale-110 transition-transform" />
                  <span className="font-medium text-sm">مسح رمز QR مشفر لمزامنة جهاز مصاحب</span>
                </button>
              </div>
            </div>
          </>
        ) : (
          /* Audit Logs Tab */
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <h3 className="font-semibold text-text-primary flex items-center gap-2">
                <ShieldAlert size={18} className="text-accent" />
                سجل العمليات الأمنية في قاعدة البيانات (Audit Trail)
              </h3>
              <span className="text-xs text-text-secondary">تسجيل فوري للأحداث بدون كشف المحتوى</span>
            </div>

            <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden divide-y divide-gray-800">
              {auditLogs.length === 0 ? (
                <div className="p-8 text-center text-text-secondary text-sm">
                  <Cpu size={28} className="mx-auto mb-2 opacity-50" />
                  لا توجد أحداث أمنية مسجلة بعد، أو سجل الدخول لاستعراض سجلات قاعدة بيانات Cloud SQL المحدثة.
                </div>
              ) : (
                auditLogs.map((log) => (
                  <div key={log.id} className="p-4 flex items-start justify-between text-xs font-mono">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <span className="text-accent font-bold">[{log.eventType}]</span>
                        <span className="text-gray-400">{log.details || 'عملية أمنية قياسية'}</span>
                      </div>
                      {log.ipAddress && (
                        <div className="text-gray-500">العنوان البعيد المشفر: {log.ipAddress}</div>
                      )}
                    </div>
                    <span className="text-gray-500 whitespace-nowrap">
                      {new Date(log.createdAt).toLocaleTimeString()}
                    </span>
                  </div>
                ))
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
