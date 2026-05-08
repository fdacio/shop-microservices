#!/bin/bash
set -euo pipefail

OPTIONS="${1:-""}"
SLEEP=30

if [ "$OPTIONS" = "--down" ]; then
  docker-compose down --remove-orphans
  SLEEP=60
fi
echo "✅ UP containers"
docker-compose up -d
echo "✅ Containers are UP. Wait for the deployment to end ...";
sleep $SLEEP
docker ps --format "table {{.Image}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
echo "✅ UP finished!!!";
