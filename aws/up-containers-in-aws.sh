#!/bin/bash
set -e

APP="${1:-all}"
SLEEP=${2:-60}
APP_PATH="/home/$USER/shop"

cd "$APP_PATH"

build_image() {
      local module="$1"
      local image_name
      case "$module" in
          auth)     image_name="daciosoftware/shop-auth-api:latest" ;;
          customer) image_name="daciosoftware/shop-customer-api:latest" ;;
          order)    image_name="daciosoftware/shop-order-api:latest" ;;
          product)  image_name="daciosoftware/shop-product-api:latest" ;;
          gateway)  image_name="daciosoftware/shop-gateway-api:latest" ;;
      esac
    # Docker buildar a imagem (só para os módulos que têm Dockerfile)
    local dir="$APP_PATH/$module";
    if [[ -n "$image_name" ]]; then
        echo "🐳 Image building: $image_name"
        docker build -t "$image_name" "$dir"
        echo "✅ Image $image_name created with success"
    fi
}

if [ "$APP" = "all" ]; then
  echo "🧹 Stopping old containers..."
  docker compose down --remove-orphans
  echo "✅ Prune system docker..."
  docker system prune -a --volumes -f
  echo "🚀 Starting all containers..."
  echo "🐳 Building all images..."
  for m in auth customer product order gateway; do
     build_image "$m"
  done
  docker compose up -d
else
  echo "🚀 Starting container: $APP"
  docker compose down --remove-orphans "$APP"
  echo "🐳 Building $APP image..."
  build_image "$APP"
  docker compose up -d "$APP"
fi

echo
echo "✅ Containers are UP. Waiting $SLEEP seconds for stabilization..."
sleep "$SLEEP"
echo
docker ps --format "table {{.Image}}\t{{.Names}}\t{{.Status}}\t{{.Ports}}"
echo
echo "🎯 Deploy finished!"
