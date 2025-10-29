#!/bin/bash

# shellcheck disable=SC2164
cd /home/moderar/sistemas/shop
cp -f ./shop-auth*.jar ./auth
cp -f ./shop-gateway*.jar ./gateway
cp -f ./shop-order*.jar ./order
cp -f ./shop-product*.jar ./product
cp -f ./shop-customer*.jar ./customer

SLEEP=40
if [ $# -eq 0 ]; then
docker-compose down --remove-orphans
SLEEP=80
fi
docker-compose build
docker-compose up -d
echo "Containers are UP. Wait for the deployment to end ...";
sleep $SLEEP
docker ps --format "table {{.Image}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
echo "Deploy finished!!!";
