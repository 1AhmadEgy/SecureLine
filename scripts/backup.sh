#!/bin/bash
BACKUP_DIR="./backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
mkdir -p "$BACKUP_DIR"
cd server
docker-compose exec postgres pg_dump -U secureline secureline_db > "../$BACKUP_DIR/db_$TIMESTAMP.sql"
echo "Backup saved to $BACKUP_DIR/db_$TIMESTAMP.sql"
