import { Target, ShieldAlert, Fingerprint, RefreshCw, KeyRound, WifiOff, Users, Cpu } from 'lucide-react';

export function VisionView() {
  const features = [
    {
      icon: <Fingerprint size={20} className="text-accent" />,
      title: "وضع الشبح (Ghost Mode)",
      desc: "إخفاء كل المحادثات السابقة، لا تظهر إلا عند فك القفل بالبصمة للحماية من التفتيش الجسدي."
    },
    {
      icon: <KeyRound size={20} className="text-accent" />,
      title: "وضع الفخ (Decoy Mode)",
      desc: "رمز سري بديل يفتح نسخة 'نظيفة' من التطبيق بدون أي رسائل حساسة للحماية في حالات الإكراه."
    },
    {
      icon: <RefreshCw size={20} className="text-accent" />,
      title: "تحديث المفاتيح (24h)",
      desc: "تغيير مفاتيح التشفير تلقائياً كل 24 ساعة لمنع التنصت طويل الأمد."
    },
    {
      icon: <WifiOff size={20} className="text-accent" />,
      title: "المراسلة بدون إنترنت",
      desc: "إرسال رسائل مشفرة عبر البلوتوث أو شبكة WiFi مباشرة في مناطق حجب الإنترنت."
    },
    {
      icon: <Users size={20} className="text-accent" />,
      title: "هوية لامركزية (DID)",
      desc: "لا حاجة لرقم هاتف. إضافة جهات الاتصال عبر مسح كود QR يحتوي على بصمة المفتاح العام."
    },
    {
      icon: <Cpu size={20} className="text-accent" />,
      title: "واجهة AMOLED خفيفة",
      desc: "خلفيات سوداء نقية تطفئ البكسلات لتوفير البطارية، وتستهلك أقل من 50% من الذاكرة مقارنة بالمنافسين."
    }
  ];

  const competitors = [
    { name: "الميزة", whatsapp: "WhatsApp", telegram: "Telegram", signal: "Signal", us: "SecureLine" },
    { name: "تشفير E2E افتراضي", whatsapp: "نعم", telegram: "لا (السري فقط)", signal: "نعم", us: "نعم" },
    { name: "بدون رقم هاتف", whatsapp: "لا", telegram: "لا", signal: "لا", us: "نعم" },
    { name: "إخفاء عنوان IP", whatsapp: "لا", telegram: "لا", signal: "لا", us: "نعم (Tor)" },
    { name: "وضع الفخ (Decoy)", whatsapp: "لا", telegram: "لا", signal: "لا", us: "نعم" },
    { name: "واجهة خفيفة", whatsapp: "لا", telegram: "لا", signal: "نعم", us: "نعم++" },
    { name: "مفتوح المصدر", whatsapp: "لا", telegram: "العميل فقط", signal: "نعم", us: "نعم" },
    { name: "لامركزي", whatsapp: "لا", telegram: "جزئي", signal: "لا", us: "نعم" },
  ];

  return (
    <div className="flex flex-col h-full bg-black overflow-y-auto">
      <div className="p-6 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10 flex items-center gap-3">
        <Target className="text-accent" size={28} />
        <div>
          <h2 className="text-2xl font-bold">رؤية واستراتيجية المشروع</h2>
          <p className="text-text-secondary text-sm mt-1">الملخص التنفيذي لتطبيق SecureLine</p>
        </div>
      </div>

      <div className="p-6 max-w-5xl space-y-10">
        
        {/* The Vision */}
        <section className="bg-primary/30 border border-accent/20 rounded-2xl p-8 relative overflow-hidden">
          <div className="absolute top-0 end-0 w-64 h-64 bg-accent/5 rounded-full blur-3xl -me-20 -mt-20"></div>
          <h3 className="text-xl font-bold text-white mb-4">الرؤية (The Vision)</h3>
          <p className="text-lg text-text-secondary leading-relaxed italic border-s-2 border-accent ps-4">
            "أن نصبح المعيار الذهبي للاتصالات الخاصة، حيث لا يستطيع أحد — لا الحكومات، ولا الشركات، 
            ولا حتى مطورو التطبيق أنفسهم — معرفة هوية المستخدمين أو محتوى اتصالاتهم."
          </p>
        </section>

        {/* Unique Features */}
        <section>
          <h3 className="text-lg font-semibold text-white mb-4 flex items-center">
            <ShieldAlert className="me-2 text-accent" size={20} />
            الميزات الفريدة (Unique Features)
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {features.map((f, i) => (
              <div key={i} className="bg-primary border border-gray-800 rounded-xl p-5 hover:border-gray-600 transition-colors">
                <div className="w-10 h-10 rounded-lg bg-gray-900 flex items-center justify-center mb-4">
                  {f.icon}
                </div>
                <h4 className="font-semibold text-white mb-2">{f.title}</h4>
                <p className="text-sm text-text-secondary leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </section>

        {/* Competitive Matrix */}
        <section>
          <h3 className="text-lg font-semibold text-white mb-4">مصفوفة التحليل التنافسي</h3>
          <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden overflow-x-auto">
            <table className="w-full text-sm text-start">
              <thead className="bg-gray-900/50 text-text-secondary text-xs uppercase text-start">
                <tr>
                  <th className="px-6 py-4 font-medium text-start">{competitors[0].name}</th>
                  <th className="px-6 py-4 font-medium text-start">{competitors[0].whatsapp}</th>
                  <th className="px-6 py-4 font-medium text-start">{competitors[0].telegram}</th>
                  <th className="px-6 py-4 font-medium text-start">{competitors[0].signal}</th>
                  <th className="px-6 py-4 font-medium text-accent text-start">{competitors[0].us}</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-800">
                {competitors.slice(1).map((row, i) => (
                  <tr key={i} className="hover:bg-gray-800/20 transition-colors">
                    <td className="px-6 py-4 text-white font-medium">{row.name}</td>
                    <td className="px-6 py-4 text-text-secondary">{row.whatsapp}</td>
                    <td className="px-6 py-4 text-text-secondary">{row.telegram}</td>
                    <td className="px-6 py-4 text-text-secondary">{row.signal}</td>
                    <td className="px-6 py-4 text-accent font-medium">{row.us}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>

        {/* Roadmap */}
        <section className="pb-8">
          <h3 className="text-lg font-semibold text-white mb-6">خارطة الطريق التنفيذية (Roadmap)</h3>
          <div className="space-y-6 relative before:absolute before:inset-0 before:ms-2 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-accent before:via-gray-800 before:to-transparent">
            
            <div className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active">
              <div className="flex items-center justify-center w-5 h-5 rounded-full border-2 border-accent bg-black shrink-0 md:order-1 md:group-odd:translate-x-1/2 md:group-even:-translate-x-1/2 shadow-[0_0_10px_rgba(0,255,170,0.5)] z-10"></div>
              <div className="w-[calc(100%-2rem)] md:w-[calc(50%-1.5rem)] bg-primary border border-accent/30 p-5 rounded-xl shadow-lg">
                <div className="flex items-center justify-between mb-1">
                  <h4 className="font-bold text-white text-base">المرحلة 1: MVP (3 أشهر)</h4>
                  <span className="text-xs font-medium text-black bg-accent px-2 py-1 rounded-full">الحالية</span>
                </div>
                <ul className="text-sm text-text-secondary space-y-1 mt-3">
                  <li className="flex items-center text-white"><span className="text-accent me-2">✓</span> تشغيل Signal-Android محلياً</li>
                  <li className="flex items-center text-white"><span className="text-accent me-2">✓</span> تغيير الهوية والواجهة (AMOLED)</li>
                  <li className="flex items-center text-white"><span className="text-accent me-2">✓</span> إضافة طبقة التعمية</li>
                  <li className="flex items-center"><span className="w-1.5 h-1.5 rounded-full bg-gray-600 me-2.5 ms-0.5"></span> اختبار مكالمات P2P</li>
                </ul>
              </div>
            </div>

            <div className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group">
              <div className="flex items-center justify-center w-5 h-5 rounded-full border-2 border-gray-700 bg-black shrink-0 md:order-1 md:group-odd:translate-x-1/2 md:group-even:-translate-x-1/2 z-10"></div>
              <div className="w-[calc(100%-2rem)] md:w-[calc(50%-1.5rem)] bg-primary border border-gray-800 p-5 rounded-xl opacity-70">
                <h4 className="font-bold text-white text-base mb-1">المرحلة 2: الإطلاق التجريبي (6 أشهر)</h4>
                <ul className="text-sm text-text-secondary space-y-1 mt-3">
                  <li>• دمج شبكة Tor</li>
                  <li>• تنفيذ SQLCipher</li>
                  <li>• اختبار على أجهزة متعددة</li>
                  <li>• إطلاق على متجر F-Droid</li>
                </ul>
              </div>
            </div>

            <div className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group">
              <div className="flex items-center justify-center w-5 h-5 rounded-full border-2 border-gray-800 bg-black shrink-0 md:order-1 md:group-odd:translate-x-1/2 md:group-even:-translate-x-1/2 z-10"></div>
              <div className="w-[calc(100%-2rem)] md:w-[calc(50%-1.5rem)] bg-transparent border border-dashed border-gray-800 p-5 rounded-xl opacity-50">
                <h4 className="font-bold text-white text-base mb-1">المرحلة 3: الإطلاق الكامل (12 شهراً)</h4>
                <p className="text-sm text-text-secondary mt-2">نسخة iOS، نسخة سطح المكتب (Desktop)، وإطلاق برنامج مكافآت الباحثين الأمنيين (Bug Bounty).</p>
              </div>
            </div>

          </div>
        </section>

      </div>
    </div>
  );
}
