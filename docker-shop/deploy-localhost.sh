#!/bin/bash
SLEEP=40
docker-compose down --remove-orphans
docker-compose build
docker-compose up -d
echo "Containers are UP. Wait for the deployment to end ...";
sleep $SLEEP
docker ps --format "table {{.Image}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
echo "Deploy finished!!!";
