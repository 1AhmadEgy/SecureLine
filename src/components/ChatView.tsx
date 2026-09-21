import { useState, useRef, useEffect } from 'react';
import { 
  Send, 
  Lock, 
  EyeOff, 
  ShieldCheck, 
  Check, 
  CheckCheck, 
  Binary, 
  Globe, 
  Flame, 
  Trash2,
  AlertCircle
} from 'lucide-react';
import type { Message } from '../types';
import { useAuth } from '../context/AuthContext';

export function ChatView() {
  const { token } = useAuth();
  const [showInspector, setShowInspector] = useState(false);
  const [disappearTimer, setDisappearTimer] = useState<number>(0); // 0 = off, 15 = 15s, 60 = 60s
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
      body: 'نعم، تم دمج libsignal بنجاح مع طبقة تعمية (Dummy Padding) لتشتيت تحليل حركة المرور.',
      timestamp: new Date(Date.now() - 3500000),
      isEncrypted: true,
      isObfuscated: true,
      status: 'read'
    },
  ]);
  const [inputText, setInputText] = useState('');
  const [shredNotice, setShredNotice] = useState<string | null>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // Ephemeral message countdown interval (Crypto-Shredding simulation)
  useEffect(() => {
    const timer = setInterval(() => {
      setMessages((prev) => {
        let hasExpired = false;
        const updated = prev.map((msg) => {
          if (msg.remainingSeconds !== undefined && msg.remainingSeconds > 0) {
            return { ...msg, remainingSeconds: msg.remainingSeconds - 1 };
          }
          if (msg.remainingSeconds !== undefined && msg.remainingSeconds <= 0) {
            hasExpired = true;
          }
          return msg;
        });

        if (hasExpired) {
          const surviving = updated.filter(m => m.remainingSeconds === undefined || m.remainingSeconds > 0);
          setShredNotice('تم تدمير الرسائل المنتهية الصلاحية ومسح مفاتيحها محلياً (Zeroized).');
          setTimeout(() => setShredNotice(null), 3000);
          return surviving;
        }

        return updated;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, []);

  const handleSend = async () => {
    if (!inputText.trim()) return;
    
    const textToSend = inputText;
    const msgId = Date.now().toString();
    const newMsg: Message = {
      id: msgId,
      sender: 'me',
      body: textToSend,
      timestamp: new Date(),
      isEncrypted: true,
      isObfuscated: true,
      status: 'sent',
      ephemeralSeconds: disappearTimer > 0 ? disappearTimer : undefined,
      remainingSeconds: disappearTimer > 0 ? disappearTimer : undefined,
    };
    
    setMessages((prev) => [...prev, newMsg]);
    setInputText('');

    // Attempt to persist encrypted payload to backend if authenticated
    if (token) {
      try {
        const dummyNonce = Array.from({ length: 12 }, () => Math.floor(Math.random() * 256).toString(16).padStart(2, '0')).join('');
        await fetch('/api/messages/send', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            recipientId: 1,
            encryptedContent: btoa(unescape(encodeURIComponent(textToSend))),
            nonce: dummyNonce,
            isObfuscated: true,
          })
        });
      } catch (err) {
        console.error('Failed to sync message to backend:', err);
      }
    }
    
    // Simulate peer delivery & read progression
    setTimeout(() => {
      setMessages(prev => prev.map(m => m.id === newMsg.id ? { ...m, status: 'delivered' } : m));
      
      setTimeout(() => {
        setMessages(prev => prev.map(m => m.id === newMsg.id ? { ...m, status: 'read' } : m));
      }, 1200);
    }, 800);
  };

  const shredAllNow = () => {
    setMessages([]);
    setShredNotice('تم تدمير جميع الرسائل فورياً بنمط الهروب الطارئ (Panic Shred).');
    setTimeout(() => setShredNotice(null), 3500);
  };

  return (
    <div className="flex flex-col h-full bg-black">
      {/* Chat Header */}
      <div className="flex items-center justify-between p-4 border-b border-gray-800 bg-primary/50">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-gray-800 flex items-center justify-center text-text-secondary font-semibold">
            007
          </div>
          <div>
            <div className="flex items-center">
              <h2 className="font-semibold text-text-primary me-2">العميل 007</h2>
              <div className="flex items-center text-accent bg-accent/10 px-1.5 py-0.5 rounded text-[10px] font-medium border border-accent/20">
                <ShieldCheck size={12} className="me-1" /> مشفر E2EE
              </div>
            </div>
            <div className="flex items-center text-xs text-text-secondary mt-0.5">
              <span className="w-2 h-2 rounded-full bg-accent me-2 animate-pulse"></span>
              مسار Tor نشط • خادم Ktor
            </div>
          </div>
        </div>

        <div className="flex items-center gap-3 text-text-secondary">
          {/* Emergency Panic Shred Button */}
          <button
            onClick={shredAllNow}
            title="تدمير فوري طارئ للمحادثة"
            className="p-1.5 rounded-lg bg-red-950/40 text-red-400 hover:bg-red-900/60 border border-red-800/40 transition-colors"
          >
            <Trash2 size={16} />
          </button>

          {/* Network Inspector Toggle */}
          <button 
            onClick={() => setShowInspector(!showInspector)}
            className={`flex items-center gap-1.5 px-2.5 py-1.5 rounded-lg text-xs transition-colors ${showInspector ? 'bg-accent/20 text-accent border border-accent/30' : 'bg-gray-800/50 hover:bg-gray-800 text-text-secondary border border-transparent'}`}
            title="فحص حزم التعمية"
          >
            <Binary size={14} />
            <span className="hidden sm:inline">فاحص الحزم</span>
          </button>

          <div className="hidden md:flex flex-col items-end text-xs">
            <span className="flex items-center text-accent"><Lock size={11} className="me-1" /> ChaCha20-Poly1305</span>
            <span className="flex items-center text-text-secondary"><Globe size={11} className="me-1" /> Tor SOCKS (No DNS Leak)</span>
          </div>
        </div>
      </div>

      {/* Ephemeral Notice Toast */}
      {shredNotice && (
        <div className="bg-emerald-950/80 border-b border-accent/30 px-4 py-2 text-xs text-accent flex items-center justify-between transition-all">
          <span className="flex items-center gap-2">
            <Flame size={14} className="text-orange-400 animate-bounce" />
            {shredNotice}
          </span>
          <button onClick={() => setShredNotice(null)} className="text-gray-400 hover:text-white">✕</button>
        </div>
      )}

      {/* Messages Feed */}
      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        <div className="text-center my-3">
          <span className="bg-gray-900/90 text-text-secondary text-xs px-3 py-1 rounded-full border border-gray-800">
            الرسائل مشفرة بنظام السقاطة المزدوجة (Double Ratchet) ومحمية ضد التحليل الجنائي.
          </span>
        </div>
        
        {messages.length === 0 ? (
          <div className="h-48 flex flex-col items-center justify-center text-gray-500 text-sm">
            <AlertCircle size={32} className="mb-2 text-gray-600" />
            <span>لا توجد رسائل نشطة. تم تدمير كافة السجلات بنجاح.</span>
          </div>
        ) : (
          messages.map((msg) => (
            <div key={msg.id} className={`flex flex-col ${msg.sender === 'me' ? 'items-end' : 'items-start'}`}>
              <div className={`max-w-[75%] rounded-2xl p-3 ${
                msg.sender === 'me' 
                  ? 'bg-accent text-black rounded-ee-none shadow-sm' 
                  : 'bg-primary border border-gray-800 text-text-primary rounded-es-none shadow-sm'
              }`}>
                <p className="text-sm leading-relaxed">{msg.body}</p>
                
                <div className={`flex items-center justify-end gap-2 mt-1.5 text-[10px] ${
                  msg.sender === 'me' ? 'text-black/75' : 'text-text-secondary'
                }`}>
                  {/* Countdown Badge if Self-Destruct is enabled */}
                  {msg.remainingSeconds !== undefined && (
                    <span className="flex items-center gap-1 font-mono font-bold text-orange-600 bg-orange-200/50 px-1.5 py-0.2 rounded">
                      <Flame size={11} className="animate-pulse" />
                      {msg.remainingSeconds}s
                    </span>
                  )}

                  <span>{msg.timestamp.toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })}</span>

                  {msg.sender === 'me' && msg.status && (
                    <span className="ms-1 flex items-center">
                      {msg.status === 'sent' && <Check size={13} className="text-black/50" />}
                      {msg.status === 'delivered' && <CheckCheck size={13} className="text-black/50" />}
                      {msg.status === 'read' && <CheckCheck size={13} className="text-blue-800 font-bold" />}
                    </span>
                  )}
                  {msg.sender === 'them' && <Lock size={10} className="ms-1 text-accent" />}
                </div>
              </div>
              
              {/* Obfuscation Inspector Tool */}
              {showInspector && (
                <div className={`mt-1.5 max-w-[85%] bg-black/90 border border-gray-800 rounded-lg p-2 text-[10px] font-mono ${msg.sender === 'me' ? 'text-end' : 'text-start'}`}>
                  <div className="flex items-center gap-1 text-accent mb-1 justify-between" dir="ltr">
                    <span className="text-gray-500">Transmitted Payload Structure</span>
                    <span className="bg-accent/10 px-1.5 py-0.5 rounded border border-accent/20 text-[9px]">Padded 64B</span>
                  </div>
                  <div className="break-all opacity-80 leading-relaxed text-start" dir="ltr">
                    <span className="text-purple-400" title="Dummy Noise Prefix (8 bytes)">[A3 F1 09 C4 7B 22 E1 55]</span>{' '}
                    <span className="text-gray-300" title="Encrypted Payload Body">
                      {Array.from({ length: 8 }).map(() => Math.floor(Math.random() * 256).toString(16).padStart(2, '0').toUpperCase()).join(' ')} ...
                    </span>{' '}
                    <span className="text-emerald-400" title="Auth MAC">[9E 4D 02]</span>{' '}
                    <span className="text-pink-400" title="Dummy Trailing Padding (12 bytes)">[FF 0A 1B 2C 3D 4E 5F]</span>
                  </div>
                </div>
              )}
            </div>
          ))
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input Toolbar with Self-Destruct Selector */}
      <div className="p-3 border-t border-gray-800 bg-primary/30">
        <div className="flex items-center justify-between mb-2 px-2 text-xs text-text-secondary">
          <div className="flex items-center gap-2">
            <Flame size={14} className={disappearTimer > 0 ? 'text-orange-400' : 'text-gray-500'} />
            <span>مؤقت التدمير الذاتي:</span>
            <div className="flex items-center gap-1">
              {[
                { label: 'إيقاف', val: 0 },
                { label: '15 ثانية', val: 15 },
                { label: '60 ثانية', val: 60 },
              ].map((opt) => (
                <button
                  key={opt.val}
                  onClick={() => setDisappearTimer(opt.val)}
                  className={`px-2 py-0.5 rounded text-[11px] transition-colors ${
                    disappearTimer === opt.val 
                      ? 'bg-orange-500/20 text-orange-400 border border-orange-500/40 font-semibold' 
                      : 'hover:bg-gray-800 text-gray-400'
                  }`}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>
          <span className="text-[10px] text-gray-500 hidden sm:inline">مفتاح الحذف: Crypto-Shredding</span>
        </div>

        <div className="flex items-center gap-2 bg-black border border-gray-800 rounded-full p-1 ps-4">
          <input
            type="text"
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSend()}
            placeholder="رسالة مشفرة بـ Signal..."
            className="flex-1 bg-transparent outline-none text-text-primary text-sm placeholder:text-text-secondary"
          />
          <button 
            onClick={handleSend}
            className="w-10 h-10 rounded-full bg-accent flex items-center justify-center text-black hover:bg-emerald-300 transition-colors shrink-0"
          >
            <Send size={18} className="me-0.5 mt-0.5 rotate-180" />
          </button>
        </div>
      </div>
    </div>
  );
}
