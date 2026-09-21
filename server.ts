import express from "express";
import path from "path";
import { createServer as createViteServer } from "vite";
import { requireAuth, type AuthRequest } from "./src/middleware/auth.ts";
import { getOrCreateUser, getUserByUid } from "./src/db/users.ts";
import { saveMessage, getUserMessages, logSecurityAudit, getRecentSecurityLogs } from "./src/db/messages.ts";

async function startServer() {
  const app = express();
  const PORT = 3000;

  app.use(express.json());

  // API routes FIRST
  app.get("/api/health", (_req, res) => {
    res.json({ status: "ok", service: "SecureLine Backend", database: "Cloud SQL" });
  });

  // Synchronize authenticated user into Cloud SQL
  app.post("/api/auth/sync", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user || !req.user.uid) {
        return res.status(401).json({ error: "Invalid user token" });
      }
      const user = await getOrCreateUser(
        req.user.uid,
        req.user.email || "anonymous@secureline.internal",
        req.user.name || undefined
      );
      return res.json({ success: true, user });
    } catch (error: any) {
      console.error("Failed to sync user:", error);
      return res.status(500).json({ error: "Failed to synchronize user profile" });
    }
  });

  // Fetch current user profile
  app.get("/api/user/profile", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user || !req.user.uid) {
        return res.status(401).json({ error: "Invalid user token" });
      }
      const user = await getUserByUid(req.user.uid);
      if (!user) {
        return res.status(404).json({ error: "User not found in database" });
      }
      return res.json(user);
    } catch (error: any) {
      console.error("Failed to fetch user profile:", error);
      return res.status(500).json({ error: "Failed to fetch user profile" });
    }
  });

  // Store encrypted message payload with audit trace
  app.post("/api/messages/send", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user || !req.user.uid) {
        return res.status(401).json({ error: "Invalid user token" });
      }
      const { recipientId, encryptedContent, nonce, isObfuscated } = req.body;
      if (!encryptedContent || !nonce) {
        return res.status(400).json({ error: "encryptedContent and nonce are required" });
      }
      const sender = await getUserByUid(req.user.uid);
      if (!sender) {
        return res.status(404).json({ error: "Sender profile not found" });
      }

      const msg = await saveMessage({
        senderId: sender.id,
        recipientId: recipientId || sender.id,
        encryptedContent,
        nonce,
        isObfuscated: isObfuscated ?? true,
      });

      // Log security event
      const clientIp = (req.headers["x-forwarded-for"] as string) || req.socket.remoteAddress;
      await logSecurityAudit(sender.id, "MESSAGE_SENT", `Payload encrypted with nonce: ${nonce.slice(0, 8)}...`, clientIp);

      return res.json({ success: true, message: msg });
    } catch (error: any) {
      console.error("Failed to store message:", error);
      return res.status(500).json({ error: "Internal error processing secure message" });
    }
  });

  // Fetch security audit events
  app.get("/api/security/audit", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user || !req.user.uid) {
        return res.status(401).json({ error: "Invalid user token" });
      }
      const logs = await getRecentSecurityLogs(30);
      return res.json({ logs });
    } catch (error: any) {
      console.error("Failed to retrieve security events:", error);
      return res.status(500).json({ error: "Failed to load audit logs" });
    }
  });

  // Vite middleware for development
  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (_req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://0.0.0.0:${PORT}`);
  });
}

startServer();
