#!/bin/bash
echo "Rotating encryption keys..."
cd server
docker-compose exec signal-server java -jar /app/server.jar rotate-keys
echo "Keys rotated successfully"
