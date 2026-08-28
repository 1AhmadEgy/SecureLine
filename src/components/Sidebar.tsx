import { Shield, MessageSquare, Phone, Settings, Server, Lock } from 'lucide-react';
import type { ViewMode } from '../types';

interface SidebarProps {
  currentView: ViewMode;
  onViewChange: (view: ViewMode) => void;
}

export function Sidebar({ currentView, onViewChange }: SidebarProps) {
  const navItems: { id: ViewMode; label: string; icon: React.ReactNode }[] = [
    { id: 'chat', label: 'Chat', icon: <MessageSquare size={20} /> },
    { id: 'calls', label: 'Calls (WebRTC)', icon: <Phone size={20} /> },
    { id: 'settings', label: 'Security', icon: <Settings size={20} /> },
    { id: 'architecture', label: 'Architecture', icon: <Server size={20} /> },
  ];

  return (
    <div className="w-64 bg-primary border-r border-gray-800 flex flex-col h-full">
      <div className="p-6 flex items-center space-x-3">
        <div className="w-10 h-10 rounded-full bg-accent/10 flex items-center justify-center">
          <Shield className="text-accent" size={24} />
        </div>
        <div>
          <h1 className="text-xl font-bold tracking-tight">SecureLine</h1>
          <div className="flex items-center text-xs text-accent mt-1">
            <Lock size={10} className="mr-1" /> End-to-End Encrypted
          </div>
        </div>
      </div>

      <nav className="flex-1 px-4 py-4 space-y-2">
        {navItems.map((item) => (
          <button
            key={item.id}
            onClick={() => onViewChange(item.id)}
            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors duration-200 ${
              currentView === item.id
                ? 'bg-accent/10 text-accent font-medium'
                : 'text-text-secondary hover:bg-gray-800/50 hover:text-text-primary'
            }`}
          >
            {item.icon}
            <span>{item.label}</span>
          </button>
        ))}
      </nav>
      
      <div className="p-4 m-4 bg-black rounded-lg border border-gray-800">
        <div className="flex items-center justify-between text-xs text-text-secondary mb-2">
          <span>Signal Protocol</span>
          <span className="text-accent">Active</span>
        </div>
        <div className="flex items-center justify-between text-xs text-text-secondary mb-2">
          <span>Tor Network</span>
          <span className="text-accent">Connected</span>
        </div>
        <div className="flex items-center justify-between text-xs text-text-secondary">
          <span>SQLCipher</span>
          <span className="text-accent">Secured</span>
        </div>
      </div>
    </div>
  );
}
