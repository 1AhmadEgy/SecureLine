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

export default function App() {
  const [currentView, setCurrentView] = useState<ViewMode>('chat');
  const [isUnlocked, setIsUnlocked] = useState(false);

  if (!isUnlocked) {
    return <AppLock onUnlock={() => setIsUnlocked(true)} />;
  }

  return (
    <div className="flex h-screen bg-black text-text-primary overflow-hidden font-sans selection:bg-accent selection:text-black">
      <Sidebar currentView={currentView} onViewChange={setCurrentView} />
      
      <main className="flex-1 relative">
        {currentView === 'chat' && <ChatView />}
        {currentView === 'calls' && <CallsView />}
        {currentView === 'settings' && <SettingsView />}
        {currentView === 'architecture' && <ArchitectureView />}
        {currentView === 'vision' && <VisionView />}
        {currentView === 'runbook' && <RunbookView />}
      </main>
    </div>
  );
}
