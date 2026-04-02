#!/bin/bash
set -e

source config.sh

MODULO=$1

ssh -i "$SSH_KEY" "$USER"@"$HOST" "docker logs $MODULO"