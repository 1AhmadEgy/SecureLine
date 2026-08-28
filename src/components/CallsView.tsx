import { Phone, Video, MicOff, PhoneOff, Settings2, ShieldAlert } from 'lucide-react';

export function CallsView() {
  return (
    <div className="flex flex-col h-full bg-black">
      <div className="p-6 border-b border-gray-800">
        <h2 className="text-2xl font-bold">Secure Call (WebRTC)</h2>
        <p className="text-text-secondary text-sm mt-1">Peer-to-Peer connection established with AEC3 enabled.</p>
      </div>

      <div className="flex-1 p-6 flex flex-col items-center justify-center">
        <div className="relative w-full max-w-2xl aspect-video bg-primary rounded-2xl border border-gray-800 overflow-hidden flex flex-col items-center justify-center">
          
          <div className="absolute top-4 left-4 bg-black/60 px-3 py-1.5 rounded-full backdrop-blur-sm border border-gray-700 flex items-center space-x-2">
            <div className="w-2 h-2 bg-accent rounded-full animate-pulse"></div>
            <span className="text-xs font-mono text-accent">04:23</span>
          </div>
          
          <div className="absolute top-4 right-4 bg-black/60 px-3 py-1.5 rounded-full backdrop-blur-sm border border-gray-700 flex flex-col space-y-1 text-xs">
            <span className="text-text-secondary flex justify-between w-32">Codec: <span className="text-text-primary">Opus</span></span>
            <span className="text-text-secondary flex justify-between w-32">Network: <span className="text-text-primary">UDP (P2P)</span></span>
            <span className="text-text-secondary flex justify-between w-32">Echo Cancel: <span className="text-accent">Active</span></span>
          </div>

          <div className="w-24 h-24 rounded-full bg-gray-800 flex items-center justify-center text-3xl mb-4 shadow-2xl">
            A
          </div>
          <h3 className="text-xl font-medium">Agent 007</h3>
          <p className="text-sm text-text-secondary mt-1 flex items-center">
            <ShieldAlert size={14} className="mr-1 text-accent" />
            Verified Identity
          </p>
          
          {/* Picture in Picture */}
          <div className="absolute bottom-6 right-6 w-32 aspect-video bg-gray-900 rounded-lg border-2 border-gray-700 flex items-center justify-center">
            <span className="text-xs text-text-secondary">You</span>
          </div>
        </div>

        <div className="flex items-center justify-center space-x-6 mt-8">
          <button className="w-14 h-14 rounded-full bg-gray-800 flex items-center justify-center text-text-primary hover:bg-gray-700 transition-colors">
            <MicOff size={24} />
          </button>
          <button className="w-14 h-14 rounded-full bg-gray-800 flex items-center justify-center text-text-primary hover:bg-gray-700 transition-colors">
            <Video size={24} />
          </button>
          <button className="w-14 h-14 rounded-full bg-gray-800 flex items-center justify-center text-text-primary hover:bg-gray-700 transition-colors">
            <Settings2 size={24} />
          </button>
          <button className="w-16 h-16 rounded-full bg-red-500 flex items-center justify-center text-white hover:bg-red-600 transition-colors shadow-lg shadow-red-500/20">
            <PhoneOff size={28} />
          </button>
        </div>
      </div>
    </div>
  );
}
