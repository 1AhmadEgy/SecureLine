import { ShieldCheck, EyeOff, Key, Server, Database, Globe, Timer, Monitor, Smartphone, Laptop, QrCode } from 'lucide-react';

export function SettingsView() {
  const settingsGroups = [
    {
      title: 'Obfuscation Layer',
      icon: <EyeOff size={18} className="text-accent" />,
      settings: [
        { name: 'Dummy Padding', description: 'Add 16-byte random data to hide message lengths', active: true },
        { name: 'Byte Reversal', description: 'Reverse byte order before transmission', active: true },
      ]
    },
    {
      title: 'Network Privacy',
      icon: <Globe size={18} className="text-accent" />,
      settings: [
        { name: 'Tor Network Routing', description: 'Route all traffic through local SOCKS proxy (127.0.0.1:9050)', active: true },
        { name: 'Signal Server Relay', description: 'Use Signal-Server only for signaling, no payload access', active: true },
      ]
    },
    {
      title: 'Local Storage',
      icon: <Database size={18} className="text-accent" />,
      settings: [
        { name: 'SQLCipher', description: 'AES-256 encrypted SQLite database', active: true },
        { name: 'Hardware Keystore', description: 'Store keys in Secure Enclave / TEE', active: true },
      ]
    },
    {
      title: 'Message Privacy',
      icon: <Timer size={18} className="text-accent" />,
      settings: [
        { 
          name: 'Disappearing Messages', 
          description: 'Automatically remove messages after a set duration', 
          active: true,
          type: 'toggle-with-select',
          options: ['1 Hour', '24 Hours', '1 Week'],
          currentValue: '24 Hours'
        },
      ]
    }
  ];

  return (
    <div className="flex flex-col h-full bg-black overflow-y-auto">
      <div className="p-6 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10">
        <h2 className="text-2xl font-bold">Security Settings</h2>
        <p className="text-text-secondary text-sm mt-1">Configure advanced privacy and encryption protocols.</p>
      </div>

      <div className="p-6 max-w-3xl space-y-8">
        
        <div className="bg-primary/50 border border-gray-800 rounded-xl p-5 flex items-start space-x-4">
          <div className="p-3 bg-accent/10 rounded-full">
            <ShieldCheck size={24} className="text-accent" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-text-primary">Double Ratchet Protocol</h3>
            <p className="text-sm text-text-secondary mt-1">
              Currently utilizing libsignal for X25519, AES-256, and HMAC-SHA256 end-to-end encryption.
              Keys are rotated securely after every message.
            </p>
          </div>
        </div>

        <div className="space-y-6">
          {settingsGroups.map((group, idx) => (
            <div key={idx} className="space-y-3">
              <h3 className="text-sm font-medium text-text-secondary uppercase tracking-wider flex items-center space-x-2">
                {group.icon}
                <span>{group.title}</span>
              </h3>
              <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden divide-y divide-gray-800">
                {group.settings.map((setting, sIdx) => (
                  <div key={sIdx} className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                    <div>
                      <h4 className="font-medium text-text-primary">{setting.name}</h4>
                      <p className="text-xs text-text-secondary mt-1">{setting.description}</p>
                    </div>
                    {((setting as any).type === 'toggle-with-select') ? (
                      <div className="flex items-center space-x-3">
                        <select 
                          className="bg-black border border-gray-700 text-text-primary text-sm rounded-lg focus:ring-accent focus:border-accent block p-2 outline-none cursor-pointer"
                          defaultValue={(setting as any).currentValue}
                        >
                          {(setting as any).options.map((opt: string) => (
                            <option key={opt} value={opt}>{opt}</option>
                          ))}
                        </select>
                        <div className="relative inline-flex items-center cursor-pointer">
                          <div className={`w-11 h-6 rounded-full peer relative outline-none transition-colors ${setting.active ? 'bg-accent' : 'bg-gray-700'}`}>
                            <div className={`absolute top-[2px] left-[2px] bg-black rounded-full h-5 w-5 transition-transform ${setting.active ? 'translate-x-full' : ''}`}></div>
                          </div>
                        </div>
                      </div>
                    ) : (
                      <div className="relative inline-flex items-center cursor-pointer">
                        <div className={`w-11 h-6 rounded-full peer relative outline-none transition-colors ${setting.active ? 'bg-accent' : 'bg-gray-700'}`}>
                          <div className={`absolute top-[2px] left-[2px] bg-black rounded-full h-5 w-5 transition-transform ${setting.active ? 'translate-x-full' : ''}`}></div>
                        </div>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>

        <div className="space-y-3 pt-6 border-t border-gray-800">
          <h3 className="text-sm font-medium text-text-secondary uppercase tracking-wider flex items-center space-x-2">
            <Monitor size={18} className="text-accent" />
            <span>Linked Devices</span>
          </h3>
          <div className="bg-primary border border-gray-800 rounded-xl overflow-hidden divide-y divide-gray-800">
             
             <div className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-gray-800 rounded-lg">
                    <Smartphone className="text-text-primary" size={20} />
                  </div>
                  <div>
                    <h4 className="font-medium text-text-primary">iPhone 14 Pro (This Device)</h4>
                    <p className="text-xs text-accent mt-0.5">Active now • Location hidden</p>
                  </div>
                </div>
             </div>
             
             <div className="p-4 flex items-center justify-between hover:bg-gray-800/30 transition-colors">
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-gray-800 rounded-lg">
                    <Laptop className="text-text-primary" size={20} />
                  </div>
                  <div>
                    <h4 className="font-medium text-text-primary">SecureLine Desktop (macOS)</h4>
                    <p className="text-xs text-text-secondary mt-0.5">Last active: 2 hours ago</p>
                  </div>
                </div>
                <button className="text-xs font-medium text-red-400 hover:text-red-300 transition-colors px-3 py-1.5 rounded-lg border border-red-400/20 hover:bg-red-400/10">Unlink</button>
             </div>
             
             <button className="w-full p-4 bg-gray-900/40 hover:bg-gray-800/60 transition-colors flex items-center justify-center space-x-2 text-accent group">
                <QrCode size={18} className="group-hover:scale-110 transition-transform" />
                <span className="font-medium text-sm">Scan QR Code to Link New Device</span>
             </button>
          </div>
        </div>

      </div>
    </div>
  );
}
