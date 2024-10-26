#!/bin/bash
HOST="138.197.210.74"
USER="moderar"
PREFIX="*"
#Services
#shop-auth-api
#shop-gateway-api
#shop-product-api
#shop-order-api
#shop-customers-api


if [ $# -gt 0 ]; then
    PREFIX=$1
fi
echo "$PREFIX";

# shellcheck disable=SC2046
scp $(find ./shop* -name "$PREFIX-0.0.1-SNAPSHOT.jar" && ls remote-deploy.sh) $USER@$HOST:/home/moderar/sistemas/shop &&
ssh $USER@$HOST chmod a+x /home/moderar/sistemas/shop/remote-deploy.sh &&
ssh $USER@$HOST  /home/moderar/sistemas/shop/remote-deploy.sh
