import { db } from './index.ts';
import { messages, securityEvents } from './schema.ts';
import { desc, eq, or } from 'drizzle-orm';

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
      .values(params)
      .returning();
    return result[0];
  } catch (error) {
    console.error('Database message insert failed:', error);
    throw new Error('Failed to persist secure message payload.', { cause: error });
  }
}

export async function getUserMessages(userId: number, limit = 50) {
  const safeLimit = Math.min(Math.max(limit, 1), 100);
  try {
    return await db.select()
      .from(messages)
      .where(or(eq(messages.senderId, userId), eq(messages.recipientId, userId)))
      .orderBy(desc(messages.createdAt))
      .limit(safeLimit);
  } catch (error) {
    console.error('Database query for messages failed:', error);
    throw new Error('Failed to load secure messages.', { cause: error });
  }
}

export async function logSecurityAudit(
  userId: number | null,
  eventType: string,
  details?: string,
  ip?: string
) {
  try {
    await db.insert(securityEvents).values({
      userId: userId || null,
      eventType,
      details: details || null,
      ipAddress: ip || null,
    });
  } catch (error) {
    console.error('Security event log failed:', error);
  }
}

export async function getUserSecurityLogs(userId: number, limit = 30) {
  const safeLimit = Math.min(Math.max(limit, 1), 100);
  try {
    return await db.select()
      .from(securityEvents)
      .where(eq(securityEvents.userId, userId))
      .orderBy(desc(securityEvents.createdAt))
      .limit(safeLimit);
  } catch (error) {
    console.error('Failed to fetch user security events:', error);
    throw new Error('Failed to load audit logs.', { cause: error });
  }
}
