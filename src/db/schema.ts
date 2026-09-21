import { relations } from 'drizzle-orm';
import { pgTable, serial, text, timestamp, boolean, integer } from 'drizzle-orm/pg-core';

// Define the 'users' table - uid is the Firebase Auth UID
export const users = pgTable('users', {
  id: serial('id').primaryKey(),
  uid: text('uid').notNull().unique(),
  email: text('email').notNull(),
  displayName: text('display_name'),
  avatarUrl: text('avatar_url'),
  createdAt: timestamp('created_at').defaultNow(),
});

// Contacts table with fingerprint and public key
export const contacts = pgTable('contacts', {
  id: serial('id').primaryKey(),
  userId: integer('user_id')
    .references(() => users.id, { onDelete: 'cascade' })
    .notNull(),
  contactName: text('contact_name').notNull(),
  publicKey: text('public_key').notNull(),
  fingerprint: text('fingerprint').notNull(),
  isVerified: boolean('is_verified').default(false).notNull(),
  createdAt: timestamp('created_at').defaultNow(),
});

// Secure messages metadata and encrypted payload store
export const messages = pgTable('messages', {
  id: serial('id').primaryKey(),
  senderId: integer('sender_id')
    .references(() => users.id, { onDelete: 'cascade' })
    .notNull(),
  recipientId: integer('recipient_id')
    .references(() => users.id, { onDelete: 'cascade' })
    .notNull(),
  encryptedContent: text('encrypted_content').notNull(),
  nonce: text('nonce').notNull(),
  isObfuscated: boolean('is_obfuscated').default(true).notNull(),
  createdAt: timestamp('created_at').defaultNow(),
});

// Security events and audit trail
export const securityEvents = pgTable('security_events', {
  id: serial('id').primaryKey(),
  userId: integer('user_id').references(() => users.id, { onDelete: 'cascade' }),
  eventType: text('event_type').notNull(),
  details: text('details'),
  ipAddress: text('ip_address'),
  createdAt: timestamp('created_at').defaultNow(),
});

// Relations
export const usersRelations = relations(users, ({ many }) => ({
  contacts: many(contacts),
  sentMessages: many(messages, { relationName: 'sentMessages' }),
  receivedMessages: many(messages, { relationName: 'receivedMessages' }),
  securityEvents: many(securityEvents),
}));

export const contactsRelations = relations(contacts, ({ one }) => ({
  user: one(users, {
    fields: [contacts.userId],
    references: [users.id],
  }),
}));

export const messagesRelations = relations(messages, ({ one }) => ({
  sender: one(users, {
    fields: [messages.senderId],
    references: [users.id],
    relationName: 'sentMessages',
  }),
  recipient: one(users, {
    fields: [messages.recipientId],
    references: [users.id],
    relationName: 'receivedMessages',
  }),
}));

export const securityEventsRelations = relations(securityEvents, ({ one }) => ({
  user: one(users, {
    fields: [securityEvents.userId],
    references: [users.id],
  }),
}));
