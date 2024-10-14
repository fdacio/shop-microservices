#!/bin/sh
docker ps | tail -n+2 | cut -c16-40,85-110
