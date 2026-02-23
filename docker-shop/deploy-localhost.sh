#!/bin/bash
set -e
cd ../shop-models && mvn --offline --update-snapshots -DskipTests=true clean install
cd ../shop-exceptions && mvn --offline --update-snapshots -DskipTests=true clean install
cd ../shop-auth-keys && mvn --offline --update-snapshots -DskipTests=true clean install
cd ../shop-auth-api && mvn --offline --update-snapshots -DskipTests=true clean install
cd ../shop-customer-api && mvn --offline --update-snapshots -DskipTests=true clean install
cd ../shop-order-api && mvn --offline --update-snapshots -DskipTests=true clean install
cd ../shop-product-api && mvn --offline --update-snapshots -DskipTests=true clean install
cd ../shop-gateway-api && mvn --offline --update-snapshots -DskipTests=true clean install

cd ../shop-auth-api && mvn jib:dockerBuild
cd ../shop-customer-api && mvn jib:dockerBuild
cd ../shop-gateway-api && mvn jib:dockerBuild
cd ../shop-order-api && mvn jib:dockerBuild
cd ../shop-product-api && mvn jib:dockerBuild

# shellcheck disable=SC2164
cd ../docker-shop

SLEEP=40
docker-compose down --remove-orphans
#docker-compose build
echo "UP containers"
docker-compose up -d
echo "Containers are UP. Wait for the deployment to end ...";
sleep $SLEEP
docker ps --format "table {{.Image}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
echo "Deploy finished!!!";
