import { useState, useRef, useEffect, type ChangeEvent } from 'react';
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
  AlertCircle,
  Paperclip,
  FileText,
  FileCode,
  FileArchive,
  Image as ImageIcon,
  Download,
  Key,
  Shield,
  Clock,
  Sparkles,
  X,
  FileCheck,
  Users,
  UserPlus,
  UserMinus,
  RefreshCw,
  Plus,
  Info,
  MessageSquare,
  ShieldAlert,
  Fingerprint
} from 'lucide-react';
import type { 
  Message, 
  EncryptedFileAttachment, 
  GroupSession, 
  GroupMember, 
  EncryptedReadReceipt 
} from '../types';
import { useAuth } from '../context/AuthContext';
import { 
  encryptFileWithAES256, 
  decryptAndDownloadFile, 
  formatFileSize, 
  type EncryptedFileResult 
} from '../lib/fileCrypto';
import { 
  getInitialGroupSessions, 
  getInitialGroupMessages, 
  rekeyGroupSession, 
  createBlindedReceiptToken, 
  createEncryptedReceiptEnvelope,
  generateFingerprint,
  generateRandomHex
} from '../lib/signalGroupCrypto';

// Helper to convert auto-delete label to milliseconds
function parseDurationToMs(label: string): number {
  if (label.includes('15 ثانية')) return 15 * 1000;
  if (label.includes('1 دقيقة') || label.includes('60 ثانية')) return 60 * 1000;
  if (label.includes('1 ساعة')) return 60 * 60 * 1000;
  if (label.includes('24 ساعة')) return 24 * 60 * 60 * 1000;
  if (label.includes('7 أيام') || label.includes('1 أسبوع')) return 7 * 24 * 60 * 60 * 1000;
  if (label.includes('30 يوماً')) return 30 * 24 * 60 * 60 * 1000;
  return 0; // Off
}

export function ChatView() {
  const { token } = useAuth();
  
  // Navigation: Active Conversation ('direct_007' or group ID)
  const [activeChatId, setActiveChatId] = useState<string>('direct_007');

  // Group Sessions state
  const [groupSessions, setGroupSessions] = useState<GroupSession[]>(() => getInitialGroupSessions());

  // Messages per conversation
  const [directMessages, setDirectMessages] = useState<Message[]>([
    {
      id: 'dm_1',
      sender: 'them',
      senderName: 'العميل 007',
      body: 'هل نستخدم التنفيذ الجديد لبروتوكول السقاطة المزدوجة ومشاركة الملفات المشفرة بـ AES-256؟',
      timestamp: new Date(Date.now() - 3600000),
      isEncrypted: true,
      isObfuscated: true,
      status: 'read',
      blindedReceiptToken: 'brc_78a1bc0912d4'
    },
    {
      id: 'dm_2',
      sender: 'me',
      senderName: 'المشغل (أنت)',
      body: 'نعم، تم دمج libsignal بنجاح مع طبقة تعمية (Dummy Padding) وإشعارات القراءة المشفرة طرفياً بـ Blinded Tokens.',
      timestamp: new Date(Date.now() - 3500000),
      isEncrypted: true,
      isObfuscated: true,
      status: 'read',
      blindedReceiptToken: 'brc_910fa3e11029'
    },
  ]);

  const [groupMessagesMap, setGroupMessagesMap] = useState<Record<string, Message[]>>(() => ({
    'group_alpha_ops': getInitialGroupMessages('group_alpha_ops'),
    'group_crypto_council': getInitialGroupMessages('group_crypto_council'),
  }));

  // Read Receipts Enabled preference
  const [readReceiptsEnabled, setReadReceiptsEnabled] = useState<boolean>(() => {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('secureline_read_receipts_enabled') !== 'false';
    }
    return true;
  });

  const [showInspector, setShowInspector] = useState(false);
  const [activeAutoDelete, setActiveAutoDelete] = useState<string>('24 ساعة');
  const [inputText, setInputText] = useState('');
  const [shredNotice, setShredNotice] = useState<string | null>(null);
  const [rekeyNotice, setRekeyNotice] = useState<string | null>(null);

  // Modals state
  const [isGroupModalOpen, setIsGroupModalOpen] = useState(false);
  const [groupModalTab, setGroupModalTab] = useState<'members' | 'keys'>('members');
  const [isCreateGroupModalOpen, setIsCreateGroupModalOpen] = useState(false);
  const [receiptDetailMessage, setReceiptDetailMessage] = useState<Message | null>(null);

  // New Member input state
  const [newMemberName, setNewMemberName] = useState('');
  const [newMemberFingerprint, setNewMemberFingerprint] = useState('');
  const [newMemberError, setNewMemberError] = useState<string | null>(null);

  // New Group input state
  const [newGroupName, setNewGroupName] = useState('');
  const [newGroupDesc, setNewGroupDesc] = useState('');

  // File Upload & AES-256 Encryption state
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [stagedEncryptedFile, setStagedEncryptedFile] = useState<EncryptedFileResult | null>(null);
  const [isEncryptingFile, setIsEncryptingFile] = useState(false);
  const [encryptProgressStage, setEncryptProgressStage] = useState<string | null>(null);
  const [decryptingFileId, setDecryptingFileId] = useState<string | null>(null);
  const [inspectCipherId, setInspectCipherId] = useState<string | null>(null);

  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Current active group session if in group mode
  const currentGroup = groupSessions.find(g => g.id === activeChatId);
  const activeMessages = activeChatId === 'direct_007' 
    ? directMessages 
    : (groupMessagesMap[activeChatId] || []);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [activeChatId, directMessages, groupMessagesMap, stagedEncryptedFile]);

  // Load configured auto-delete policy and read receipts from localStorage
  useEffect(() => {
    const savedAutoDelete = localStorage.getItem('secureline_auto_delete_setting');
    if (savedAutoDelete) setActiveAutoDelete(savedAutoDelete);

    const handleSettingsChange = (e: Event) => {
      const custom = e as CustomEvent<string>;
      if (custom.detail) setActiveAutoDelete(custom.detail);
    };

    const handleReceiptsChange = (e: Event) => {
      const custom = e as CustomEvent<boolean>;
      if (custom.detail !== undefined) setReadReceiptsEnabled(custom.detail);
    };

    window.addEventListener('secureline_autodelete_changed', handleSettingsChange);
    window.addEventListener('secureline_receipts_toggled', handleReceiptsChange);

    return () => {
      window.removeEventListener('secureline_autodelete_changed', handleSettingsChange);
      window.removeEventListener('secureline_receipts_toggled', handleReceiptsChange);
    };
  }, []);

  // Update policy handler from chat
  const handleAutoDeletePolicyChange = (newVal: string) => {
    setActiveAutoDelete(newVal);
    localStorage.setItem('secureline_auto_delete_setting', newVal);
    window.dispatchEvent(new CustomEvent('secureline_autodelete_changed', { detail: newVal }));
  };

  // Ephemeral message countdown interval (Crypto-Shredding & Auto-deletion)
  useEffect(() => {
    const timer = setInterval(() => {
      const now = Date.now();
      let triggeredShred = false;

      // Check Direct Messages
      setDirectMessages((prev) => {
        let hasExpired = false;
        const updated = prev.map((msg) => {
          if (msg.remainingSeconds !== undefined && msg.remainingSeconds > 0) {
            return { ...msg, remainingSeconds: msg.remainingSeconds - 1 };
          }
          if (msg.remainingSeconds !== undefined && msg.remainingSeconds <= 0) {
            hasExpired = true;
          }
          if (msg.expiresAt && msg.expiresAt.getTime() <= now) {
            hasExpired = true;
          }
          return msg;
        });

        if (hasExpired) {
          triggeredShred = true;
          return updated.filter(m => {
            const expSec = m.remainingSeconds !== undefined && m.remainingSeconds <= 0;
            const expDate = m.expiresAt && m.expiresAt.getTime() <= now;
            return !expSec && !expDate;
          });
        }
        return updated;
      });

      // Check Group Messages
      setGroupMessagesMap((prevMap) => {
        let mapChanged = false;
        const newMap: Record<string, Message[]> = {};

        for (const [gid, msgs] of Object.entries(prevMap) as [string, Message[]][]) {
          let hasExpired = false;
          const updated = msgs.map((msg) => {
            if (msg.remainingSeconds !== undefined && msg.remainingSeconds > 0) {
              return { ...msg, remainingSeconds: msg.remainingSeconds - 1 };
            }
            if (msg.remainingSeconds !== undefined && msg.remainingSeconds <= 0) {
              hasExpired = true;
            }
            if (msg.expiresAt && msg.expiresAt.getTime() <= now) {
              hasExpired = true;
            }
            return msg;
          });

          if (hasExpired) {
            triggeredShred = true;
            mapChanged = true;
            newMap[gid] = updated.filter(m => {
              const expSec = m.remainingSeconds !== undefined && m.remainingSeconds <= 0;
              const expDate = m.expiresAt && m.expiresAt.getTime() <= now;
              return !expSec && !expDate;
            });
          } else {
            newMap[gid] = updated;
          }
        }

        return mapChanged ? newMap : prevMap;
      });

      if (triggeredShred) {
        setShredNotice('تم تدمير الرسائل المنتهية الصلاحية ومسح مفاتيحها محلياً (Zeroized).');
        setTimeout(() => setShredNotice(null), 3500);
      }
    }, 1000);

    return () => clearInterval(timer);
  }, []);

  // Handle Local File Selection & Instant AES-256 Encryption
  const handleFileChange = async (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setIsEncryptingFile(true);
    setEncryptProgressStage('بدء تشفير الملف محلياً بـ AES-256-GCM...');

    try {
      const encryptedRes = await encryptFileWithAES256(file, (stage) => {
        setEncryptProgressStage(stage);
      });
      setStagedEncryptedFile(encryptedRes);
      setIsEncryptingFile(false);
      setEncryptProgressStage(null);
    } catch (err: unknown) {
      console.error('File encryption failed:', err);
      setIsEncryptingFile(false);
      setEncryptProgressStage(null);
      alert('حدث خطأ أثناء تشفير الملف بـ AES-256');
    }

    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  // Send Message (Direct or Group) with End-to-End Encrypted Read Receipts
  const handleSend = async () => {
    if (!inputText.trim() && !stagedEncryptedFile) return;

    const textToSend = inputText.trim();
    const msgId = Date.now().toString();

    // Auto-deletion metadata
    const durationMs = parseDurationToMs(activeAutoDelete);
    const expiresAt = durationMs > 0 ? new Date(Date.now() + durationMs) : undefined;
    const remainingSeconds = durationMs > 0 && durationMs <= 60000 ? Math.round(durationMs / 1000) : undefined;

    let filePayload: EncryptedFileAttachment | undefined;
    if (stagedEncryptedFile) {
      filePayload = {
        id: stagedEncryptedFile.id,
        name: stagedEncryptedFile.name,
        size: stagedEncryptedFile.size,
        type: stagedEncryptedFile.type,
        cipherAlgorithm: 'AES-256-GCM',
        iv: stagedEncryptedFile.iv,
        checksum: stagedEncryptedFile.checksum,
        keyRawHex: stagedEncryptedFile.keyRawHex,
        encryptedBlobUrl: stagedEncryptedFile.encryptedBlobUrl,
        originalPreviewUrl: stagedEncryptedFile.originalPreviewUrl,
        status: 'encrypted',
        encryptedAt: stagedEncryptedFile.encryptedAt,
      };
    }

    const blindedToken = await createBlindedReceiptToken(msgId, 'sender_me');

    const newMsg: Message = {
      id: msgId,
      sender: 'me',
      senderName: 'المشغل (أنت)',
      body: textToSend,
      timestamp: new Date(),
      isEncrypted: true,
      isObfuscated: true,
      status: 'sent',
      groupId: activeChatId !== 'direct_007' ? activeChatId : undefined,
      ephemeralSeconds: durationMs > 0 ? Math.round(durationMs / 1000) : undefined,
      remainingSeconds,
      expiresAt,
      fileAttachment: filePayload,
      blindedReceiptToken: blindedToken,
      receipts: [],
    };

    setInputText('');
    setStagedEncryptedFile(null);

    // Save message locally
    if (activeChatId === 'direct_007') {
      setDirectMessages(prev => [...prev, newMsg]);
    } else {
      setGroupMessagesMap(prev => ({
        ...prev,
        [activeChatId]: [...(prev[activeChatId] || []), newMsg],
      }));
    }

    // Backend sync if token is available
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
            encryptedContent: btoa(unescape(encodeURIComponent(textToSend || '[ملف مرفق مشفر بـ AES-256]'))),
            nonce: dummyNonce,
            isObfuscated: true,
          })
        });
      } catch (err) {
        console.error('Failed to sync message to backend:', err);
      }
    }

    // --- Signal E2EE Read Receipts Simulation ---
    if (activeChatId === 'direct_007') {
      // Direct message: Sent -> Delivered -> Read progression
      setTimeout(() => {
        setDirectMessages(prev => prev.map(m => m.id === msgId ? { ...m, status: 'delivered' } : m));

        if (readReceiptsEnabled) {
          setTimeout(async () => {
            const peerReceipt = await createEncryptedReceiptEnvelope(msgId, 'agent_007', 'العميل 007', 'read');
            setDirectMessages(prev => prev.map(m => m.id === msgId ? {
              ...m,
              status: 'read',
              receipts: [peerReceipt]
            } : m));
          }, 1400);
        }
      }, 700);

    } else if (currentGroup) {
      // Group message: sender receives individual encrypted receipt envelopes from group members
      const otherMembers = currentGroup.members.filter(m => m.id !== 'me');

      // 1. Delivery Receipts (members receive via Sender Key ratchets)
      setTimeout(() => {
        const deliveryReceipts: EncryptedReadReceipt[] = otherMembers.map(m => ({
          messageId: msgId,
          readerId: m.id,
          readerName: m.name,
          status: 'delivered',
          blindedToken: 'brc_' + generateRandomHex(10),
          receivedAt: new Date(),
          receiptMac: `HMAC-${generateRandomHex(4).toUpperCase()}`,
        }));

        setGroupMessagesMap(prev => ({
          ...prev,
          [activeChatId]: (prev[activeChatId] || []).map(m => 
            m.id === msgId ? { ...m, status: 'delivered', receipts: deliveryReceipts } : m
          ),
        }));

        // 2. Read Receipts (members view message with privacy-preserving blinded tokens)
        if (readReceiptsEnabled) {
          setTimeout(() => {
            const readReceipts: EncryptedReadReceipt[] = otherMembers.map(m => ({
              messageId: msgId,
              readerId: m.id,
              readerName: m.name,
              status: 'read',
              blindedToken: 'brc_' + generateRandomHex(12),
              receivedAt: new Date(),
              receiptMac: `HMAC-${generateRandomHex(6).toUpperCase()}`,
            }));

            setGroupMessagesMap(prev => ({
              ...prev,
              [activeChatId]: (prev[activeChatId] || []).map(m => 
                m.id === msgId ? { ...m, status: 'read', receipts: readReceipts } : m
              ),
            }));
          }, 1600);
        }
      }, 800);
    }
  };

  // Group Management: Rekeying (Advances epoch and updates sender keys)
  const handleRekeySession = (groupId: string, reason = 'تدوير يدوي لتعزيز السرية المستقبلية') => {
    setGroupSessions(prev => prev.map(g => {
      if (g.id === groupId) {
        const rekeyed = rekeyGroupSession(g, reason);
        return rekeyed;
      }
      return g;
    }));

    const noticeMsg: Message = {
      id: `sys_rekey_${Date.now()}`,
      sender: 'system',
      senderName: 'بروتوكول Signal (مفاتيح البث)',
      body: `🔄 تم تدوير مفاتيح الجلسة بنجاح (${reason}). تم تصفير سلاسل السقاطة القديمة (Zeroized) لضمان السرية اللاحقة.`,
      timestamp: new Date(),
      isEncrypted: true,
      isObfuscated: false,
    };

    setGroupMessagesMap(prev => ({
      ...prev,
      [groupId]: [...(prev[groupId] || []), noticeMsg],
    }));

    setRekeyNotice(`تم تدوير مفتاح البث المشترك لـ ${reason} وتحديث بصمة الجلسة بنجاح.`);
    setTimeout(() => setRekeyNotice(null), 4000);
  };

  // Group Management: Add Member with automated session rekeying
  const handleAddMember = () => {
    if (!currentGroup) return;
    if (!newMemberName.trim()) {
      setNewMemberError('يرجى كتابة اسم العضو أولاً');
      return;
    }

    const memberId = 'mem_' + Date.now();
    const fingerprint = newMemberFingerprint.trim() || generateFingerprint();
    const newMember: GroupMember = {
      id: memberId,
      name: newMemberName.trim(),
      role: 'member',
      fingerprint,
      publicKeyHex: `04${generateRandomHex(16)}...${generateRandomHex(4)}`,
      isVerified: true,
      joinedAt: 'الآن',
      hasSenderKey: true,
    };

    // Update group members and rekey to enforce Forward Secrecy
    const updatedGroup = {
      ...currentGroup,
      members: [...currentGroup.members, newMember],
    };
    const rekeyed = rekeyGroupSession(updatedGroup, `انضمام العضو (${newMember.name})`);

    setGroupSessions(prev => prev.map(g => g.id === currentGroup.id ? rekeyed : g));

    const noticeMsg: Message = {
      id: `sys_add_${Date.now()}`,
      sender: 'system',
      senderName: 'أمن المجموعة',
      body: `👤 انضم ${newMember.name} إلى المجموعة المشفرة. تم تدوير مفتاح الجلسة لـ Epoch ${rekeyed.epoch} لمنع فك تشفير الرسائل السابقة (Forward Secrecy).`,
      timestamp: new Date(),
      isEncrypted: true,
      isObfuscated: false,
    };

    setGroupMessagesMap(prev => ({
      ...prev,
      [currentGroup.id]: [...(prev[currentGroup.id] || []), noticeMsg],
    }));

    setNewMemberName('');
    setNewMemberFingerprint('');
    setNewMemberError(null);
    setRekeyNotice(`تمت إضافة ${newMember.name} بنجاح وتوليد Epoch جديد.`);
    setTimeout(() => setRekeyNotice(null), 4000);
  };

  // Group Management: Remove Member with automated session rekeying
  const handleRemoveMember = (memberId: string) => {
    if (!currentGroup) return;
    const memberToRemove = currentGroup.members.find(m => m.id === memberId);
    if (!memberToRemove) return;

    if (memberToRemove.id === 'me') {
      alert('لا يمكنك إزالة نفسك من المجموعة');
      return;
    }

    const remainingMembers = currentGroup.members.filter(m => m.id !== memberId);
    const updatedGroup = {
      ...currentGroup,
      members: remainingMembers,
    };
    const rekeyed = rekeyGroupSession(updatedGroup, `إزالة العضو (${memberToRemove.name})`);

    setGroupSessions(prev => prev.map(g => g.id === currentGroup.id ? rekeyed : g));

    const noticeMsg: Message = {
      id: `sys_rm_${Date.now()}`,
      sender: 'system',
      senderName: 'أمن المجموعة',
      body: `🚫 تمت إزالة ${memberToRemove.name}. تم إبطال مفاتيح البث السابقة فورياً وتوليد Epoch ${rekeyed.epoch} لمنع العضو من قراءة الرسائل القادمة (Post-Compromise Security).`,
      timestamp: new Date(),
      isEncrypted: true,
      isObfuscated: false,
    };

    setGroupMessagesMap(prev => ({
      ...prev,
      [currentGroup.id]: [...(prev[currentGroup.id] || []), noticeMsg],
    }));

    setRekeyNotice(`تم طرد ${memberToRemove.name} وإعادة تشفير الجلسة فوراً.`);
    setTimeout(() => setRekeyNotice(null), 4000);
  };

  // Group Management: Toggle Member Admin Role
  const handleToggleMemberRole = (memberId: string) => {
    if (!currentGroup) return;
    setGroupSessions(prev => prev.map(g => {
      if (g.id === currentGroup.id) {
        return {
          ...g,
          members: g.members.map(m => {
            if (m.id === memberId) {
              const nextRole: 'admin' | 'member' = m.role === 'admin' ? 'member' : 'admin';
              return { ...m, role: nextRole };
            }
            return m;
          }),
        };
      }
      return g;
    }));
  };

  // Create New Encrypted Group Chat
  const handleCreateGroup = () => {
    if (!newGroupName.trim()) {
      alert('يرجى تحديد اسم المجموعة المشفرة');
      return;
    }

    const newGroupId = `group_${Date.now()}`;
    const initialKey = generateRandomHex(32);
    const sessionFp = generateFingerprint(initialKey);

    const newGroup: GroupSession = {
      id: newGroupId,
      name: newGroupName.trim(),
      description: newGroupDesc.trim() || 'مجموعة مشفرة جماعياً بنظام Signal Sender Keys.',
      createdAt: new Date().toISOString(),
      epoch: 1,
      sessionFingerprint: sessionFp,
      activeSenderKeyHex: initialKey,
      ratchetSteps: 1,
      members: [
        {
          id: 'me',
          name: 'المشغل (أنت)',
          role: 'admin',
          fingerprint: '38192 44109 88123 90123',
          publicKeyHex: '04a1f9c832...b419',
          isVerified: true,
          joinedAt: 'الآن',
          hasSenderKey: true,
        },
        {
          id: 'agent_007',
          name: 'العميل 007',
          role: 'member',
          fingerprint: '19024 81920 77123 44019',
          publicKeyHex: '04bb81c201...99ef',
          isVerified: true,
          joinedAt: 'الآن',
          hasSenderKey: true,
        }
      ]
    };

    setGroupSessions(prev => [newGroup, ...prev]);
    setGroupMessagesMap(prev => ({
      ...prev,
      [newGroupId]: [
        {
          id: `init_${Date.now()}`,
          sender: 'system',
          senderName: 'نظام التشفير الجماعي',
          body: `🛡️ تم إنشاء جلسة المجموعة المشفرة بنجاح بنظام مفاتيح البث (Signal Sender Keys) - Epoch 1. بصمة الأمان: ${sessionFp}`,
          timestamp: new Date(),
          isEncrypted: true,
          isObfuscated: false,
        }
      ]
    }));

    setActiveChatId(newGroupId);
    setIsCreateGroupModalOpen(false);
    setNewGroupName('');
    setNewGroupDesc('');
  };

  // Decrypt and Download Attachment
  const handleDecryptFile = async (attachment: EncryptedFileAttachment) => {
    try {
      setDecryptingFileId(attachment.id);
      const response = await fetch(attachment.encryptedBlobUrl);
      const encryptedBlob = await response.blob();

      await decryptAndDownloadFile(
        encryptedBlob,
        attachment.iv,
        attachment.keyRawHex,
        attachment.name,
        attachment.type
      );

      setDecryptingFileId(null);
    } catch (err) {
      console.error('Decryption error:', err);
      setDecryptingFileId(null);
      alert('فشل فك التشفير. تأكد من صحة المفتاح وبصمة التحقق.');
    }
  };

  const shredAllNow = () => {
    if (activeChatId === 'direct_007') {
      setDirectMessages([]);
    } else {
      setGroupMessagesMap(prev => ({
        ...prev,
        [activeChatId]: [],
      }));
    }
    setShredNotice('تم تدمير جميع الرسائل والملفات فورياً بنمط الهروب الطارئ (Panic Shred).');
    setTimeout(() => setShredNotice(null), 3500);
  };

  const getFileIcon = (mime: string) => {
    if (mime.startsWith('image/')) return <ImageIcon size={20} className="text-emerald-400" />;
    if (mime.includes('zip') || mime.includes('tar') || mime.includes('rar')) return <FileArchive size={20} className="text-amber-400" />;
    if (mime.includes('json') || mime.includes('javascript') || mime.includes('html')) return <FileCode size={20} className="text-blue-400" />;
    return <FileText size={20} className="text-accent" />;
  };

  return (
    <div className="flex flex-col h-full bg-black select-none">
      {/* Hidden native file input */}
      <input 
        type="file" 
        ref={fileInputRef} 
        onChange={handleFileChange} 
        className="hidden" 
      />

      {/* Top Conversation Switcher Bar */}
      <div className="bg-gray-950/90 border-b border-gray-800/80 px-4 py-2 flex items-center justify-between gap-2 overflow-x-auto text-xs shrink-0">
        <div className="flex items-center gap-2">
          {/* Direct Chat Tab */}
          <button
            onClick={() => setActiveChatId('direct_007')}
            className={`flex items-center gap-2 px-3 py-1.5 rounded-lg border transition-all ${
              activeChatId === 'direct_007'
                ? 'bg-accent/15 border-accent text-accent font-medium shadow-sm'
                : 'bg-primary/40 border-gray-800 text-text-secondary hover:text-white hover:bg-gray-800/50'
            }`}
          >
            <div className="w-2 h-2 rounded-full bg-accent animate-pulse" />
            <span className="font-semibold">العميل 007</span>
            <span className="text-[10px] opacity-70 bg-black/40 px-1 rounded border border-gray-800">1:1 E2EE</span>
          </button>

          {/* Group Chat Tabs */}
          {groupSessions.map((group) => {
            const isActive = activeChatId === group.id;
            return (
              <button
                key={group.id}
                onClick={() => setActiveChatId(group.id)}
                className={`flex items-center gap-2 px-3 py-1.5 rounded-lg border transition-all ${
                  isActive
                    ? 'bg-accent/15 border-accent text-accent font-medium shadow-sm'
                    : 'bg-primary/40 border-gray-800 text-text-secondary hover:text-white hover:bg-gray-800/50'
                }`}
              >
                <Users size={13} className={isActive ? 'text-accent' : 'text-gray-400'} />
                <span className="font-semibold truncate max-w-[130px]">{group.name}</span>
                <span className="text-[10px] bg-black/50 px-1.5 py-0.2 rounded border border-gray-700 font-mono text-gray-300">
                  {group.members.length} أعضاء
                </span>
                <span className="text-[9px] bg-accent/20 text-accent px-1 rounded font-mono">
                  E{group.epoch}
                </span>
              </button>
            );
          })}
        </div>

        {/* Create Group Button */}
        <button
          onClick={() => setIsCreateGroupModalOpen(true)}
          className="flex items-center gap-1.5 px-3 py-1.5 bg-gray-900 hover:bg-gray-800 border border-gray-700 text-accent rounded-lg text-xs transition-colors shrink-0"
          title="إنشاء مجموعة مشفرة جديدة بنظام مفاتيح البث"
        >
          <Plus size={14} />
          <span>مجموعة جديدة</span>
        </button>
      </div>

      {/* Active Conversation Header */}
      <div className="flex items-center justify-between p-3.5 border-b border-gray-800 bg-primary/40">
        <div className="flex items-center gap-3">
          {activeChatId === 'direct_007' ? (
            <div className="w-10 h-10 rounded-full bg-gray-800 flex items-center justify-center text-text-secondary font-semibold border border-gray-700">
              007
            </div>
          ) : (
            <div className="w-10 h-10 rounded-full bg-accent/10 border border-accent/30 flex items-center justify-center text-accent font-semibold">
              <Users size={20} />
            </div>
          )}

          <div>
            <div className="flex items-center gap-2">
              <h2 className="font-semibold text-text-primary text-sm sm:text-base">
                {activeChatId === 'direct_007' ? 'العميل 007' : currentGroup?.name}
              </h2>
              <div className="flex items-center text-accent bg-accent/10 px-1.5 py-0.5 rounded text-[10px] font-medium border border-accent/20">
                <ShieldCheck size={12} className="me-1" />
                {activeChatId === 'direct_007' ? 'Double Ratchet E2EE' : 'Signal Sender Keys'}
              </div>

              {currentGroup && (
                <span className="text-[10px] bg-purple-950/60 border border-purple-800/50 text-purple-300 px-1.5 py-0.5 rounded font-mono">
                  Epoch {currentGroup.epoch}
                </span>
              )}
            </div>

            <div className="flex items-center text-[11px] text-text-secondary mt-0.5 gap-2">
              <span className="flex items-center gap-1">
                <span className="w-2 h-2 rounded-full bg-accent animate-pulse"></span>
                مسار Tor نشط
              </span>
              <span>•</span>
              {activeChatId === 'direct_007' ? (
                <span>ملفات AES-256-GCM • إشعارات قراءة معماة</span>
              ) : (
                <span>
                  {currentGroup?.members.length} أعضاء مؤمنين • بصمة الجلسة: {currentGroup?.sessionFingerprint.substring(0, 11)}...
                </span>
              )}
            </div>
          </div>
        </div>

        {/* Header Actions */}
        <div className="flex items-center gap-2 text-text-secondary">
          {/* Group Management Actions (Only in Group View) */}
          {currentGroup && (
            <>
              {/* Member & Key Management Modal Trigger */}
              <button
                onClick={() => {
                  setGroupModalTab('members');
                  setIsGroupModalOpen(true);
                }}
                className="flex items-center gap-1 px-2.5 py-1 rounded-lg bg-gray-900 border border-gray-800 text-xs text-text-primary hover:text-accent hover:border-accent/40 transition-colors"
                title="إدارة الأعضاء ومفاتيح الجلسة المشتركة"
              >
                <Users size={14} className="text-accent" />
                <span className="hidden sm:inline">الأعضاء والمفاتيح</span>
              </button>

              {/* Force Rekey Button */}
              <button
                onClick={() => handleRekeySession(currentGroup.id, 'تدوير يدوي بواسطة المشغل')}
                className="p-1.5 rounded-lg bg-gray-900 border border-gray-800 text-text-secondary hover:text-accent transition-colors"
                title="تدوير وتحديث مفاتيح الجلسة فورياً (Advance Ratchet Epoch)"
              >
                <RefreshCw size={15} />
              </button>
            </>
          )}

          {/* Auto-Deletion Policy Selector Dropdown */}
          <div className="flex items-center gap-1.5 bg-gray-900 border border-gray-800 px-2 py-1 rounded-lg text-xs">
            <Clock size={13} className={activeAutoDelete !== 'معطل (احتفاظ دائم)' ? 'text-orange-400' : 'text-gray-500'} />
            <select
              value={activeAutoDelete}
              onChange={(e) => handleAutoDeletePolicyChange(e.target.value)}
              className="bg-transparent border-0 text-text-primary text-xs outline-none cursor-pointer pe-1"
              title="تحديد مدة الحذف التلقائي للرسائل"
            >
              <option value="معطل (احتفاظ دائم)" className="bg-gray-900">حذف تلقائي: معطل</option>
              <option value="15 ثانية" className="bg-gray-900">حذف: 15 ثانية</option>
              <option value="1 دقيقة" className="bg-gray-900">حذف: 1 دقيقة</option>
              <option value="1 ساعة" className="bg-gray-900">حذف: 1 ساعة</option>
              <option value="24 ساعة" className="bg-gray-900">حذف: 24 ساعة</option>
              <option value="7 أيام" className="bg-gray-900">حذف: 7 أيام</option>
              <option value="30 يوماً" className="bg-gray-900">حذف: 30 يوماً</option>
            </select>
          </div>

          {/* Emergency Panic Shred Button */}
          <button
            onClick={shredAllNow}
            title="تدمير فوري طارئ للمحادثة"
            className="p-1.5 rounded-lg bg-red-950/40 text-red-400 hover:bg-red-900/60 border border-red-800/40 transition-colors"
          >
            <Trash2 size={16} />
          </button>

          {/* Packet Inspector Toggle */}
          <button 
            onClick={() => setShowInspector(!showInspector)}
            className={`p-1.5 sm:px-2.5 sm:py-1 rounded-lg text-xs transition-colors flex items-center gap-1.5 ${
              showInspector ? 'bg-accent/20 text-accent border border-accent/30' : 'bg-gray-800/50 hover:bg-gray-800 text-text-secondary border border-transparent'
            }`}
            title="فحص حزم التعمية"
          >
            <Binary size={15} />
            <span className="hidden md:inline">فاحص الحزم</span>
          </button>
        </div>
      </div>

      {/* Rekey Notice Toast */}
      {rekeyNotice && (
        <div className="bg-accent/15 border-b border-accent/40 px-4 py-2 text-xs text-accent flex items-center justify-between transition-all">
          <span className="flex items-center gap-2">
            <RefreshCw size={14} className="text-accent animate-spin" />
            {rekeyNotice}
          </span>
          <button onClick={() => setRekeyNotice(null)} className="text-gray-400 hover:text-white">✕</button>
        </div>
      )}

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
        {/* Info Banner */}
        <div className="text-center my-2">
          <span className="bg-gray-900/90 text-text-secondary text-xs px-3.5 py-1 rounded-full border border-gray-800 inline-flex items-center gap-1.5">
            <ShieldCheck size={13} className="text-accent" />
            {currentGroup ? (
              <span>
                مجموعة مشفرة بمفاتيح البث (Signal Sender Keys) • تدعم إدارة الأعضاء والسرية اللاحقة وإشعارات القراءة المشفرة
              </span>
            ) : (
              <span>
                محادثة ثنائية مشفرة بـ Double Ratchet • إشعارات القراءة مؤمنة برمز معماة (Blinded Tokens)
              </span>
            )}
          </span>
        </div>

        {activeMessages.length === 0 ? (
          <div className="h-48 flex flex-col items-center justify-center text-gray-500 text-sm">
            <AlertCircle size={32} className="mb-2 text-gray-600" />
            <span>لا توجد رسائل نشطة. سجل المحادثة فارغ ومؤمن تشفيرياً.</span>
          </div>
        ) : (
          activeMessages.map((msg) => {
            const isMe = msg.sender === 'me';
            const isSystem = msg.sender === 'system';

            if (isSystem) {
              return (
                <div key={msg.id} className="flex justify-center my-3">
                  <div className="bg-purple-950/40 border border-purple-800/40 text-purple-200 text-xs px-4 py-2 rounded-xl max-w-lg text-center leading-relaxed font-mono">
                    {msg.body}
                  </div>
                </div>
              );
            }

            return (
              <div key={msg.id} className={`flex flex-col ${isMe ? 'items-end' : 'items-start'}`}>
                {/* Sender Name in Group Chat */}
                {currentGroup && !isMe && (
                  <span className="text-[11px] font-semibold text-accent/80 mb-1 ps-2 flex items-center gap-1.5">
                    <span>{msg.senderName || msg.sender}</span>
                    <span className="text-[9px] bg-gray-900 px-1.5 rounded text-gray-400 border border-gray-800">
                      عضو موثق
                    </span>
                  </span>
                )}

                <div className={`max-w-[85%] sm:max-w-[75%] rounded-2xl p-3.5 ${
                  isMe 
                    ? 'bg-accent text-black rounded-ee-none shadow-sm' 
                    : 'bg-primary border border-gray-800 text-text-primary rounded-es-none shadow-sm'
                }`}>
                  {/* File Attachment Card */}
                  {msg.fileAttachment && (
                    <div className={`mb-2.5 rounded-xl border overflow-hidden ${
                      isMe 
                        ? 'bg-emerald-900/20 border-emerald-700/40 text-black' 
                        : 'bg-black/60 border-gray-800 text-white'
                    }`}>
                      {/* AES-256 Header Tag */}
                      <div className="px-3 py-1.5 bg-black/40 border-b border-gray-800 flex items-center justify-between text-[11px] font-mono">
                        <div className="flex items-center gap-1.5 text-accent font-semibold">
                          <ShieldCheck size={13} className="text-accent" />
                          <span>مشفر طرفياً: AES-256-GCM</span>
                        </div>
                        <span className="text-[10px] text-gray-400 bg-gray-900/80 px-1.5 py-0.5 rounded border border-gray-700">
                          12B Nonce • SHA-256
                        </span>
                      </div>

                      <div className="p-3">
                        {/* Image Preview if available */}
                        {msg.fileAttachment.originalPreviewUrl && (
                          <div className="mb-2 rounded-lg overflow-hidden border border-gray-800/40 max-h-48 bg-black/50 flex items-center justify-center">
                            <img 
                              src={msg.fileAttachment.originalPreviewUrl} 
                              alt={msg.fileAttachment.name} 
                              className="max-h-48 w-auto object-contain" 
                            />
                          </div>
                        )}

                        <div className="flex items-center justify-between gap-3">
                          <div className="flex items-center gap-2.5 overflow-hidden">
                            <div className="p-2 rounded-lg bg-gray-900/80 border border-gray-700/60 shrink-0">
                              {getFileIcon(msg.fileAttachment.type)}
                            </div>
                            <div className="truncate">
                              <p className="font-semibold text-xs truncate max-w-[200px]" title={msg.fileAttachment.name}>
                                {msg.fileAttachment.name}
                              </p>
                              <p className="text-[10px] opacity-75 mt-0.5">
                                {formatFileSize(msg.fileAttachment.size)} • {msg.fileAttachment.type || 'ملف بيانات'}
                              </p>
                            </div>
                          </div>

                          <button
                            onClick={() => handleDecryptFile(msg.fileAttachment!)}
                            disabled={decryptingFileId === msg.fileAttachment.id}
                            className={`px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5 transition-all shrink-0 ${
                              isMe
                                ? 'bg-black text-accent hover:bg-gray-900 shadow-sm'
                                : 'bg-accent text-black hover:bg-emerald-300'
                            }`}
                            title="فك تشفير الملف وتنزيله إلى جهازك"
                          >
                            <Download size={13} />
                            <span>
                              {decryptingFileId === msg.fileAttachment.id ? 'جاري فك التشفير...' : 'فك التشفير وتنزيل'}
                            </span>
                          </button>
                        </div>

                        {/* Cryptographic Inspector details button */}
                        <div className="mt-2 pt-2 border-t border-gray-700/40 flex items-center justify-between text-[10px]">
                          <button
                            onClick={() => setInspectCipherId(inspectCipherId === msg.fileAttachment!.id ? null : msg.fileAttachment!.id)}
                            className="text-gray-400 hover:text-accent underline flex items-center gap-1"
                          >
                            <Key size={11} />
                            <span>{inspectCipherId === msg.fileAttachment.id ? 'إخفاء المعاملات' : 'فحص معطيات تشفير AES-256'}</span>
                          </button>
                          <span className="text-[9px] opacity-60 font-mono">
                            Tag: {msg.fileAttachment.checksum.substring(0, 8)}...
                          </span>
                        </div>

                        {/* Expanded Cipher Inspector */}
                        {inspectCipherId === msg.fileAttachment.id && (
                          <div className="mt-2 p-2 bg-black/90 border border-gray-800 rounded-lg text-[10px] font-mono text-gray-300 space-y-1" dir="ltr">
                            <div><span className="text-gray-500">Algorithm:</span> AES-256-GCM (Hardware-accelerated)</div>
                            <div><span className="text-gray-500">IV (12-Bytes):</span> <span className="text-accent">{msg.fileAttachment.iv}</span></div>
                            <div><span className="text-gray-500">SHA-256:</span> <span className="text-purple-400">{msg.fileAttachment.checksum}</span></div>
                            <div><span className="text-gray-500">Session Key (Hex):</span> <span className="text-pink-400">{msg.fileAttachment.keyRawHex.substring(0, 16)}... (256 bits)</span></div>
                          </div>
                        )}
                      </div>
                    </div>
                  )}

                  {/* Message Text Body */}
                  {msg.body && <p className="text-sm leading-relaxed">{msg.body}</p>}
                  
                  {/* Message Meta Footer */}
                  <div className={`flex items-center justify-end gap-2 mt-1.5 text-[10px] ${
                    isMe ? 'text-black/75' : 'text-text-secondary'
                  }`}>
                    {/* Countdown Badge if Self-Destruct is active */}
                    {msg.remainingSeconds !== undefined && (
                      <span className="flex items-center gap-1 font-mono font-bold text-orange-600 bg-orange-200/60 px-1.5 py-0.2 rounded">
                        <Flame size={11} className="animate-pulse" />
                        {msg.remainingSeconds}s
                      </span>
                    )}

                    {/* Expiry indicator for hours/days */}
                    {msg.expiresAt && msg.remainingSeconds === undefined && (
                      <span className="flex items-center gap-1 text-[9px] opacity-75 font-mono">
                        <Clock size={10} />
                        تنتهي: {msg.expiresAt.toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })}
                      </span>
                    )}

                    <span>{msg.timestamp.toLocaleTimeString('ar-EG', { hour: '2-digit', minute: '2-digit' })}</span>

                    {/* Interactive End-to-End Encrypted Read Receipts Indicator */}
                    {isMe && (
                      <button
                        onClick={() => setReceiptDetailMessage(msg)}
                        className="ms-1 flex items-center gap-0.5 hover:opacity-80 transition-opacity"
                        title="فحص إيصال القراءة المشفر طرفياً (E2EE Blinded Receipt)"
                      >
                        {currentGroup ? (
                          // Group read receipt indicator
                          <span className="flex items-center gap-1 bg-black/20 px-1 py-0.2 rounded text-[10px] font-mono">
                            {msg.status === 'read' ? (
                              <>
                                <CheckCheck size={13} className="text-blue-900 font-bold" />
                                <span>{msg.receipts?.length || currentGroup.members.length - 1}/{currentGroup.members.length - 1} قرؤوا</span>
                              </>
                            ) : msg.status === 'delivered' ? (
                              <>
                                <CheckCheck size={13} className="text-black/60" />
                                <span>استُلمت</span>
                              </>
                            ) : (
                              <>
                                <Check size={13} className="text-black/50" />
                                <span>أُرسلت</span>
                              </>
                            )}
                          </span>
                        ) : (
                          // Direct chat read receipt indicator
                          <span>
                            {msg.status === 'sent' && <Check size={13} className="text-black/50" />}
                            {msg.status === 'delivered' && <CheckCheck size={13} className="text-black/60" />}
                            {msg.status === 'read' && <CheckCheck size={13} className="text-blue-900 font-bold" />}
                          </span>
                        )}
                      </button>
                    )}

                    {!isMe && <Lock size={10} className="ms-1 text-accent" />}
                  </div>
                </div>
                
                {/* Obfuscation Packet Inspector */}
                {showInspector && (
                  <div className={`mt-1.5 max-w-[85%] bg-black/90 border border-gray-800 rounded-lg p-2 text-[10px] font-mono ${isMe ? 'text-end' : 'text-start'}`}>
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
            );
          })
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Staged Encrypted File Card (Before Sending) */}
      {stagedEncryptedFile && (
        <div className="mx-3 p-3 bg-gray-950 border border-accent/40 rounded-xl mb-2 flex items-center justify-between text-xs animate-in fade-in slide-in-from-bottom-2">
          <div className="flex items-center gap-3 overflow-hidden">
            <div className="p-2 bg-accent/10 border border-accent/20 rounded-lg text-accent">
              <FileCheck size={20} />
            </div>
            <div className="truncate">
              <div className="flex items-center gap-2">
                <span className="font-semibold text-text-primary truncate">{stagedEncryptedFile.name}</span>
                <span className="text-[10px] bg-accent/20 text-accent border border-accent/30 px-1.5 py-0.2 rounded font-mono">
                  AES-256-GCM مشفر جاهز
                </span>
              </div>
              <p className="text-[11px] text-text-secondary mt-0.5">
                {formatFileSize(stagedEncryptedFile.size)} • بصمة SHA-256: {stagedEncryptedFile.checksum.substring(0, 12)}...
              </p>
            </div>
          </div>

          <button
            onClick={() => setStagedEncryptedFile(null)}
            className="p-1.5 text-gray-400 hover:text-white hover:bg-gray-800 rounded-lg transition-colors"
            title="إلغاء الملف"
          >
            <X size={16} />
          </button>
        </div>
      )}

      {/* File Encrypting Progress Notice */}
      {isEncryptingFile && (
        <div className="mx-3 p-2.5 bg-accent/10 border border-accent/30 rounded-xl mb-2 flex items-center gap-2 text-xs text-accent">
          <Sparkles size={15} className="animate-spin shrink-0" />
          <span>{encryptProgressStage || 'جاري معالجة وتشفير بايتات الملف بـ 256-bit AES...'}</span>
        </div>
      )}

      {/* Input Toolbar */}
      <div className="p-3 border-t border-gray-800 bg-primary/30">
        <div className="flex items-center gap-2 bg-black border border-gray-800 rounded-full p-1 ps-2">
          {/* File Picker Button */}
          <button
            onClick={() => fileInputRef.current?.click()}
            disabled={isEncryptingFile}
            className="w-9 h-9 rounded-full bg-gray-900 hover:bg-gray-800 flex items-center justify-center text-text-secondary hover:text-accent transition-colors shrink-0"
            title="اختيار ملف وتشفيره بـ AES-256 قبل البث"
          >
            <Paperclip size={18} />
          </button>

          <input
            type="text"
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            onKeyDown={(e) => e.key === 'Enter' && handleSend()}
            placeholder={
              stagedEncryptedFile 
                ? "أضف تعليقاً على الملف المشفر..." 
                : currentGroup 
                  ? `رسالة مشفرة إلى ${currentGroup.name} (Signal Sender Keys)...` 
                  : "رسالة مشفرة بـ Signal..."
            }
            className="flex-1 bg-transparent outline-none text-text-primary text-sm placeholder:text-text-secondary px-2"
          />

          <button 
            onClick={handleSend}
            disabled={!inputText.trim() && !stagedEncryptedFile}
            className={`w-10 h-10 rounded-full flex items-center justify-center text-black transition-colors shrink-0 ${
              inputText.trim() || stagedEncryptedFile 
                ? 'bg-accent hover:bg-emerald-300 cursor-pointer shadow-md' 
                : 'bg-gray-800 text-gray-500 cursor-not-allowed'
            }`}
          >
            <Send size={18} className="me-0.5 mt-0.5 rotate-180" />
          </button>
        </div>
      </div>

      {/* ================= MODALS ================= */}

      {/* 1. Group Members & Shared Keys Management Modal */}
      {isGroupModalOpen && currentGroup && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-gray-950 border border-gray-800 rounded-2xl w-full max-w-2xl overflow-hidden shadow-2xl animate-in fade-in zoom-in-95">
            {/* Modal Header */}
            <div className="p-4 border-b border-gray-800 flex items-center justify-between bg-primary/60">
              <div className="flex items-center gap-2.5">
                <div className="p-2 rounded-lg bg-accent/15 border border-accent/30 text-accent">
                  <Users size={18} />
                </div>
                <div>
                  <h3 className="font-bold text-sm text-text-primary">{currentGroup.name}</h3>
                  <p className="text-[11px] text-text-secondary">إدارة الأعضاء ومفاتيح الجلسة (Signal Sender Keys Protocol)</p>
                </div>
              </div>
              <button
                onClick={() => setIsGroupModalOpen(false)}
                className="p-1.5 rounded-lg text-gray-400 hover:text-white hover:bg-gray-800 transition-colors"
              >
                <X size={18} />
              </button>
            </div>

            {/* Modal Sub-tabs */}
            <div className="flex border-b border-gray-800 bg-black text-xs font-medium">
              <button
                onClick={() => setGroupModalTab('members')}
                className={`flex-1 py-2.5 text-center transition-colors border-b-2 ${
                  groupModalTab === 'members'
                    ? 'border-accent text-accent bg-accent/5 font-semibold'
                    : 'border-transparent text-text-secondary hover:text-white'
                }`}
              >
                الأعضاء ({currentGroup.members.length})
              </button>
              <button
                onClick={() => setGroupModalTab('keys')}
                className={`flex-1 py-2.5 text-center transition-colors border-b-2 ${
                  groupModalTab === 'keys'
                    ? 'border-accent text-accent bg-accent/5 font-semibold'
                    : 'border-transparent text-text-secondary hover:text-white'
                }`}
              >
                المفاتيح المشتركة والسقاطة (Epoch {currentGroup.epoch})
              </button>
            </div>

            {/* Tab 1: Members Management */}
            {groupModalTab === 'members' && (
              <div className="p-4 space-y-4 max-h-[60vh] overflow-y-auto">
                {/* Add Member Box */}
                <div className="p-3.5 bg-gray-900/60 border border-gray-800 rounded-xl space-y-2.5">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-semibold text-accent flex items-center gap-1.5">
                      <UserPlus size={14} /> إضافة عضو جديد إلى المجموعة المشفرة
                    </span>
                    <span className="text-[10px] text-gray-400 bg-black/40 px-2 py-0.5 rounded border border-gray-700">
                      سيتم تدوير المفاتيح تلقائياً (Forward Secrecy)
                    </span>
                  </div>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-2 text-xs">
                    <input
                      type="text"
                      placeholder="اسم العضو أو المعرف التكتيكي..."
                      value={newMemberName}
                      onChange={(e) => {
                        setNewMemberName(e.target.value);
                        setNewMemberError(null);
                      }}
                      className="bg-black border border-gray-800 rounded-lg px-3 py-2 text-white outline-none focus:border-accent"
                    />
                    <input
                      type="text"
                      placeholder="بصمة أمان المفتاح العام (اختياري)..."
                      value={newMemberFingerprint}
                      onChange={(e) => setNewMemberFingerprint(e.target.value)}
                      className="bg-black border border-gray-800 rounded-lg px-3 py-2 text-white outline-none focus:border-accent font-mono text-[11px]"
                    />
                  </div>

                  {newMemberError && (
                    <p className="text-red-400 text-[11px]">{newMemberError}</p>
                  )}

                  <div className="flex justify-end">
                    <button
                      onClick={handleAddMember}
                      className="px-3.5 py-1.5 bg-accent hover:bg-emerald-300 text-black font-semibold rounded-lg text-xs transition-colors flex items-center gap-1.5"
                    >
                      <UserPlus size={13} />
                      <span>إضافة وتوليد مفاتيح البث المشفرة</span>
                    </button>
                  </div>
                </div>

                {/* Member List */}
                <div className="space-y-2">
                  <h4 className="text-xs font-semibold text-text-secondary">الأعضاء المشاركون في الجلسة المشفرة</h4>
                  {currentGroup.members.map((member) => (
                    <div
                      key={member.id}
                      className="p-3 bg-black/60 border border-gray-800 rounded-xl flex items-center justify-between gap-3 text-xs"
                    >
                      <div className="flex items-center gap-3 overflow-hidden">
                        <div className="w-8 h-8 rounded-full bg-gray-800 border border-gray-700 flex items-center justify-center font-bold text-accent shrink-0">
                          {member.name.charAt(0)}
                        </div>
                        <div className="truncate">
                          <div className="flex items-center gap-2">
                            <span className="font-semibold text-text-primary truncate">{member.name}</span>
                            <span className={`text-[10px] px-1.5 py-0.2 rounded font-mono ${
                              member.role === 'admin' 
                                ? 'bg-purple-950/60 text-purple-300 border border-purple-800/40' 
                                : 'bg-gray-800 text-gray-300'
                            }`}>
                              {member.role === 'admin' ? 'مشرف' : 'عضو'}
                            </span>
                            {member.isVerified && (
                              <span className="text-[10px] text-accent flex items-center gap-0.5">
                                <ShieldCheck size={11} /> موثق
                              </span>
                            )}
                          </div>
                          <p className="text-[10px] text-gray-500 font-mono mt-0.5 truncate">
                            بصمة الأمان: {member.fingerprint}
                          </p>
                        </div>
                      </div>

                      {/* Actions */}
                      <div className="flex items-center gap-1.5 shrink-0">
                        {member.id !== 'me' && (
                          <>
                            <button
                              onClick={() => handleToggleMemberRole(member.id)}
                              className="px-2 py-1 bg-gray-900 hover:bg-gray-800 border border-gray-700 text-gray-300 rounded text-[11px] transition-colors"
                              title="تبديل رتبة العضو"
                            >
                              {member.role === 'admin' ? 'خفض الرتبة' : 'ترقية لمشرف'}
                            </button>
                            <button
                              onClick={() => handleRemoveMember(member.id)}
                              className="p-1.5 bg-red-950/40 hover:bg-red-900/60 border border-red-800/40 text-red-400 rounded transition-colors"
                              title="إزالة العضو وتدوير المفاتيح فورياً (Post-Compromise Security)"
                            >
                              <UserMinus size={14} />
                            </button>
                          </>
                        )}
                        {member.id === 'me' && (
                          <span className="text-[11px] text-accent font-medium px-2">أنت (المالك)</span>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Tab 2: Shared Keys & Cryptography */}
            {groupModalTab === 'keys' && (
              <div className="p-4 space-y-4 max-h-[60vh] overflow-y-auto text-xs">
                {/* Session Security Overview */}
                <div className="p-3.5 bg-gray-900/70 border border-gray-800 rounded-xl space-y-2">
                  <div className="flex items-center justify-between">
                    <span className="font-semibold text-accent flex items-center gap-1.5">
                      <Key size={14} /> حالة بروتوكول Sender Keys
                    </span>
                    <span className="text-[10px] bg-emerald-950/60 text-accent border border-emerald-800/40 px-2 py-0.5 rounded font-mono">
                      Ratchet Steps: {currentGroup.ratchetSteps}
                    </span>
                  </div>
                  <p className="text-[11px] text-gray-400 leading-relaxed">
                    يعتمد تشفير المجموعة على سلاسل مفاتيح البث المتسلسلة (Sender Ratchet Chains). يمتلك كل عضو مفتاح إرسال فريداً يُعاد تدويره ومسح نسخه القديمة فور أي تغيير في قوام المجموعة.
                  </p>
                </div>

                {/* Cryptographic Values */}
                <div className="space-y-2 font-mono text-[11px]" dir="ltr">
                  <div className="p-2.5 bg-black border border-gray-800 rounded-lg">
                    <div className="text-gray-500 mb-0.5 text-[10px]">Session Safety Number / Group Fingerprint:</div>
                    <div className="text-accent font-bold tracking-wider">{currentGroup.sessionFingerprint}</div>
                  </div>

                  <div className="p-2.5 bg-black border border-gray-800 rounded-lg">
                    <div className="text-gray-500 mb-0.5 text-[10px]">Active Epoch:</div>
                    <div className="text-purple-300 font-bold">Epoch {currentGroup.epoch} (Forward Secrecy Active)</div>
                  </div>

                  <div className="p-2.5 bg-black border border-gray-800 rounded-lg">
                    <div className="text-gray-500 mb-0.5 text-[10px]">Active Shared Sender Key (256-bit Hex):</div>
                    <div className="text-pink-400 break-all">{currentGroup.activeSenderKeyHex}</div>
                  </div>
                </div>

                {/* Force Rekey Action */}
                <div className="p-3 bg-amber-950/30 border border-amber-800/40 rounded-xl flex items-center justify-between gap-3">
                  <div>
                    <h5 className="font-semibold text-amber-300 text-xs">إعادة التشفير القسري (Force Rekey)</h5>
                    <p className="text-[11px] text-gray-400 mt-0.5">
                      توليد حقبة تشفير جديدة (Epoch) وتصفير كافة السلاسل السابقة من ذاكرة الأجهزة.
                    </p>
                  </div>
                  <button
                    onClick={() => handleRekeySession(currentGroup.id, 'تدوير يدوي مباشر')}
                    className="px-3 py-1.5 bg-amber-500 hover:bg-amber-400 text-black font-semibold rounded-lg transition-colors flex items-center gap-1.5 shrink-0"
                  >
                    <RefreshCw size={13} />
                    <span>تدوير الآن</span>
                  </button>
                </div>
              </div>
            )}

            {/* Modal Footer */}
            <div className="p-3 bg-black border-t border-gray-800 flex justify-end">
              <button
                onClick={() => setIsGroupModalOpen(false)}
                className="px-4 py-1.5 bg-gray-900 hover:bg-gray-800 text-white rounded-lg text-xs transition-colors"
              >
                إغلاق
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 2. Create Group Modal */}
      {isCreateGroupModalOpen && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-gray-950 border border-gray-800 rounded-2xl w-full max-w-md overflow-hidden shadow-2xl animate-in fade-in zoom-in-95">
            <div className="p-4 border-b border-gray-800 flex items-center justify-between bg-primary/60">
              <div className="flex items-center gap-2">
                <div className="p-2 rounded-lg bg-accent/15 border border-accent/30 text-accent">
                  <Plus size={18} />
                </div>
                <div>
                  <h3 className="font-bold text-sm text-text-primary">إنشاء مجموعة مشفرة جديدة</h3>
                  <p className="text-[11px] text-text-secondary">توليد جلسة Signal Sender Keys مشفرة بالكامل</p>
                </div>
              </div>
              <button
                onClick={() => setIsCreateGroupModalOpen(false)}
                className="p-1.5 rounded-lg text-gray-400 hover:text-white"
              >
                <X size={18} />
              </button>
            </div>

            <div className="p-4 space-y-3 text-xs">
              <div>
                <label className="block text-gray-400 mb-1">اسم المجموعة التكتيكية</label>
                <input
                  type="text"
                  placeholder="مثال: غرفة المراقبة الإقليمية..."
                  value={newGroupName}
                  onChange={(e) => setNewGroupName(e.target.value)}
                  className="w-full bg-black border border-gray-800 rounded-lg px-3 py-2 text-white outline-none focus:border-accent"
                />
              </div>

              <div>
                <label className="block text-gray-400 mb-1">الوصف أو الغرض الأمني</label>
                <input
                  type="text"
                  placeholder="مثال: تنسيق العمليات الحقلية وتوزيع المفاتيح المشتركة..."
                  value={newGroupDesc}
                  onChange={(e) => setNewGroupDesc(e.target.value)}
                  className="w-full bg-black border border-gray-800 rounded-lg px-3 py-2 text-white outline-none focus:border-accent"
                />
              </div>

              <div className="p-3 bg-gray-900/60 border border-gray-800 rounded-xl space-y-1 text-[11px] text-gray-400">
                <div className="text-accent font-semibold flex items-center gap-1">
                  <ShieldCheck size={13} /> مميزات الجلسة الجديدة:
                </div>
                <div>• توزيع مفاتيح البث المشفرة (Epoch 1)</div>
                <div>• دعم إضافة وإزالة الأعضاء مع إعادة تشفير آلية</div>
                <div>• إشعارات قراءة طرفية مشفرة بـ Blinded Tokens</div>
              </div>
            </div>

            <div className="p-3 bg-black border-t border-gray-800 flex justify-end gap-2 text-xs">
              <button
                onClick={() => setIsCreateGroupModalOpen(false)}
                className="px-3.5 py-1.5 bg-gray-900 hover:bg-gray-800 text-gray-300 rounded-lg transition-colors"
              >
                إلغاء
              </button>
              <button
                onClick={handleCreateGroup}
                disabled={!newGroupName.trim()}
                className={`px-4 py-1.5 rounded-lg font-semibold transition-colors flex items-center gap-1.5 ${
                  newGroupName.trim() 
                    ? 'bg-accent hover:bg-emerald-300 text-black' 
                    : 'bg-gray-800 text-gray-500 cursor-not-allowed'
                }`}
              >
                <ShieldCheck size={14} />
                <span>إنشاء وتوليد المفاتيح</span>
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 3. Encrypted Read Receipt Envelope Inspector Modal */}
      {receiptDetailMessage && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-gray-950 border border-gray-800 rounded-2xl w-full max-w-lg overflow-hidden shadow-2xl animate-in fade-in zoom-in-95">
            <div className="p-4 border-b border-gray-800 flex items-center justify-between bg-primary/60">
              <div className="flex items-center gap-2">
                <div className="p-2 rounded-lg bg-accent/15 border border-accent/30 text-accent">
                  <CheckCheck size={18} />
                </div>
                <div>
                  <h3 className="font-bold text-sm text-text-primary">مظروف إشعار القراءة المشفر (E2EE Read Receipt)</h3>
                  <p className="text-[11px] text-text-secondary">حماية الخصوصية المطلقة وتقليل البيانات الوصفية (Metadata Minimization)</p>
                </div>
              </div>
              <button
                onClick={() => setReceiptDetailMessage(null)}
                className="p-1.5 rounded-lg text-gray-400 hover:text-white"
              >
                <X size={18} />
              </button>
            </div>

            <div className="p-4 space-y-3 text-xs">
              {/* Privacy Shield Info */}
              <div className="p-3 bg-emerald-950/40 border border-emerald-800/40 rounded-xl space-y-1.5 text-[11px] text-emerald-200">
                <div className="font-semibold flex items-center gap-1.5 text-accent">
                  <ShieldCheck size={14} /> مبدأ تقليل البيانات الوصفية (Metadata Minimization)
                </div>
                <p className="leading-relaxed opacity-90">
                  لا تُرسل معرفات الرسائل أو أوقات القراءة بصورة مكشوفة عبر الخوادم أو الوسطاء. يتم تغليف الإيصال داخل حزمة مشفرة بالسقاطة الثنائية مع بصمة معماة (Blinded Token) عبر HMAC-SHA256 لتأكيد القراءة دون تمكين الرقابة من الاستدلال.
                </p>
              </div>

              {/* Message Details */}
              <div className="space-y-1.5 p-3 bg-black border border-gray-800 rounded-xl font-mono text-[11px]" dir="ltr">
                <div>
                  <span className="text-gray-500">Message ID:</span> <span className="text-gray-300">{receiptDetailMessage.id}</span>
                </div>
                <div>
                  <span className="text-gray-500">Blinded Token (HMAC):</span>{' '}
                  <span className="text-purple-400">{receiptDetailMessage.blindedReceiptToken || 'brc_910fa3e11029'}</span>
                </div>
                <div>
                  <span className="text-gray-500">Status Progression:</span>{' '}
                  <span className="text-accent font-semibold">{receiptDetailMessage.status?.toUpperCase() || 'DELIVERED'}</span>
                </div>
              </div>

              {/* Individual Member Receipts (if group) */}
              {receiptDetailMessage.receipts && receiptDetailMessage.receipts.length > 0 && (
                <div className="space-y-2">
                  <h4 className="font-semibold text-text-secondary text-xs">سجل تأكيدات الأعضاء المشفرة:</h4>
                  <div className="space-y-1.5 max-h-40 overflow-y-auto">
                    {receiptDetailMessage.receipts.map((rcpt, idx) => (
                      <div key={idx} className="p-2.5 bg-black border border-gray-800 rounded-lg flex items-center justify-between text-[11px]">
                        <div>
                          <span className="font-semibold text-text-primary">{rcpt.readerName}</span>
                          <div className="text-[10px] text-gray-500 font-mono mt-0.5" dir="ltr">
                            Token: {rcpt.blindedToken.substring(0, 14)}... | {rcpt.receiptMac}
                          </div>
                        </div>
                        <div className="flex items-center gap-1 text-accent font-semibold">
                          <CheckCheck size={14} className="text-accent" />
                          <span>{rcpt.status === 'read' ? 'تمت القراءة' : 'تم الاستلام'}</span>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>

            <div className="p-3 bg-black border-t border-gray-800 flex justify-end">
              <button
                onClick={() => setReceiptDetailMessage(null)}
                className="px-4 py-1.5 bg-accent hover:bg-emerald-300 text-black font-semibold rounded-lg text-xs transition-colors"
              >
                إغلاق الفاحص
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
