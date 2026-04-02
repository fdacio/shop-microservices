#!/bin/bash
set -e

source config.sh

ssh -i "$SSH_KEY" "$USER"@"$HOST" "docker ps --format \"table {{.Image}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}\""