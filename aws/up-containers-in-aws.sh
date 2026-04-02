#!/bin/bash
set -e

APP="${1:-all}"
SLEEP=${2:-60}
APP_PATH="/home/$USER/shop"

cd "$APP_PATH"

if [ "$APP" = "all" ]; then
  echo "🧹 Stopping old containers..."
  docker compose down --remove-orphans
  echo "✅ Prune system docker..."
  docker system prune -a --volumes -f
  echo "⚙️ Building all images..."
  docker compose build
  echo "🚀 Starting all containers..."
  docker compose up -d
else
  echo "🚀 Starting container: $APP"
  docker compose build "$APP"
  docker compose down "$APP"
  docker compose up -d "$APP"
fi

echo
echo "✅ Containers are UP. Waiting $SLEEP seconds for stabilization..."
sleep "$SLEEP"
echo
docker ps --format "table {{.Image}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
echo
echo "🎯 Deploy finished!"
