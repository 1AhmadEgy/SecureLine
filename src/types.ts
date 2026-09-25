export type ViewMode = 'chat' | 'calls' | 'settings' | 'architecture' | 'vision' | 'runbook';

export interface EncryptedFileAttachment {
  id: string;
  name: string;
  size: number;
  type: string;
  cipherAlgorithm: 'AES-256-GCM';
  iv: string;
  checksum: string;
  keyRawHex: string;
  encryptedBlobUrl: string;
  originalPreviewUrl?: string;
  status: 'encrypted' | 'decrypted';
  encryptedAt: string;
}

export interface EncryptedReadReceipt {
  messageId: string;
  readerId: string;
  readerName: string;
  status: 'delivered' | 'read';
  blindedToken: string;
  receivedAt: Date;
  receiptMac: string;
}

export interface GroupMember {
  id: string;
  name: string;
  role: 'admin' | 'member';
  fingerprint: string;
  publicKeyHex: string;
  isVerified: boolean;
  joinedAt: string;
  hasSenderKey: boolean;
}

export interface GroupSession {
  id: string;
  name: string;
  description: string;
  createdAt: string;
  epoch: number;
  sessionFingerprint: string;
  activeSenderKeyHex: string;
  ratchetSteps: number;
  members: GroupMember[];
}

export interface Message {
  id: string;
  sender: 'me' | 'them' | string;
  senderName?: string;
  body: string;
  timestamp: Date;
  isEncrypted: boolean;
  isObfuscated: boolean;
  status?: 'sent' | 'delivered' | 'read';
  groupId?: string;
  ephemeralSeconds?: number;
  remainingSeconds?: number;
  expiresAt?: Date;
  fileAttachment?: EncryptedFileAttachment;
  receipts?: EncryptedReadReceipt[];
  blindedReceiptToken?: string;
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
