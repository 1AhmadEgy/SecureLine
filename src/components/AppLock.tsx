import { Fingerprint, Lock } from 'lucide-react';
import { useState } from 'react';

interface AppLockProps {
  onUnlock: () => void;
}

export function AppLock({ onUnlock }: AppLockProps) {
  const [authenticating, setAuthenticating] = useState(false);

  const handleUnlock = () => {
    setAuthenticating(true);
    setTimeout(() => {
      onUnlock();
    }, 1200);
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-black text-text-primary selection:bg-accent selection:text-black">
      <div className="flex flex-col items-center space-y-8">
        <div className="w-16 h-16 rounded-full bg-accent/10 flex items-center justify-center mb-4 border border-accent/20">
          <Lock className="text-accent" size={32} />
        </div>
        
        <div className="text-center space-y-2">
          <h1 className="text-2xl font-bold tracking-tight">SecureLine مقفل</h1>
          <p className="text-text-secondary text-sm">قم بتأكيد هويتك للمتابعة</p>
        </div>

        <button 
          onClick={handleUnlock}
          disabled={authenticating}
          className={`relative flex flex-col items-center justify-center w-32 h-32 rounded-full border-2 transition-all duration-300 ${
            authenticating 
              ? 'border-accent bg-accent/5 scale-95 shadow-[0_0_30px_rgba(0,255,170,0.3)]' 
              : 'border-gray-800 hover:border-gray-600 bg-gray-900/50'
          }`}
        >
          <Fingerprint 
            size={48} 
            className={`transition-colors duration-300 ${authenticating ? 'text-accent animate-pulse' : 'text-text-secondary'}`} 
          />
          <span className={`absolute -bottom-8 text-xs font-medium transition-colors ${authenticating ? 'text-accent' : 'text-text-secondary'}`}>
            {authenticating ? 'جاري التحقق...' : 'اضغط لفك القفل'}
          </span>
        </button>
      </div>
    </div>
  );
}
