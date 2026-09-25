import express from "express";
import helmet from "helmet";
import rateLimit from "express-rate-limit";
import path from "path";
import { createServer as createViteServer } from "vite";
import { requireAuth, type AuthRequest } from "./src/middleware/auth.ts";
import { getOrCreateUser, getUserById, getUserByUid } from "./src/db/users.ts";
import { saveMessage, logSecurityAudit, getUserSecurityLogs } from "./src/db/messages.ts";

async function startServer() {
  const app = express();
  const PORT = Number(process.env.PORT || 3000);

  app.disable("x-powered-by");
  app.set("trust proxy", process.env.TRUST_PROXY === "true" ? 1 : false);
  app.use(helmet());
  app.use(express.json({ limit: "256kb" }));

  const apiLimiter = rateLimit({
    windowMs: 60_000,
    limit: 120,
    standardHeaders: "draft-7",
    legacyHeaders: false,
    message: { error: "Too many requests. Please try again later." },
  });
  app.use("/api", apiLimiter);

  app.get("/api/health", (_req, res) => {
    res.json({ status: "ok", service: "SecureLine Backend" });
  });

  app.post("/api/auth/sync", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user?.uid) return res.status(401).json({ error: "Invalid user token" });
      const user = await getOrCreateUser(
        req.user.uid,
        req.user.email || "anonymous@secureline.internal",
        req.user.name || undefined
      );
      return res.json({ success: true, user });
    } catch (error) {
      console.error("Failed to sync user:", error);
      return res.status(500).json({ error: "Failed to synchronize user profile" });
    }
  });

  app.get("/api/user/profile", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user?.uid) return res.status(401).json({ error: "Invalid user token" });
      const user = await getUserByUid(req.user.uid);
      if (!user) return res.status(404).json({ error: "User not found in database" });
      return res.json(user);
    } catch (error) {
      console.error("Failed to fetch user profile:", error);
      return res.status(500).json({ error: "Failed to fetch user profile" });
    }
  });

  app.post("/api/messages/send", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user?.uid) return res.status(401).json({ error: "Invalid user token" });

      const { recipientId, encryptedContent, nonce, isObfuscated } = req.body ?? {};
      const numericRecipientId = Number(recipientId);

      if (
        !Number.isInteger(numericRecipientId) ||
        numericRecipientId <= 0 ||
        typeof encryptedContent !== "string" ||
        encryptedContent.length === 0 ||
        encryptedContent.length > 200_000 ||
        typeof nonce !== "string" ||
        nonce.length === 0 ||
        nonce.length > 256
      ) {
        return res.status(400).json({ error: "Invalid message payload" });
      }

      const sender = await getUserByUid(req.user.uid);
      if (!sender) return res.status(404).json({ error: "Sender profile not found" });

      const recipient = await getUserById(numericRecipientId);
      if (!recipient) return res.status(404).json({ error: "Recipient not found" });

      const msg = await saveMessage({
        senderId: sender.id,
        recipientId: recipient.id,
        encryptedContent,
        nonce,
        isObfuscated: isObfuscated === true,
      });

      const forwardedFor = req.headers["x-forwarded-for"];
      const clientIp = process.env.TRUST_PROXY === "true"
        ? (Array.isArray(forwardedFor) ? forwardedFor[0] : forwardedFor?.split(",")[0]?.trim())
        : req.socket.remoteAddress;

      await logSecurityAudit(sender.id, "MESSAGE_SENT", "Encrypted message accepted by relay", clientIp);

      return res.status(201).json({
        success: true,
        message: { id: msg.id, recipientId: msg.recipientId, createdAt: msg.createdAt },
      });
    } catch (error) {
      console.error("Failed to store message:", error);
      return res.status(500).json({ error: "Internal error processing secure message" });
    }
  });

  app.get("/api/security/audit", requireAuth, async (req: AuthRequest, res) => {
    try {
      if (!req.user?.uid) return res.status(401).json({ error: "Invalid user token" });
      const user = await getUserByUid(req.user.uid);
      if (!user) return res.status(404).json({ error: "User not found in database" });
      const logs = await getUserSecurityLogs(user.id, 30);
      return res.json({ logs });
    } catch (error) {
      console.error("Failed to retrieve security events:", error);
      return res.status(500).json({ error: "Failed to load audit logs" });
    }
  });

  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({ server: { middlewareMode: true }, appType: "spa" });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (_req, res) => res.sendFile(path.join(distPath, "index.html")));
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`Server running on http://0.0.0.0:${PORT}`);
  });
}

startServer().catch((error) => {
  console.error("Fatal startup error:", error);
  process.exitCode = 1;
});
