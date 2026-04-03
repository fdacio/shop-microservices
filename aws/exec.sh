#!/bin/bash
set -e

source config.sh
CONTAINER="$1"
COMMAND="$2"
ssh -i "$SSH_KEY" "$USER"@"$HOST" "docker exec -it $CONTAINER $COMMAND"