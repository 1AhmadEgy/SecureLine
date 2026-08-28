import { useState, useRef, useEffect } from 'react';
import { Send, Lock, EyeOff, ShieldCheck, Check, CheckCheck, Binary, Globe } from 'lucide-react';
import type { Message } from '../types';

export function ChatView() {
  const [showInspector, setShowInspector] = useState(false);
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      sender: 'them',
      body: 'هل نستخدم التنفيذ الجديد لبروتوكول السقاطة المزدوجة؟',
      timestamp: new Date(Date.now() - 3600000),
      isEncrypted: true,
      isObfuscated: true,
      status: 'read'
    },
    {
      id: '2',
      sender: 'me',
      body: 'نعم، تم دمج libsignal بنجاح. بالإضافة إلى طبقة بايتات وهمية (Dummy Padding) للتعمية.',
      timestamp: new Date(Date.now() - 3500000),
      isEncrypted: true,
      isObfuscated: true,
      status: 'read'
    },
  ]);
  const [inputText, setInputText] = useState('');
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSend = () => {
    if (!inputText.trim()) return;
    
    const newMsg: Message = {
      id: Date.now().toString(),
      sender: 'me',
      body: inputText,
      timestamp: new Date(),
      isEncrypted: true,
      isObfuscated: true,
      status: 'sent',
    };
    
    setMessages((prev) => [...prev, newMsg]);
    setInputText('');
    
    // Simulate delivery and read status
    setTimeout(() => {
      setMessages(prev => prev.map(m => m.id === newMsg.id ? { ...m, status: 'delivered' } : m));
      
      setTimeout(() => {
        setMessages(prev => prev.map(m => m.id === newMsg.id ? { ...m, status: 'read' } : m));
      }, 1500);
    }, 1000);
  };

  return (
    <div className="flex flex-col h-full bg-black">
      <div className="flex items-center justify-between p-4 border-b border-gray-800 bg-primary/50">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-gray-800 flex items-center justify-center text-text-secondary">
            A
          </div>
          <div>
            <div className="flex items-center">
              <h2 className="font-semibold text-text-primary me-2">العميل 007</h2>
              <div className="flex items-center text-accent bg-accent/10 px-1.5 py-0.5 rounded text-[10px] font-medium border border-accent/20">
                <ShieldCheck size={12} className="me-1" /> مشفر
              </div>
            </div>
            <div className="flex items-center text-xs text-text-secondary mt-0.5">
              <span className="w-2 h-2 rounded-full bg-accent me-2"></span>
              متصل عبر Tor
            </div>
          </div>
        </div>
        <div className="flex items-center gap-4 text-text-secondary">
          <button 
            onClick={() => setShowInspector(!showInspector)}
            className={`flex items-center gap-1.5 px-2.5 py-1.5 rounded-lg text-xs transition-colors ${showInspector ? 'bg-accent/20 text-accent border border-accent/30' : 'bg-gray-800/50 hover:bg-gray-800 text-text-secondary border border-transparent'}`}
            title="فحص الشبكة والتعمية"
          >
            <Binary size={14} />
            <span className="hidden sm:inline">مفتش الشبكة</span>
          </button>
          <div className="flex flex-col items-end text-xs">
            <span className="flex items-center text-accent"><Lock size={12} className="me-1" /> AES-256-GCM</span>
            <span className="flex items-center"><Globe size={12} className="me-1" /> مسار Tor</span>
            <span className="flex items-center"><EyeOff size={12} className="me-1" /> تعمية نشطة</span>
          </div>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        <div className="text-center my-4">
          <span className="bg-gray-900 text-text-secondary text-xs px-3 py-1 rounded-full border border-gray-800">
            الرسائل مشفرة من طرف إلى طرف. لا يمكن لأي شخص خارج هذه المحادثة قراءتها.
          </span>
        </div>
        
        {messages.map((msg) => (
          <div key={msg.id} className={`flex flex-col ${msg.sender === 'me' ? 'items-end' : 'items-start'}`}>
            <div className={`max-w-[70%] rounded-2xl p-3 ${
              msg.sender === 'me' 
                ? 'bg-accent text-black rounded-ee-none' 
                : 'bg-primary border border-gray-800 text-text-primary rounded-es-none'
            }`}>
              <p className="text-sm">{msg.body}</p>
              <div className={`flex items-center justify-end mt-1 text-[10px] ${
                msg.sender === 'me' ? 'text-black/70' : 'text-text-secondary'
              }`}>
                <span>{msg.timestamp.toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })}</span>
                {msg.sender === 'me' && msg.status && (
                  <span className="ms-1 flex items-center">
                    {msg.status === 'sent' && <Check size={14} className="text-black/50" />}
                    {msg.status === 'delivered' && <CheckCheck size={14} className="text-black/50" />}
                    {msg.status === 'read' && <CheckCheck size={14} className="text-blue-700" />}
                  </span>
                )}
                {msg.sender === 'them' && <Lock size={10} className="ms-1" />}
              </div>
            </div>
            
            {/* Obfuscation Inspector Tool */}
            {showInspector && (
              <div className={`mt-1 max-w-[80%] bg-black border border-gray-800 rounded-lg p-2 text-[10px] font-mono ${msg.sender === 'me' ? 'text-end' : 'text-start'}`}>
                <div className="flex items-center gap-1 text-accent mb-1 justify-between" dir="ltr">
                  <span className="text-gray-500">Transmitted Payload</span>
                  <span className="bg-accent/10 px-1 rounded border border-accent/20">Obfuscated</span>
                </div>
                <div className="break-all opacity-80 leading-relaxed" dir="ltr">
                  <span className="text-purple-400" title="Dummy Prefix (8 bytes)">[A3 F1 09 C4 7B 22 E1 55]</span>{' '}
                  <span className="text-gray-400" title="Reversed Encrypted Payload">{
                    Array.from({length: Math.min(msg.body.length, 12)}).map(() => Math.floor(Math.random()*256).toString(16).padStart(2, '0').toUpperCase()).join(' ')
                  }...</span>{' '}
                  <span className="text-pink-400" title="Dummy Suffix (12 bytes)">[FF 0A 1B 2C 3D 4E 5F 60 71 82 93 A4]</span>
                </div>
              </div>
            )}
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      <div className="p-4 border-t border-gray-800 bg-primary/30">
        <div className="flex items-center gap-2 bg-black border border-gray-800 rounded-full p-1 ps-4">
          <input
            type="text"
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSend()}
            placeholder="رسالة آمنة..."
            className="flex-1 bg-transparent outline-none text-text-primary text-sm placeholder:text-text-secondary"
          />
          <button 
            onClick={handleSend}
            className="w-10 h-10 rounded-full bg-accent flex items-center justify-center text-black hover:bg-emerald-300 transition-colors"
          >
            <Send size={18} className="me-0.5 mt-0.5 rotate-180" />
          </button>
        </div>
      </div>
    </div>
  );
}
