import { Fingerprint, Lock, KeyRound, ShieldAlert, Check } from 'lucide-react';
import { useState, type FormEvent } from 'react';

interface AppLockProps {
  onUnlock: (isDecoy?: boolean) => void;
}

export function AppLock({ onUnlock }: AppLockProps) {
  const [authenticating, setAuthenticating] = useState(false);
  const [mode, setMode] = useState<'biometric' | 'pin'>('biometric');
  const [pin, setPin] = useState('');
  const [pinError, setPinError] = useState(false);

  const handleUnlockBiometric = () => {
    setAuthenticating(true);
    setTimeout(() => {
      onUnlock(false);
    }, 900);
  };

  const handlePinSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (pin === '0000') {
      // Decoy PIN
      setAuthenticating(true);
      setTimeout(() => {
        onUnlock(true);
      }, 600);
    } else if (pin === '1337' || pin.length >= 4) {
      // Normal PIN
      setAuthenticating(true);
      setTimeout(() => {
        onUnlock(false);
      }, 600);
    } else {
      setPinError(true);
      setTimeout(() => setPinError(false), 1500);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-black text-text-primary selection:bg-accent selection:text-black p-4">
      <div className="flex flex-col items-center space-y-6 max-w-sm w-full">
        <div className="w-16 h-16 rounded-full bg-accent/10 flex items-center justify-center border border-accent/20">
          <Lock className="text-accent" size={30} />
        </div>
        
        <div className="text-center space-y-1.5">
          <h1 className="text-2xl font-bold tracking-tight">SecureLine مقفل</h1>
          <p className="text-text-secondary text-xs">
            {mode === 'biometric' ? 'المصادقة عبر الشريحة البيومترية المستقلة (TEE)' : 'أدخل رمز المرور لفك تشفير مخزن المفاتيح'}
          </p>
        </div>

        {mode === 'biometric' ? (
          <div className="flex flex-col items-center py-4">
            <button 
              onClick={handleUnlockBiometric}
              disabled={authenticating}
              className={`relative flex flex-col items-center justify-center w-28 h-28 rounded-full border-2 transition-all duration-300 ${
                authenticating 
                  ? 'border-accent bg-accent/5 scale-95 shadow-[0_0_30px_rgba(0,255,170,0.25)]' 
                  : 'border-gray-800 hover:border-accent/40 bg-gray-900/50'
              }`}
              title="انقر لمحاكاة بصمة الإصبع"
            >
              <Fingerprint 
                size={44} 
                className={`transition-colors duration-300 ${authenticating ? 'text-accent animate-pulse' : 'text-text-secondary'}`} 
              />
            </button>
            <span className={`text-xs font-medium mt-3 transition-colors ${authenticating ? 'text-accent' : 'text-text-secondary'}`}>
              {authenticating ? 'جاري فك التشفير الآمن...' : 'انقر على البصمة لفك القفل'}
            </span>
          </div>
        ) : (
          <form onSubmit={handlePinSubmit} className="w-full space-y-4">
            <div className="relative">
              <input
                type="password"
                maxLength={8}
                value={pin}
                onChange={(e) => setPin(e.target.value)}
                placeholder="أدخل الرمز (مثال: 1337 أو 0000 للفخ)"
                className={`w-full bg-gray-900 border text-center text-lg tracking-widest text-white rounded-xl py-3 px-4 outline-none focus:ring-1 focus:ring-accent ${
                  pinError ? 'border-red-500 bg-red-950/20' : 'border-gray-800'
                }`}
                autoFocus
              />
              {pinError && (
                <span className="text-red-400 text-xs mt-1 block text-center">رمز غير صحيح!</span>
              )}
            </div>

            <button
              type="submit"
              className="w-full py-3 bg-accent text-black font-semibold rounded-xl hover:bg-emerald-300 transition-colors text-sm"
            >
              تأكيد الدخول
            </button>
          </form>
        )}

        {/* Toggle Mode */}
        <div className="pt-2 flex items-center justify-center gap-4 text-xs">
          <button
            onClick={() => setMode(mode === 'biometric' ? 'pin' : 'biometric')}
            className="text-text-secondary hover:text-accent transition-colors flex items-center gap-1.5"
          >
            <KeyRound size={14} />
            <span>{mode === 'biometric' ? 'استخدام الرمز السري (PIN / Decoy)' : 'استخدام البصمة'}</span>
          </button>
        </div>

        {/* Security Decoy Notice */}
        <div className="bg-primary/40 border border-gray-800 rounded-lg p-3 text-[11px] text-gray-500 text-center leading-relaxed">
          <span className="text-accent font-semibold">ميزة وضع الفخ (Decoy Mode):</span> إدخال الرمز <code className="text-accent bg-black px-1 rounded">0000</code> يفتح مساحة تمويه نظيفة خالية من الرسائل الحقيقية.
        </div>
      </div>
    </div>
  );
}
