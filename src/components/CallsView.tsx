import { useState, useEffect } from 'react';
import { 
  Phone, 
  Video, 
  VideoOff, 
  Mic, 
  MicOff, 
  PhoneOff, 
  Settings2, 
  ShieldAlert, 
  Wifi, 
  Volume2, 
  ShieldCheck,
  Radio
} from 'lucide-react';

export function CallsView() {
  const [callActive, setCallActive] = useState(true);
  const [isMuted, setIsMuted] = useState(false);
  const [isVideoEnabled, setIsVideoEnabled] = useState(false);
  const [callSeconds, setCallSeconds] = useState(263); // 04:23 default
  const [fecActive, setFecActive] = useState(true);
  const [currentCodec, setCurrentCodec] = useState<'Opus-16k' | 'Opus-32k' | 'Opus-8k'>('Opus-16k');
  const [showCallSettings, setShowCallSettings] = useState(false);

  useEffect(() => {
    if (!callActive) return;
    const interval = setInterval(() => {
      setCallSeconds(prev => prev + 1);
    }, 1000);
    return () => clearInterval(interval);
  }, [callActive]);

  const formatDuration = (totalSeconds: number) => {
    const mins = Math.floor(totalSeconds / 60).toString().padStart(2, '0');
    const secs = (totalSeconds % 60).toString().padStart(2, '0');
    return `${mins}:${secs}`;
  };

  const toggleCall = () => {
    if (callActive) {
      setCallActive(false);
    } else {
      setCallActive(true);
      setCallSeconds(0);
    }
  };

  return (
    <div className="flex flex-col h-full bg-black">
      <div className="p-6 border-b border-gray-800 flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold flex items-center gap-2">
            مكالمة آمنة (WebRTC E2EE)
            {callActive && (
              <span className="text-xs bg-emerald-950 text-emerald-400 border border-emerald-800 px-2 py-0.5 rounded-full flex items-center gap-1 font-normal">
                <Radio size={12} className="animate-pulse" /> متصلة
              </span>
            )}
          </h2>
          <p className="text-text-secondary text-sm mt-1">اتصال ند-لند (P2P) مباشر محمي بـ SRTP مع تدوير المفاتيح أثناء المكالمة.</p>
        </div>

        <button 
          onClick={() => setShowCallSettings(!showCallSettings)}
          className={`p-2 rounded-lg border transition-colors ${showCallSettings ? 'bg-accent/20 text-accent border-accent/30' : 'bg-gray-900 border-gray-800 text-gray-400 hover:text-white'}`}
          title="معلمات الاتصال وتصحيح الحزم"
        >
          <Settings2 size={18} />
        </button>
      </div>

      <div className="flex-1 p-6 flex flex-col items-center justify-center">
        {callActive ? (
          <div className="relative w-full max-w-2xl aspect-video bg-primary rounded-2xl border border-gray-800 overflow-hidden flex flex-col items-center justify-center shadow-2xl">
            {/* Top Left Call Timer */}
            <div className="absolute top-4 start-4 bg-black/70 px-3 py-1.5 rounded-full backdrop-blur-md border border-gray-700 flex items-center gap-2">
              <div className="w-2 h-2 bg-accent rounded-full animate-pulse"></div>
              <span className="text-xs font-mono text-accent">{formatDuration(callSeconds)}</span>
            </div>
            
            {/* Real-time Telemetry Overlay */}
            <div className="absolute top-4 end-4 bg-black/70 px-3 py-2 rounded-xl backdrop-blur-md border border-gray-700 flex flex-col space-y-1 text-xs">
              <span className="text-text-secondary flex justify-between gap-4 w-48">
                الترميز: <span className="text-text-primary">{currentCodec}</span>
              </span>
              <span className="text-text-secondary flex justify-between gap-4 w-48">
                بروتوكول النقل: <span className="text-emerald-400">SRTP (DTLS 1.3)</span>
              </span>
              <span className="text-text-secondary flex justify-between gap-4 w-48">
                فقدان الحزم (Loss): <span className="text-accent font-mono">0.0%</span>
              </span>
              <span className="text-text-secondary flex justify-between gap-4 w-48">
                تصحيح FEC: <span className={fecActive ? 'text-accent' : 'text-gray-500'}>{fecActive ? 'نشط' : 'معطل'}</span>
              </span>
            </div>

            {/* Peer Avatar */}
            <div className="w-24 h-24 rounded-full bg-gray-800 border-2 border-accent/40 flex items-center justify-center text-3xl mb-4 shadow-[0_0_40px_rgba(0,255,170,0.15)] relative">
              <span className="font-semibold text-white">007</span>
              <span className="absolute bottom-0 end-0 w-6 h-6 bg-accent rounded-full flex items-center justify-center text-black">
                <ShieldCheck size={14} />
              </span>
            </div>

            <h3 className="text-xl font-medium text-white">العميل 007</h3>
            <p className="text-xs text-text-secondary mt-1 flex items-center gap-1.5">
              <ShieldAlert size={14} className="text-accent" />
              بصمة الأمان SAS: <span className="font-mono text-accent">5924-8103-9912</span>
            </p>

            {/* In-Call Settings Panel Popup */}
            {showCallSettings && (
              <div className="absolute inset-x-6 top-16 bg-black/90 border border-gray-700 rounded-xl p-4 backdrop-blur-md text-xs space-y-3 z-20">
                <div className="flex items-center justify-between border-b border-gray-800 pb-2">
                  <span className="font-semibold text-white flex items-center gap-1">
                    <Settings2 size={14} className="text-accent" /> معايير البث في الوقت الحقيقي
                  </span>
                  <button onClick={() => setShowCallSettings(false)} className="text-gray-400 hover:text-white">✕</button>
                </div>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                  <div>
                    <label className="text-gray-400 block mb-1">ملف ترميز Opus:</label>
                    <select
                      value={currentCodec}
                      onChange={(e) => setCurrentCodec(e.target.value as any)}
                      className="w-full bg-gray-900 border border-gray-700 text-white rounded p-1.5 outline-none"
                    >
                      <option value="Opus-16k">Opus 16 kbps (توفير النطاق والبطارية)</option>
                      <option value="Opus-32k">Opus 32 kbps (استوديو عالي النقاوة)</option>
                      <option value="Opus-8k">Opus 8 kbps (شبكات الأقمار والـ 2G)</option>
                    </select>
                  </div>
                  <div className="flex items-center justify-between p-2 bg-gray-900 rounded border border-gray-800">
                    <span className="text-gray-300">تصحيح الأخطاء التلقائي (Opus FEC):</span>
                    <button
                      onClick={() => setFecActive(!fecActive)}
                      className={`px-2 py-1 rounded font-semibold ${fecActive ? 'bg-accent text-black' : 'bg-gray-800 text-gray-400'}`}
                    >
                      {fecActive ? 'مفعل' : 'معطل'}
                    </button>
                  </div>
                </div>
              </div>
            )}
            
            {/* Picture in Picture */}
            <div className="absolute bottom-6 end-6 w-32 aspect-video bg-gray-900/90 rounded-lg border-2 border-gray-700 flex flex-col items-center justify-center overflow-hidden">
              {isVideoEnabled ? (
                <div className="w-full h-full bg-gray-800 flex items-center justify-center text-accent text-xs">
                  بث الفيديو نشط
                </div>
              ) : (
                <div className="flex flex-col items-center">
                  <VideoOff size={16} className="text-gray-500 mb-1" />
                  <span className="text-[10px] text-text-secondary">الكاميرا مغلقة</span>
                </div>
              )}
            </div>
          </div>
        ) : (
          <div className="w-full max-w-md aspect-video bg-primary/40 rounded-2xl border border-gray-800 flex flex-col items-center justify-center text-center p-6">
            <PhoneOff size={44} className="text-gray-600 mb-3" />
            <h3 className="text-lg font-semibold text-white">انتهت المكالمة</h3>
            <p className="text-xs text-text-secondary mt-1">تم مسح جميع مفاتيح تشفير الجلسة من ذاكرة الوصول العشوائي (RAM Zeroized).</p>
            <button
              onClick={toggleCall}
              className="mt-5 flex items-center gap-2 px-4 py-2 rounded-xl bg-accent text-black font-semibold text-sm hover:bg-emerald-300 transition-colors"
            >
              <Phone size={16} /> إعادة الاتصال الآمن
            </button>
          </div>
        )}

        {/* Call Controls */}
        {callActive && (
          <div className="flex items-center justify-center gap-5 mt-8">
            <button 
              onClick={() => setIsMuted(!isMuted)}
              className={`w-14 h-14 rounded-full flex items-center justify-center transition-all ${
                isMuted ? 'bg-red-500/20 text-red-400 border border-red-500/40' : 'bg-gray-800 text-text-primary hover:bg-gray-700'
              }`}
              title={isMuted ? 'إلغاء كتم الصوت' : 'كتم الميكروفون'}
            >
              {isMuted ? <MicOff size={22} /> : <Mic size={22} />}
            </button>

            <button 
              onClick={() => setIsVideoEnabled(!isVideoEnabled)}
              className={`w-14 h-14 rounded-full flex items-center justify-center transition-all ${
                isVideoEnabled ? 'bg-accent/20 text-accent border border-accent/40' : 'bg-gray-800 text-text-primary hover:bg-gray-700'
              }`}
              title={isVideoEnabled ? 'تعطيل الفيديو' : 'تفعيل الكاميرا'}
            >
              {isVideoEnabled ? <Video size={22} /> : <VideoOff size={22} />}
            </button>

            <button 
              onClick={() => setShowCallSettings(!showCallSettings)}
              className="w-14 h-14 rounded-full bg-gray-800 flex items-center justify-center text-text-primary hover:bg-gray-700 transition-colors"
              title="إعدادات الصوت الميدانية"
            >
              <Settings2 size={22} />
            </button>

            <button 
              onClick={toggleCall}
              className="w-16 h-16 rounded-full bg-red-500 flex items-center justify-center text-white hover:bg-red-600 transition-all shadow-lg shadow-red-500/30 active:scale-95"
              title="إنهاء المكالمة"
            >
              <PhoneOff size={28} />
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
