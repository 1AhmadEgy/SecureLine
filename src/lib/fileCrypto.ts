// File Encryption Service using Web Crypto API (AES-256-GCM)

export interface EncryptedFileResult {
  id: string;
  name: string;
  size: number;
  type: string;
  cipherAlgorithm: 'AES-256-GCM';
  iv: string; // 12 bytes in hex
  checksum: string; // SHA-256 digest in hex
  keyRawHex: string; // 256-bit AES key in hex
  encryptedSize: number;
  encryptedBlob: Blob;
  encryptedBlobUrl: string;
  originalPreviewUrl?: string;
  encryptedAt: string;
  status: 'encrypted';
}

// Convert ArrayBuffer to Hex String
export function bufferToHex(buffer: ArrayBuffer | Uint8Array): string {
  const bytes = buffer instanceof Uint8Array ? buffer : new Uint8Array(buffer);
  return Array.from(bytes)
    .map(b => b.toString(16).padStart(2, '0'))
    .join('');
}

// Convert Hex String to Uint8Array
export function hexToUint8Array(hexString: string): Uint8Array {
  const cleanHex = hexString.replace(/\s+/g, '');
  const bytes = new Uint8Array(cleanHex.length / 2);
  for (let i = 0; i < cleanHex.length; i += 2) {
    bytes[i / 2] = parseInt(cleanHex.substring(i, i + 2), 16);
  }
  return bytes;
}

// Format bytes into readable string (e.g., 2.4 MB)
export function formatFileSize(bytes: number): string {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(2))} ${sizes[i]}`;
}

/**
 * Encrypts any local file in-memory using hardware-accelerated AES-256-GCM.
 */
export async function encryptFileWithAES256(
  file: File,
  onProgress?: (stage: string) => void
): Promise<EncryptedFileResult> {
  onProgress?.('قراءة بايتات الملف من القرص المحلي...');
  const fileBuffer = await file.arrayBuffer();

  onProgress?.('حساب بصمة النزاهة الرياضية (SHA-256 Checksum)...');
  const hashBuffer = await window.crypto.subtle.digest('SHA-256', fileBuffer);
  const checksum = bufferToHex(hashBuffer);

  onProgress?.('توليد مفتاح عشوائي مشفر 256-بت (AES-256 Key)...');
  const key = await window.crypto.subtle.generateKey(
    {
      name: 'AES-GCM',
      length: 256,
    },
    true,
    ['encrypt', 'decrypt']
  );

  // Generate 12-byte IV (Standard for AES-GCM)
  const iv = new Uint8Array(12);
  window.crypto.getRandomValues(iv);

  onProgress?.('تشفير محتوى الملف بـ AES-256-GCM وطبقة التوثيق MAC...');
  const ciphertextBuffer = await window.crypto.subtle.encrypt(
    {
      name: 'AES-GCM',
      iv,
    },
    key,
    fileBuffer
  );

  // Export raw key to hex for transmission / storage
  const rawKey = await window.crypto.subtle.exportKey('raw', key);
  const keyRawHex = bufferToHex(rawKey);
  const ivHex = bufferToHex(iv);

  // Create Encrypted Blob
  const encryptedBlob = new Blob([ciphertextBuffer], { type: 'application/octet-stream' });
  const encryptedBlobUrl = URL.createObjectURL(encryptedBlob);

  let originalPreviewUrl: string | undefined;
  if (file.type.startsWith('image/')) {
    originalPreviewUrl = URL.createObjectURL(file);
  }

  onProgress?.('اكتمل التشفير بنجاح، الملف مؤمن وجاهز للبث.');

  return {
    id: `file_${Date.now()}_${Math.random().toString(36).substring(2, 7)}`,
    name: file.name,
    size: file.size,
    type: file.type || 'application/octet-stream',
    cipherAlgorithm: 'AES-256-GCM',
    iv: ivHex,
    checksum,
    keyRawHex,
    encryptedSize: ciphertextBuffer.byteLength,
    encryptedBlob,
    encryptedBlobUrl,
    originalPreviewUrl,
    encryptedAt: new Date().toISOString(),
    status: 'encrypted',
  };
}

/**
 * Decrypts an AES-256-GCM encrypted file and prompts download or returns a clean Blob URL.
 */
export async function decryptAndDownloadFile(
  encryptedBlob: Blob | ArrayBuffer,
  ivHex: string,
  keyRawHex: string,
  fileName: string,
  mimeType: string = 'application/octet-stream'
): Promise<string> {
  const iv = hexToUint8Array(ivHex);
  const keyBytes = hexToUint8Array(keyRawHex);

  const key = await window.crypto.subtle.importKey(
    'raw',
    keyBytes,
    { name: 'AES-GCM' },
    false,
    ['decrypt']
  );

  const ciphertextBuffer = encryptedBlob instanceof Blob 
    ? await encryptedBlob.arrayBuffer() 
    : encryptedBlob;

  const decryptedBuffer = await window.crypto.subtle.decrypt(
    {
      name: 'AES-GCM',
      iv,
    },
    key,
    ciphertextBuffer
  );

  const decryptedBlob = new Blob([decryptedBuffer], { type: mimeType });
  const downloadUrl = URL.createObjectURL(decryptedBlob);

  // Auto trigger download
  const link = document.createElement('a');
  link.href = downloadUrl;
  link.download = fileName;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);

  return downloadUrl;
}
