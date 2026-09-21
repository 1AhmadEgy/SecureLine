import { useState } from 'react';
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

export default function App() {
  const [currentView, setCurrentView] = useState<ViewMode>('chat');
  const [isUnlocked, setIsUnlocked] = useState(false);
  const [isDecoySession, setIsDecoySession] = useState(false);

  const handleUnlock = (decoy?: boolean) => {
    setIsDecoySession(!!decoy);
    setIsUnlocked(true);
  };

  const handleLockApp = () => {
    setIsUnlocked(false);
    setIsDecoySession(false);
  };

  if (!isUnlocked) {
    return <AppLock onUnlock={handleUnlock} />;
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
