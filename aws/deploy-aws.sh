#!/bin/bash
#ssh -i "key-rsa-ssh-shop-app-server.pem" ubuntu@ec2-18-215-149-125.compute-1.amazonaws.com
#HOST="c2-18-215-149-125.compute-1.amazonaws.com"
HOST="18.215.149.125"
USER="ubuntu"
# shellcheck disable=SC2046

ssh -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" $USER@$HOST "mkdir -p shop && cd /home/ubuntu/shop && mkdir -p auth/target gateway/target order/target product/target customer/target" &&

scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-auth-api/Dockerfile $USER@$HOST:/home/ubuntu/shop/auth &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-customer-api/Dockerfile $USER@$HOST:/home/ubuntu/shop/customer &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-gateway-api/Dockerfile $USER@$HOST:/home/ubuntu/shop/gateway &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-order-api/Dockerfile $USER@$HOST:/home/ubuntu/shop/order &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-product-api/Dockerfile $USER@$HOST:/home/ubuntu/shop/product &&

scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" docker-compose.yaml $USER@$HOST:/home/ubuntu/shop &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" remote-deploy-aws.sh $USER@$HOST:/home/ubuntu/shop &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-auth-api/target/shop-auth-api-0.0.1-SNAPSHOT.jar $USER@$HOST:/home/ubuntu/shop/auth/target &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-customer-api/target/shop-customer-api-0.0.1-SNAPSHOT.jar $USER@$HOST:/home/ubuntu/shop/customer/target &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-gateway-api/target/shop-gateway-api-0.0.1-SNAPSHOT.jar $USER@$HOST:/home/ubuntu/shop/gateway/target &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-order-api/target/shop-order-api-0.0.1-SNAPSHOT.jar $USER@$HOST:/home/ubuntu/shop/order/target &&
scp -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" ../shop-product-api/target/shop-product-api-0.0.1-SNAPSHOT.jar $USER@$HOST:/home/ubuntu/shop/product/target &&

ssh -i "/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem" $USER@$HOST "chmod a+x /home/ubuntu/shop/remote-deploy-aws.sh && /home/ubuntu/shop/remote-deploy-aws.sh"
