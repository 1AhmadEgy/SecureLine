import { db } from './index.ts';
import { messages, securityEvents, contacts } from './schema.ts';
import { eq, desc, or } from 'drizzle-orm';

export interface CreateMessageParams {
  senderId: number;
  recipientId: number;
  encryptedContent: string;
  nonce: string;
  isObfuscated?: boolean;
}

export async function saveMessage(params: CreateMessageParams) {
  try {
    const result = await db.insert(messages)
      .values({
        senderId: params.senderId,
        recipientId: params.recipientId,
        encryptedContent: params.encryptedContent,
        nonce: params.nonce,
        isObfuscated: params.isObfuscated ?? true,
      })
      .returning();
    return result[0];
  } catch (error) {
    console.error('Database message insert failed:', error);
    throw new Error('Failed to persist secure message payload.');
  }
}

export async function getUserMessages(userId: number, limit = 50) {
  try {
    return await db.select()
      .from(messages)
      .where(or(eq(messages.senderId, userId), eq(messages.recipientId, userId)))
      .orderBy(desc(messages.createdAt))
      .limit(limit);
  } catch (error) {
    console.error('Database query for messages failed:', error);
    return [];
  }
}

export async function logSecurityAudit(userId: number | null, eventType: string, details?: string, ip?: string) {
  try {
    await db.insert(securityEvents)
      .values({
        userId: userId || null,
        eventType,
        details: details || null,
        ipAddress: ip || null,
      });
  } catch (error) {
    console.error('Security event log failed:', error);
  }
}

export async function getRecentSecurityLogs(limit = 20) {
  try {
    return await db.select()
      .from(securityEvents)
      .orderBy(desc(securityEvents.createdAt))
      .limit(limit);
  } catch (error) {
    console.error('Failed to fetch security events:', error);
    return [];
  }
}
