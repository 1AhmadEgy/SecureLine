export function ArchitectureView() {
  return (
    <div className="flex flex-col h-full bg-black overflow-y-auto">
      <div className="p-6 border-b border-gray-800 sticky top-0 bg-black/80 backdrop-blur-md z-10">
        <h2 className="text-2xl font-bold">System Architecture Blueprint</h2>
        <p className="text-text-secondary text-sm mt-1">Implementation plan for SecureLine based on the provided technical specification.</p>
      </div>

      <div className="p-6 max-w-4xl space-y-8">
        
        {/* Architecture Diagram */}
        <section className="space-y-4">
          <h3 className="text-lg font-semibold text-accent border-b border-gray-800 pb-2">1. Core Architecture</h3>
          <div className="bg-primary border border-gray-800 rounded-xl p-6 overflow-x-auto font-mono text-xs text-text-secondary whitespace-pre">
{`graph TD
    A[User Device 1] <-->|WebRTC - Encrypted P2P| B[User Device 2]
    A <-->|WebSocket TLS 1.3| C[Signal Server]
    B <-->|WebSocket TLS 1.3| C
    C <-->|Cannot Decrypt| D[Server DB]
    
    subgraph "On-Device Encryption Layer"
        E[libsignal - Double Ratchet]
        F[AES-256-GCM / ChaCha20]
        G[SQLCipher (Message Storage)]
    end`}
          </div>
        </section>

        {/* Core Dependencies */}
        <section className="space-y-4">
          <h3 className="text-lg font-semibold text-accent border-b border-gray-800 pb-2">2. Open Source Dependencies</h3>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2">Encryption Protocol</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">libsignal:</span> End-to-end encryption core</li>
                <li><span className="text-white">Signal-Android/iOS:</span> UI & Logic foundation</li>
              </ul>
            </div>
            
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2">A/V Engine</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">libwebrtc:</span> Audio/Video engine</li>
                <li><span className="text-white">Opus:</span> Low bandwidth, high quality codec</li>
                <li><span className="text-white">AEC3:</span> Echo cancellation</li>
              </ul>
            </div>
            
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2">Local Storage & Privacy</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">SQLCipher:</span> 256-bit AES DB encryption</li>
                <li><span className="text-white">Android Keystore:</span> TEE Key storage</li>
                <li><span className="text-white">Tor-Android:</span> Onion routing</li>
              </ul>
            </div>
            
            <div className="bg-primary border border-gray-800 rounded-xl p-4">
              <h4 className="font-medium text-white mb-2">Backend Infrastructure</h4>
              <ul className="space-y-2 text-sm text-text-secondary list-disc list-inside">
                <li><span className="text-white">Signal-Server:</span> Signaling & routing</li>
                <li><span className="text-white">Matrix/Dendrite:</span> Optional decentralized fallback</li>
              </ul>
            </div>
          </div>
        </section>

        {/* Implementation Modules */}
        <section className="space-y-4">
          <h3 className="text-lg font-semibold text-accent border-b border-gray-800 pb-2">3. Implementation Modules</h3>
          <div className="space-y-3">
            {[
              { mod: "Module 1", desc: "Environment Setup (Android Studio, Xcode, Docker)" },
              { mod: "Module 2", desc: "Rebranding (AMOLED Black theme, custom icons)" },
              { mod: "Module 3", desc: "Unique Obfuscation Layer (16-byte dummy padding + byte reversal)" },
              { mod: "Module 4", desc: "Signal Server Setup (Docker, Twilio, Redis, DynamoDB)" },
              { mod: "Module 5", desc: "WebRTC Configuration (AEC3, Network optimization)" },
              { mod: "Module 6", desc: "Privacy Layer (Tor SOCKS proxy integration)" },
              { mod: "Module 7", desc: "Encrypted Database (SQLCipher integration)" },
              { mod: "Module 8", desc: "Performance Optimization (ProGuard, Memory profiling)" },
            ].map((m, i) => (
              <div key={i} className="flex items-start space-x-3 text-sm">
                <span className="px-2 py-1 bg-gray-800 text-accent rounded text-xs whitespace-nowrap">{m.mod}</span>
                <span className="text-text-secondary pt-0.5">{m.desc}</span>
              </div>
            ))}
          </div>
        </section>

      </div>
    </div>
  );
}
