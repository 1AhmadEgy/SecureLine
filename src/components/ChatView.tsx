import { useState, useRef, useEffect } from 'react';
import { Send, Lock, EyeOff, ShieldCheck, Check, CheckCheck } from 'lucide-react';
import type { Message } from '../types';

export function ChatView() {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      sender: 'them',
      body: 'Are we using the new Double Ratchet implementation?',
      timestamp: new Date(Date.now() - 3600000),
      isEncrypted: true,
      isObfuscated: true,
      status: 'read'
    },
    {
      id: '2',
      sender: 'me',
      body: 'Yes, libsignal is integrated. Plus dummy padding for obfuscation.',
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
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 rounded-full bg-gray-800 flex items-center justify-center text-text-secondary">
            A
          </div>
          <div>
            <div className="flex items-center">
              <h2 className="font-semibold text-text-primary mr-2">Agent 007</h2>
              <div className="flex items-center text-accent bg-accent/10 px-1.5 py-0.5 rounded text-[10px] font-medium border border-accent/20">
                <ShieldCheck size={12} className="mr-1" /> Encrypted
              </div>
            </div>
            <div className="flex items-center text-xs text-text-secondary mt-0.5">
              <span className="w-2 h-2 rounded-full bg-accent mr-2"></span>
              Online via Tor
            </div>
          </div>
        </div>
        <div className="flex items-center space-x-4 text-text-secondary">
          <div className="flex flex-col items-end text-xs">
            <span className="flex items-center text-accent"><Lock size={12} className="mr-1" /> AES-256-GCM</span>
            <span className="flex items-center"><EyeOff size={12} className="mr-1" /> Dummy Padded</span>
          </div>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        <div className="text-center my-4">
          <span className="bg-gray-900 text-text-secondary text-xs px-3 py-1 rounded-full border border-gray-800">
            Messages are end-to-end encrypted. Nobody outside this chat can read them.
          </span>
        </div>
        
        {messages.map((msg) => (
          <div key={msg.id} className={`flex ${msg.sender === 'me' ? 'justify-end' : 'justify-start'}`}>
            <div className={`max-w-[70%] rounded-2xl p-3 ${
              msg.sender === 'me' 
                ? 'bg-accent text-black rounded-br-none' 
                : 'bg-primary border border-gray-800 text-text-primary rounded-bl-none'
            }`}>
              <p className="text-sm">{msg.body}</p>
              <div className={`flex items-center justify-end mt-1 text-[10px] ${
                msg.sender === 'me' ? 'text-black/70' : 'text-text-secondary'
              }`}>
                <span>{msg.timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                {msg.sender === 'me' && msg.status && (
                  <span className="ml-1 flex items-center">
                    {msg.status === 'sent' && <Check size={14} className="text-black/50" />}
                    {msg.status === 'delivered' && <CheckCheck size={14} className="text-black/50" />}
                    {msg.status === 'read' && <CheckCheck size={14} className="text-blue-700" />}
                  </span>
                )}
                {msg.sender === 'them' && <Lock size={10} className="ml-1" />}
              </div>
            </div>
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      <div className="p-4 border-t border-gray-800 bg-primary/30">
        <div className="flex items-center space-x-2 bg-black border border-gray-800 rounded-full p-1 pl-4">
          <input
            type="text"
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSend()}
            placeholder="Secure message..."
            className="flex-1 bg-transparent outline-none text-text-primary text-sm placeholder:text-text-secondary"
          />
          <button 
            onClick={handleSend}
            className="w-10 h-10 rounded-full bg-accent flex items-center justify-center text-black hover:bg-emerald-300 transition-colors"
          >
            <Send size={18} className="ml-1" />
          </button>
        </div>
      </div>
    </div>
  );
}
