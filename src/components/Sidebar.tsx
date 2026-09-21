import type { ReactNode } from 'react';
import { 
  Shield, 
  MessageSquare, 
  Phone, 
  Settings, 
  Server, 
  Lock, 
  Target, 
  Terminal, 
  LogIn, 
  LogOut, 
  User as UserIcon,
  ShieldAlert
} from 'lucide-react';
import type { ViewMode } from '../types';
import { useAuth } from '../context/AuthContext';

interface SidebarProps {
  currentView: ViewMode;
  onViewChange: (view: ViewMode) => void;
  onLockApp?: () => void;
  isDecoy?: boolean;
}

export function Sidebar({ currentView, onViewChange, onLockApp, isDecoy }: SidebarProps) {
  const { currentUser, signInWithGoogle, signOut, loading } = useAuth();
  const navItems: { id: ViewMode; label: string; icon: ReactNode }[] = [
    { id: 'chat', label: 'المحادثات المشفرة', icon: <MessageSquare size={20} /> },
    { id: 'calls', label: 'المكالمات (WebRTC)', icon: <Phone size={20} /> },
    { id: 'settings', label: 'الأمان والخصوصية', icon: <Settings size={20} /> },
    { id: 'architecture', label: 'الهيكلية الفنية', icon: <Server size={20} /> },
    { id: 'vision', label: 'رؤية المشروع', icon: <Target size={20} /> },
    { id: 'runbook', label: 'دليل المطور', icon: <Terminal size={20} /> },
  ];

  return (
    <div className="w-64 bg-primary border-e border-gray-800 flex flex-col h-full select-none">
      <div className="p-6 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-accent/10 flex items-center justify-center shrink-0 border border-accent/20">
            <Shield className="text-accent" size={24} />
          </div>
          <div>
            <h1 className="text-xl font-bold tracking-tight">SecureLine</h1>
            <div className="flex items-center text-xs text-accent mt-1">
              <Lock size={10} className="me-1" /> {isDecoy ? 'وضع الفخ' : 'مشفر بالكامل'}
            </div>
          </div>
        </div>

        {onLockApp && (
          <button
            onClick={onLockApp}
            title="قفل التطبيق فورياً"
            className="p-1.5 text-gray-500 hover:text-accent hover:bg-gray-800/80 rounded-lg transition-colors"
          >
            <Lock size={16} />
          </button>
        )}
      </div>

      <nav className="flex-1 px-4 py-3 space-y-1.5 overflow-y-auto">
        {navItems.map((item) => (
          <button
            key={item.id}
            onClick={() => onViewChange(item.id)}
            className={`w-full flex items-center gap-3 px-4 py-2.5 rounded-lg transition-colors duration-150 text-sm ${
              currentView === item.id
                ? 'bg-accent/10 text-accent font-semibold border border-accent/20'
                : 'text-text-secondary hover:bg-gray-800/50 hover:text-text-primary'
            }`}
          >
            {item.icon}
            <span>{item.label}</span>
          </button>
        ))}
      </nav>
      
      {/* Cloud SQL User Sync Card */}
      <div className="px-4 py-2 border-t border-gray-800">
        {currentUser ? (
          <div className="flex items-center justify-between p-2 rounded-lg bg-gray-900/80 border border-gray-800">
            <div className="flex items-center gap-2 overflow-hidden">
              {currentUser.photoURL ? (
                <img
                  src={currentUser.photoURL}
                  alt={currentUser.displayName || 'User'}
                  className="w-7 h-7 rounded-full border border-gray-700"
                  referrerPolicy="no-referrer"
                />
              ) : (
                <div className="w-7 h-7 rounded-full bg-accent/20 flex items-center justify-center text-accent">
                  <UserIcon size={14} />
                </div>
              )}
              <div className="truncate text-xs">
                <p className="font-medium text-text-primary truncate">
                  {currentUser.displayName || currentUser.email?.split('@')[0]}
                </p>
                <p className="text-[10px] text-accent truncate">مربوط بقاعدة البيانات</p>
              </div>
            </div>
            <button
              onClick={() => signOut()}
              title="تسجيل الخروج"
              className="p-1.5 text-text-secondary hover:text-red-400 hover:bg-gray-800 rounded transition-colors"
            >
              <LogOut size={14} />
            </button>
          </div>
        ) : (
          <button
            onClick={() => signInWithGoogle()}
            disabled={loading}
            className="w-full flex items-center justify-center gap-2 px-3 py-2 text-xs font-medium bg-accent/10 hover:bg-accent/20 text-accent border border-accent/20 rounded-lg transition-colors"
          >
            <LogIn size={14} />
            <span>تسجيل الدخول ومزامنة الهوية</span>
          </button>
        )}
      </div>

      {/* Network & Cipher Status Bar */}
      <div className="p-3.5 m-3 bg-black rounded-xl border border-gray-800 space-y-1.5 text-xs">
        <div className="flex items-center justify-between text-text-secondary">
          <span>بروتوكول Signal (E2EE)</span>
          <span className="text-accent font-mono text-[11px]">نشط</span>
        </div>
        <div className="flex items-center justify-between text-text-secondary">
          <span>توجيه Tor SOCKS5</span>
          <span className="text-accent font-mono text-[11px]">متصل (3 قفزات)</span>
        </div>
        <div className="flex items-center justify-between text-text-secondary">
          <span>قاعدة بيانات SQLCipher</span>
          <span className="text-accent font-mono text-[11px]">مؤمنة AES-256</span>
        </div>
      </div>
    </div>
  );
}
