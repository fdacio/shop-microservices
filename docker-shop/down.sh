#!/bin/bash
set -e
echo "✅ DOWN containers"
docker-compose down --remove-orphans
echo "✅ Containers are DOWN";
