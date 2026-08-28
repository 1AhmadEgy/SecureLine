import { Shield, MessageSquare, Phone, Settings, Server, Lock, Target, Terminal } from 'lucide-react';
import type { ViewMode } from '../types';

interface SidebarProps {
  currentView: ViewMode;
  onViewChange: (view: ViewMode) => void;
}

export function Sidebar({ currentView, onViewChange }: SidebarProps) {
  const navItems: { id: ViewMode; label: string; icon: React.ReactNode }[] = [
    { id: 'chat', label: 'المحادثات', icon: <MessageSquare size={20} /> },
    { id: 'calls', label: 'المكالمات (WebRTC)', icon: <Phone size={20} /> },
    { id: 'settings', label: 'الأمان والخصوصية', icon: <Settings size={20} /> },
    { id: 'architecture', label: 'الهيكلية', icon: <Server size={20} /> },
    { id: 'vision', label: 'رؤية المشروع', icon: <Target size={20} /> },
    { id: 'runbook', label: 'دليل المطور', icon: <Terminal size={20} /> },
  ];

  return (
    <div className="w-64 bg-primary border-e border-gray-800 flex flex-col h-full">
      <div className="p-6 flex items-center gap-3">
        <div className="w-10 h-10 rounded-full bg-accent/10 flex items-center justify-center shrink-0">
          <Shield className="text-accent" size={24} />
        </div>
        <div>
          <h1 className="text-xl font-bold tracking-tight">SecureLine</h1>
          <div className="flex items-center text-xs text-accent mt-1">
            <Lock size={10} className="me-1" /> مشفر من طرف إلى طرف
          </div>
        </div>
      </div>

      <nav className="flex-1 px-4 py-4 space-y-2">
        {navItems.map((item) => (
          <button
            key={item.id}
            onClick={() => onViewChange(item.id)}
            className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg transition-colors duration-200 ${
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
          <span>بروتوكول Signal</span>
          <span className="text-accent">نشط</span>
        </div>
        <div className="flex items-center justify-between text-xs text-text-secondary mb-2">
          <span>شبكة Tor</span>
          <span className="text-accent">متصل</span>
        </div>
        <div className="flex items-center justify-between text-xs text-text-secondary">
          <span>SQLCipher</span>
          <span className="text-accent">مؤمن</span>
        </div>
      </div>
    </div>
  );
}
