import { useState, useEffect } from 'react';
import { Sidebar } from './components/Sidebar';
import { ChatView } from './components/ChatView';
import { CallsView } from './components/CallsView';
import { SettingsView } from './components/SettingsView';
import { ArchitectureView } from './components/ArchitectureView';
import { VisionView } from './components/VisionView';
import { RunbookView } from './components/RunbookView';
import { AppLock } from './components/AppLock';
import type { ViewMode } from './types';
import { ShieldAlert, Lock } from 'lucide-react';

function parseAutoLockMs(policy: string): number {
  if (policy === 'معطل') return -1;
  if (policy.includes('فورياً')) return 0;
  if (policy.includes('30 ثانية')) return 30 * 1000;
  if (policy.includes('1 دقيقة')) return 60 * 1000;
  if (policy.includes('5 دقائق')) return 5 * 60 * 1000;
  return 30 * 1000; // Default 30s
}

export default function App() {
  const [currentView, setCurrentView] = useState<ViewMode>('chat');
  const [isUnlocked, setIsUnlocked] = useState(false);
  const [isDecoySession, setIsDecoySession] = useState(false);
  const [lockReason, setLockReason] = useState<string | null>(null);

  // Auto-lock policy state (configurable via Settings, defaults to 30 seconds on tab away)
  const [autoLockPolicy, setAutoLockPolicy] = useState<string>(() => {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('secureline_autolock_policy') || 'بعد 30 ثانية من مغادرة التبويب (افتراضي)';
    }
    return 'بعد 30 ثانية من مغادرة التبويب (افتراضي)';
  });

  const handleUnlock = (decoy?: boolean) => {
    setIsDecoySession(!!decoy);
    setLockReason(null);
    setIsUnlocked(true);
  };

  const handleLockApp = (reason?: string) => {
    setLockReason(reason || null);
    setIsUnlocked(false);
    setIsDecoySession(false);
  };

  // Sync auto-lock policy changes from SettingsView
  useEffect(() => {
    const handlePolicyChange = (e: Event) => {
      const custom = e as CustomEvent<string>;
      if (custom.detail) {
        setAutoLockPolicy(custom.detail);
      }
    };

    window.addEventListener('secureline_autolock_changed', handlePolicyChange);
    return () => window.removeEventListener('secureline_autolock_changed', handlePolicyChange);
  }, []);

  // Automatic Lock Screen: Tab Blur / Window Switching Away (30 Seconds Threshold)
  useEffect(() => {
    if (!isUnlocked) return;

    const timeoutMs = parseAutoLockMs(autoLockPolicy);
    if (timeoutMs < 0) return; // Disabled

    let timer: NodeJS.Timeout | null = null;
    let awayTimestamp: number | null = null;

    const triggerAutoLock = (reason: string) => {
      if (timer) clearTimeout(timer);
      timer = null;
      awayTimestamp = null;
      setLockReason(reason);
      setIsUnlocked(false);
      setIsDecoySession(false);
    };

    const handleAway = () => {
      // Immediate lock on blur / loss of tab focus
      if (timeoutMs === 0) {
        triggerAutoLock('تم قفل التطبيق فورياً لفقدان تركيز نافذة المتصفح (Tab Lost Focus).');
        return;
      }

      if (!awayTimestamp) {
        awayTimestamp = Date.now();
      }

      if (timer) clearTimeout(timer);
      timer = setTimeout(() => {
        triggerAutoLock('تم قفل التطبيق تلقائياً لمغادرة التبويب لأكثر من 30 ثانية (Auto-Lock on Tab Away).');
      }, timeoutMs);
    };

    const handleReturn = () => {
      if (awayTimestamp) {
        const elapsed = Date.now() - awayTimestamp;
        if (elapsed >= timeoutMs) {
          triggerAutoLock('تم قفل التطبيق تلقائياً بعد مغادرة التبويب لأكثر من 30 ثانية.');
          return;
        }
      }

      if (timer) {
        clearTimeout(timer);
        timer = null;
      }
      awayTimestamp = null;
    };

    const handleVisibilityChange = () => {
      if (document.hidden || document.visibilityState === 'hidden') {
        handleAway();
      } else {
        handleReturn();
      }
    };

    const handleBlur = () => {
      handleAway();
    };

    const handleFocus = () => {
      handleReturn();
    };

    document.addEventListener('visibilitychange', handleVisibilityChange);
    window.addEventListener('blur', handleBlur);
    window.addEventListener('focus', handleFocus);

    return () => {
      if (timer) clearTimeout(timer);
      document.removeEventListener('visibilitychange', handleVisibilityChange);
      window.removeEventListener('blur', handleBlur);
      window.removeEventListener('focus', handleFocus);
    };
  }, [isUnlocked, autoLockPolicy]);

  if (!isUnlocked) {
    return <AppLock onUnlock={handleUnlock} lockReason={lockReason} />;
  }

  return (
    <div className="flex h-screen bg-black text-text-primary overflow-hidden font-sans selection:bg-accent selection:text-black">
      <Sidebar 
        currentView={currentView} 
        onViewChange={setCurrentView} 
        onLockApp={handleLockApp}
        isDecoy={isDecoySession}
      />
      
      <main className="flex-1 relative overflow-hidden flex flex-col">
        {/* Decoy Mode Banner */}
        {isDecoySession && (
          <div className="bg-amber-950/80 border-b border-amber-800/60 px-4 py-1.5 text-xs text-amber-300 flex items-center justify-between shrink-0">
            <span className="flex items-center gap-1.5">
              <ShieldAlert size={14} className="text-amber-400" />
              أنت الآن في وضع الفخ المظلل (Decoy Sandbox). لا تظهر أي محادثات أو مفاتيح حقيقية.
            </span>
            <button 
              onClick={handleLockApp}
              className="text-amber-200 underline hover:text-white text-[11px]"
            >
              قفل التطبيق
            </button>
          </div>
        )}

        <div className="flex-1 relative overflow-hidden">
          {currentView === 'chat' && <ChatView />}
          {currentView === 'calls' && <CallsView />}
          {currentView === 'settings' && <SettingsView />}
          {currentView === 'architecture' && <ArchitectureView />}
          {currentView === 'vision' && <VisionView />}
          {currentView === 'runbook' && <RunbookView />}
        </div>
      </main>
    </div>
  );
}
