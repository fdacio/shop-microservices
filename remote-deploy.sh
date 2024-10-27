#!/bin/bash

# shellcheck disable=SC2164
cd /home/moderar/sistemas/shop
cp -f ./shop-auth*.jar ./auth
cp -f ./shop-gateway*.jar ./gateway
cp -f ./shop-order*.jar ./order
cp -f ./shop-product*.jar ./product
cp -f ./shop-customer*.jar ./customer

docker-compose down --remove-orphans
docker-compose build
docker-compose up -d
echo "up containers";
sleep 50
docker-compose ps | grep "java" | awk '{print $1, $5, $6}' | tr ' ' '\t'
echo "Fim do deploy";
