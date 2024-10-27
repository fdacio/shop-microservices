#!/bin/bash

# shellcheck disable=SC2164
cd /home/moderar/sistemas/shop
cp -f ./shop-auth*.jar ./auth
cp -f ./shop-gateway*.jar ./gateway
cp -f ./shop-order*.jar ./order
cp -f ./shop-product*.jar ./product
cp -f ./shop-customer*.jar ./customer

SLEEP=60
if [ $# -eq 0 ]; then
docker-compose down --remove-orphans
SLEEP=80
fi
docker-compose build
docker-compose up -d
echo "Up containers. Wait the end of deploy ...";
sleep $SLEEP
docker-compose ps | grep "java" | awk '{print $1, $5, $6}' | tr ' ' '\t'
echo "Deploy complete!!!";
