#!/bin/bash

# shellcheck disable=SC2164
cd /home/moderar/sistemas/shop
cp -f ./shop-auth*.jar ./auth
cp -f ./shop-gateway*.jar ./gateway
cp -f ./shop-shopping*.jar ./shoopping
cp -f ./shop-product*.jar ./product
cp -f ./shop-customer*.jar ./customer
#docker-compose down
docker-compose build
docker-compose up -d
docker-compose ps | grep "java" | awk '{print $1, $5, $6}' | tr ' ' '\t'
echo "Fim do deploy";
