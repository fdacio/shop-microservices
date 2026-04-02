#!/bin/bash
set -e

MODULO=$1
HOST="100.27.189.94"
USER="ubuntu"
SSH_KEY="/home/fdacio/.ssh/key-rsa-ssh-shop-app-server.pem"

ssh -i "$SSH_KEY" $USER@$HOST "docker logs $MODULO"