export type ViewMode = 'chat' | 'calls' | 'settings' | 'architecture' | 'vision' | 'runbook';

export interface Message {
  id: string;
  sender: 'me' | 'them';
  body: string;
  timestamp: Date;
  isEncrypted: boolean;
  isObfuscated: boolean;
  status?: 'sent' | 'delivered' | 'read';
}
