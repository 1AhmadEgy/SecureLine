# SecureLine API Documentation

## Endpoints

### POST /v1/messages
Send an encrypted message

Headers:
- Authorization: Bearer <token>

Body:
{
  "recipient_id": "string",
  "encrypted_body": "base64",
  "timestamp": 1234567890
}

Response:
{
  "status": "sent",
  "message_id": "uuid"
}

### GET /v1/messages
Fetch pending messages

Headers:
- Authorization: Bearer <token>

Response:
{
  "messages": [
    {
      "id": "uuid",
      "sender_id": "string",
      "encrypted_body": "base64",
      "timestamp": 1234567890
    }
  ]
}

### POST /v1/keys
Upload public key

Headers:
- Authorization: Bearer <token>

Body:
{
  "public_key": "base64"
}

Response:
{
  "status": "stored"
}
