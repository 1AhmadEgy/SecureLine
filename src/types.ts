export type ViewMode = 'chat' | 'calls' | 'settings' | 'architecture' | 'vision' | 'runbook';

export interface Message {
  id: string;
  sender: 'me' | 'them';
  body: string;
  timestamp: Date;
  isEncrypted: boolean;
  isObfuscated: boolean;
  status?: 'sent' | 'delivered' | 'read';
  ephemeralSeconds?: number;
  remainingSeconds?: number;
}

export interface SecurityAuditEntry {
  id: number;
  userId: number | null;
  eventType: string;
  details: string | null;
  ipAddress: string | null;
  createdAt: string;
}

export interface SystemSecurityMetrics {
  torCircuits: number;
  sqlCipherPagesEncrypted: number;
  activeRatchetChains: number;
  ephemeralKeysDestroyed: number;
  hardwareStrongBox: boolean;
}
