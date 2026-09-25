// Web Authentication API (WebAuthn / Passkeys) Implementation for SecureLine

export interface WebAuthnStatus {
  supported: boolean;
  hasPlatformAuthenticator: boolean;
  isRegistered: boolean;
  registeredUsername?: string;
  registeredDate?: string;
}

const WEBAUTHN_CREDENTIAL_KEY = 'secureline_webauthn_credential_id';
const WEBAUTHN_USER_KEY = 'secureline_webauthn_username';
const WEBAUTHN_REG_DATE_KEY = 'secureline_webauthn_date';

// Buffer conversion helpers
function bufferToBase64(buffer: ArrayBuffer): string {
  const bytes = new Uint8Array(buffer);
  let binary = '';
  for (let i = 0; i < bytes.byteLength; i++) {
    binary += String.fromCharCode(bytes[i]);
  }
  return btoa(binary);
}

function base64ToBuffer(base64: string): ArrayBuffer {
  const binary = atob(base64);
  const bytes = new Uint8Array(binary.length);
  for (let i = 0; i < binary.length; i++) {
    bytes[i] = binary.charCodeAt(i);
  }
  return bytes.buffer;
}

/**
 * Checks if WebAuthn is supported in the current browser context.
 */
export function isWebAuthnSupported(): boolean {
  return typeof window !== 'undefined' && 
         window.PublicKeyCredential !== undefined &&
         typeof window.PublicKeyCredential === 'function';
}

/**
 * Checks if a biometric platform authenticator (Touch ID, Face ID, Windows Hello, Android Biometrics) is available.
 */
export async function isPlatformAuthenticatorAvailable(): Promise<boolean> {
  if (!isWebAuthnSupported()) return false;
  try {
    if (PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable) {
      return await PublicKeyCredential.isUserVerifyingPlatformAuthenticatorAvailable();
    }
    return false;
  } catch {
    return false;
  }
}

/**
 * Gets the current WebAuthn enrollment status.
 */
export async function getWebAuthnStatus(): Promise<WebAuthnStatus> {
  const supported = isWebAuthnSupported();
  const hasPlatform = supported ? await isPlatformAuthenticatorAvailable() : false;
  const credentialId = typeof window !== 'undefined' ? localStorage.getItem(WEBAUTHN_CREDENTIAL_KEY) : null;
  const username = typeof window !== 'undefined' ? localStorage.getItem(WEBAUTHN_USER_KEY) : null;
  const regDate = typeof window !== 'undefined' ? localStorage.getItem(WEBAUTHN_REG_DATE_KEY) : null;

  return {
    supported,
    hasPlatformAuthenticator: hasPlatform,
    isRegistered: !!credentialId,
    registeredUsername: username || undefined,
    registeredDate: regDate || undefined,
  };
}

/**
 * Registers a biometric passkey on the device using WebAuthn.
 */
export async function registerBiometricPasskey(
  username: string = 'SecureLine Operator'
): Promise<{ success: boolean; error?: string }> {
  if (!isWebAuthnSupported()) {
    return { success: false, error: 'متصفحك لا يدعم واجهة برمجة تطبيقات المصادقة (Web Authentication API).' };
  }

  try {
    // Generate random 32-byte challenge
    const challenge = new Uint8Array(32);
    window.crypto.getRandomValues(challenge);

    // Random user id
    const userId = new Uint8Array(16);
    window.crypto.getRandomValues(userId);

    const publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions = {
      challenge,
      rp: {
        name: 'SecureLine Encrypted Vault',
        id: window.location.hostname === 'localhost' ? 'localhost' : window.location.hostname,
      },
      user: {
        id: userId,
        name: username.toLowerCase().replace(/\s+/g, '_'),
        displayName: username,
      },
      pubKeyCredParams: [
        { alg: -7, type: 'public-key' },  // ES256
        { alg: -257, type: 'public-key' }, // RS256
        { alg: -8, type: 'public-key' },   // EdDSA
      ],
      authenticatorSelection: {
        authenticatorAttachment: 'platform', // Enforce biometric / platform sensor (Face ID / Fingerprint / Windows Hello)
        userVerification: 'required',
        residentKey: 'preferred',
      },
      timeout: 60000,
      attestation: 'none',
    };

    const credential = (await navigator.credentials.create({
      publicKey: publicKeyCredentialCreationOptions,
    })) as PublicKeyCredential | null;

    if (!credential) {
      return { success: false, error: 'تم إلغاء عملية التسجيل البيومتري أو فشلت.' };
    }

    const credIdBase64 = bufferToBase64(credential.rawId);
    localStorage.setItem(WEBAUTHN_CREDENTIAL_KEY, credIdBase64);
    localStorage.setItem(WEBAUTHN_USER_KEY, username);
    localStorage.setItem(WEBAUTHN_REG_DATE_KEY, new Date().toISOString());

    return { success: true };
  } catch (err: unknown) {
    const error = err as Error;
    console.warn('WebAuthn Registration notice:', error);
    
    // User cancelled or security policy restrictions
    if (error.name === 'NotAllowedError') {
      return { success: false, error: 'تم رفض الإذن أو إلغاء مسح البصمة / Face ID من قبل المستخدم.' };
    }
    if (error.name === 'InvalidStateError') {
      return { success: false, error: 'هذا الجهاز مسجل بالفعل في نظام المصادقة البيومترية.' };
    }
    return { success: false, error: error.message || 'حدث خطأ غير متوقع أثناء الاتصال بمستشعر البصمة.' };
  }
}

/**
 * Authenticates the user via WebAuthn biometric prompt.
 */
export async function authenticateWithBiometrics(): Promise<{ success: boolean; error?: string; isSimulated?: boolean }> {
  if (!isWebAuthnSupported()) {
    return { 
      success: false, 
      error: 'المتصفح لا يدعم المصادقة البيومترية المباشرة (WebAuthn).' 
    };
  }

  const storedCredId = localStorage.getItem(WEBAUTHN_CREDENTIAL_KEY);

  try {
    const challenge = new Uint8Array(32);
    window.crypto.getRandomValues(challenge);

    const publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions = {
      challenge,
      rpId: window.location.hostname === 'localhost' ? 'localhost' : window.location.hostname,
      userVerification: 'required',
      timeout: 60000,
    };

    if (storedCredId) {
      try {
        const rawId = base64ToBuffer(storedCredId);
        publicKeyCredentialRequestOptions.allowCredentials = [
          {
            id: rawId,
            type: 'public-key',
            transports: ['internal'],
          },
        ];
      } catch (e) {
        console.warn('Could not parse stored credential ID, proceeding with general assert:', e);
      }
    }

    const assertion = (await navigator.credentials.get({
      publicKey: publicKeyCredentialRequestOptions,
    })) as PublicKeyCredential | null;

    if (assertion && assertion.id) {
      return { success: true };
    }

    return { success: false, error: 'فشلت عملية التحقق البيومتري.' };
  } catch (err: unknown) {
    const error = err as Error;
    console.warn('WebAuthn Authentication notice:', error);

    if (error.name === 'NotAllowedError') {
      return { success: false, error: 'تم إلغاء التحقق البيومتري أو انتهاء مهلة المستشعر.' };
    }

    // In iframe or sandboxed environments without direct biometric sensor access:
    // If not allowed due to iframe permissions policy, we gracefully inform the caller.
    return { 
      success: false, 
      error: error.message || 'تعذر التواصل مع مستشعر الجهاز البيومتري.' 
    };
  }
}

/**
 * Removes biometric credential enrollment.
 */
export function removeBiometricPasskey(): void {
  localStorage.removeItem(WEBAUTHN_CREDENTIAL_KEY);
  localStorage.removeItem(WEBAUTHN_USER_KEY);
  localStorage.removeItem(WEBAUTHN_REG_DATE_KEY);
}
