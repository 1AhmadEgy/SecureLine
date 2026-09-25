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
  Lock,
  Fingerprint,
  ScanFace,
  Sparkles,
  Trash2,
  Clock,
  CheckCheck,
  Users
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import type { SecurityAuditEntry } from '../types';
import { 
  getWebAuthnStatus, 
  registerBiometricPasskey, 
  removeBiometricPasskey, 
  type WebAuthnStatus 
} from '../lib/webauthn';

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

  // WebAuthn state
  const [webAuthnStatus, setWebAuthnStatus] = useState<WebAuthnStatus>({
    supported: false,
    hasPlatformAuthenticator: false,
    isRegistered: false,
  });
  const [webAuthnLoading, setWebAuthnLoading] = useState(false);
  const [webAuthnNotice, setWebAuthnNotice] = useState<string | null>(null);

  // Auto-deletion saved setting
  const initialAutoDelete = typeof window !== 'undefined' 
    ? localStorage.getItem('secureline_auto_delete_setting') || '24 ساعة'
    : '24 ساعة';

  const initialReadReceipts = typeof window !== 'undefined'
    ? localStorage.getItem('secureline_read_receipts_enabled') !== 'false'
    : true;

  const initialTypingIndicators = typeof window !== 'undefined'
    ? localStorage.getItem('secureline_typing_enabled') !== 'false'
    : true;

  const [settingsGroups, setSettingsGroups] = useState<SettingGroup[]>([
    {
      title: 'المصادقة البيومترية وقفل التطبيق (WebAuthn / Passkeys)',
      icon: <Fingerprint size={18} className="text-accent" />,
      settings: [
        { 
          id: 'biometric_lock', 
          name: 'القفل البيومتري ببصمة الإصبع أو Face ID', 
          description: 'استخدام واجهة Web Authentication API للتحقق من هوية المستخدم عبر الشريحة الأمنية', 
          active: true 
        },
        { 
          id: 'tee_strongbox', 
          name: 'ربط المفاتيح بعتاد الأمان (Hardware TEE / StrongBox)', 
          description: 'توليد أزواج مفاتيح المصادقة في معالج أمان مستقل غير قابل للاختراق البرمجي', 
          active: true 
        },
      ]
    },
    {
      title: 'إشعارات القراءة المشفرة ومجموعات Signal (Group & Read Privacy)',
      icon: <CheckCheck size={18} className="text-accent" />,
      settings: [
        {
          id: 'e2ee_read_receipts',
          name: 'إشعارات القراءة المشفرة طرفياً (E2EE Read Receipts)',
          description: 'إرسال واستقبال تأكيدات القراءة كحزم مشفرة بنظام السقاطة تمنع الاستدلال على التوقيت أو الهوية',
          active: initialReadReceipts,
        },
        {
          id: 'blinded_receipt_tokens',
          name: 'تعمية المعرفات (Blinded Receipt Tokens - HMAC)',
          description: 'عدم إرسال معرفات الرسائل الصريحة للوسطاء؛ استخدام بصمات مجزأة معماة لحماية الخصوصية',
          active: true,
        },
        {
          id: 'signal_sender_keys',
          name: 'بروتوكول مفاتيح البث للمجموعات (Signal Sender Keys)',
          description: 'توزيع مفاتيح سلاسل التشفير بين أعضاء المجموعة مع تدوير المفاتيح فوراً عند إضافة أو إزالة أي عضو',
          active: true,
        },
        {
          id: 'typing_indicators',
          name: 'مؤشرات جاري الكتابة المشفرة (Encrypted Typing Indicators)',
          description: 'إرسال واستقبال إشعارات نبضات الكتابة مشفرة ومُموهة زمنياً لحماية خصوصية نمط الكتابة والسرعة',
          active: initialTypingIndicators,
        },
      ]
    },
    {
      title: 'خصوصية الرسائل والتدمير الذاتي (Auto-Deletion & Ephemerality)',
      icon: <Timer size={18} className="text-accent" />,
      settings: [
        { 
          id: 'auto_delete_messages',
          name: 'الحذف التلقائي للرسائل المشفرة (Auto-Deletion Policy)', 
          description: 'محو الرسائل وتصفير مفاتيحها المشتقة فور انقضاء المهلة لمنع التحليل الجنائي (Crypto-Shredding)', 
          active: initialAutoDelete !== 'معطل (احتفاظ دائم)',
          type: 'toggle-with-select',
          options: [
            'معطل (احتفاظ دائم)',
            '15 ثانية',
            '1 دقيقة',
            '1 ساعة',
            '24 ساعة',
            '7 أيام',
            '30 يوماً'
          ],
          currentValue: initialAutoDelete
        },
        { 
          id: 'panic_shred', 
          name: 'تفعيل زر التدمير الشامل الفوري (Panic Shred)', 
          description: 'إظهار زر طوارئ لحذف كافة المحادثات وتصفير الذاكرة العشوائية فوراً بنقرة واحدة', 
          active: true 
        },
      ]
    },
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
      title: 'التخزين المشفر ومشاركة الملفات (AES-256 Storage & Files)',
      icon: <Database size={18} className="text-accent" />,
      settings: [
        { id: 'sqlcipher', name: 'تشفير قاعدة البيانات (SQLCipher)', description: 'تشفير صفحات SQLite بـ 256-bit AES و 256,000 دورة PBKDF2', active: true },
        { id: 'file_aes256', name: 'تشفير الملفات المرفقة بـ AES-256-GCM', description: 'تشفير الملفات محلياً قبل البث مع رمز تحقق نزاهة SHA-256 و 12-byte Nonce', active: true },
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

  // Load WebAuthn details on mount
  useEffect(() => {
    refreshWebAuthn();
  }, []);

  const refreshWebAuthn = () => {
    getWebAuthnStatus().then(status => {
      setWebAuthnStatus(status);
    });
  };

  const handleRegisterBiometrics = async () => {
    setWebAuthnLoading(true);
    setWebAuthnNotice(null);
    const res = await registerBiometricPasskey('SecureLine Operator');
    setWebAuthnLoading(false);
    if (res.success) {
      refreshWebAuthn();
      setWebAuthnNotice('تم تسجيل بصمة Face ID / Fingerprint (WebAuthn Passkey) بنجاح!');
      setTimeout(() => setWebAuthnNotice(null), 4000);
    } else {
      setWebAuthnNotice(res.error || 'تعذر استكمال تسجيل البصمة');
      setTimeout(() => setWebAuthnNotice(null), 5000);
    }
  };

  const handleRemoveBiometrics = () => {
    removeBiometricPasskey();
    refreshWebAuthn();
    setWebAuthnNotice('تمت إزالة المفتاح البيومتري من الجهاز بنجاح.');
    setTimeout(() => setWebAuthnNotice(null), 3000);
  };

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
          const nextActive = !s.active;
          // If toggling auto delete
          if (settingId === 'auto_delete_messages') {
            const nextVal = nextActive ? (s.currentValue === 'معطل (احتفاظ دائم)' ? '24 ساعة' : s.currentValue || '24 ساعة') : 'معطل (احتفاظ دائم)';
            localStorage.setItem('secureline_auto_delete_setting', nextVal);
            window.dispatchEvent(new CustomEvent('secureline_autodelete_changed', { detail: nextVal }));
            return { ...s, active: nextActive, currentValue: nextVal };
          }
          if (settingId === 'e2ee_read_receipts') {
            localStorage.setItem('secureline_read_receipts_enabled', String(nextActive));
            window.dispatchEvent(new CustomEvent('secureline_receipts_toggled', { detail: nextActive }));
            return { ...s, active: nextActive };
          }
          if (settingId === 'typing_indicators') {
            localStorage.setItem('secureline_typing_enabled', String(nextActive));
            window.dispatchEvent(new CustomEvent('secureline_typing_toggled', { detail: nextActive }));
            return { ...s, active: nextActive };
          }
          return { ...s, active: nextActive };
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
          // If auto delete option changed
          if (settingId === 'auto_delete_messages') {
            const isActive = val !== 'معطل (احتفاظ دائم)';
            localStorage.setItem('secureline_auto_delete_setting', val);
            window.dispatchEvent(new CustomEvent('secureline_autodelete_changed', { detail: val }));
            return { ...s, currentValue: val, active: isActive };
          }
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
          <p className="text-text-secondary text-sm mt-1">تكوين طبقات التشفير، المصادقة البيومترية، والتدمير الذاتي للرسائل.</p>
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
                <h3 className="text-lg font-semibold text-text-primary">بروتوكول السقاطة المزدوجة والمصادقة البيومترية</h3>
                <p className="text-sm text-text-secondary leading-relaxed">
                  نظام التشفير يعمل وفق مواصفة libsignal مع تدوير مفاتيح X25519 بعد كل رسالة. 
                  يتم دعم المصادقة البيومترية المباشرة (Web Authentication API) عبر Face ID وبصمات الأصابع، 
                  مع الحذف التلقائي للرسائل المشفرة وتشفير الملفات بتقنية AES-256-GCM.
                </p>
              </div>
            </div>

            {/* WebAuthn Management Panel */}
            <div className="bg-primary border border-gray-800 rounded-xl p-5 space-y-4">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className="p-2.5 bg-accent/10 rounded-lg text-accent border border-accent/20">
                    <ScanFace size={22} />
                  </div>
                  <div>
                    <h4 className="font-semibold text-text-primary flex items-center gap-2">
                      <span>إدارة المصادقة البيومترية (Web Authentication API)</span>
                      <span className="text-[10px] bg-accent/10 text-accent border border-accent/20 px-2 py-0.5 rounded-full">
                        Passkeys FIDO2
                      </span>
                    </h4>
                    <p className="text-xs text-text-secondary mt-0.5">
                      {webAuthnStatus.hasPlatformAuthenticator 
                        ? 'مستشعر بيومتري مدعوم في هذا الجهاز (Touch ID / Face ID / Windows Hello)' 
                        : 'WebAuthn مدعوم على مستوى المتصفح ومتاح لربط مفاتيح الأمان'}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-2">
                  {webAuthnStatus.isRegistered ? (
                    <button
                      onClick={handleRemoveBiometrics}
                      className="px-3 py-1.5 rounded-lg text-xs font-medium text-red-400 border border-red-500/30 hover:bg-red-500/10 flex items-center gap-1.5 transition-colors"
                    >
                      <Trash2 size={13} />
                      <span>إلغاء البصمة</span>
                    </button>
                  ) : (
                    <button
                      onClick={handleRegisterBiometrics}
                      disabled={webAuthnLoading}
                      className="px-3 py-1.5 rounded-lg text-xs font-semibold bg-accent text-black hover:bg-emerald-300 flex items-center gap-1.5 transition-colors"
                    >
                      <Sparkles size={13} />
                      <span>{webAuthnLoading ? 'جاري التسجيل...' : 'تسجيل بصمة الآن'}</span>
                    </button>
                  )}
                </div>
              </div>

              {webAuthnNotice && (
                <div className="bg-accent/10 border border-accent/20 rounded-lg p-2.5 text-xs text-accent flex items-center gap-2">
                  <Check size={14} className="shrink-0" />
                  <span>{webAuthnNotice}</span>
                </div>
              )}

              <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 pt-2 text-xs">
                <div className="bg-black/60 border border-gray-800 p-2.5 rounded-lg">
                  <span className="text-gray-500 block text-[10px]">دعم Web Authentication API:</span>
                  <span className={`font-semibold mt-0.5 block ${webAuthnStatus.supported ? 'text-accent' : 'text-red-400'}`}>
                    {webAuthnStatus.supported ? 'مدعوم ومفعل ✓' : 'غير متوفر'}
                  </span>
                </div>
                <div className="bg-black/60 border border-gray-800 p-2.5 rounded-lg">
                  <span className="text-gray-500 block text-[10px]">المستشعر البيومتري للجهاز:</span>
                  <span className="font-semibold mt-0.5 text-text-primary block">
                    {webAuthnStatus.hasPlatformAuthenticator ? 'بصمة إصبع / Face ID نشط' : 'مستشعر افتراضي TEE'}
                  </span>
                </div>
                <div className="bg-black/60 border border-gray-800 p-2.5 rounded-lg">
                  <span className="text-gray-500 block text-[10px]">حالة تسجيل المفتاح (Passkey):</span>
                  <span className={`font-semibold mt-0.5 block ${webAuthnStatus.isRegistered ? 'text-accent' : 'text-amber-400'}`}>
                    {webAuthnStatus.isRegistered ? 'مسجل ومحمي بـ TEE ✓' : 'في انتظار التسجيل الأول'}
                  </span>
                </div>
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
                          <div className="flex items-center gap-2">
                            <h4 className="font-medium text-text-primary">{setting.name}</h4>
                            {setting.id === 'auto_delete_messages' && setting.active && (
                              <span className="text-[10px] bg-orange-500/10 border border-orange-500/30 text-orange-400 px-2 py-0.5 rounded flex items-center gap-1">
                                <Clock size={10} />
                                {setting.currentValue}
                              </span>
                            )}
                          </div>
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
