#!/bin/bash
set -e

<<<<<<< HEAD
HOST="52.90.98.77"
=======
HOST="98.84.113.189"
>>>>>>> 27f2e65df940ad2ed29cf4ab91646360095ee6f4
USER="ubuntu"
PATH_APPS="/home/ubuntu/shop"
SSH_KEY="C:\Users\dacio.braga\.ssh\key-rsa-ssh-shop-app-server-access-outdoor.pem"
APP="${1:-all}"

# Cria estrutura de diretórios remota
ssh -i "$SSH_KEY" $USER@$HOST "mkdir -p $PATH_APPS/{auth,gateway,order,product,customer}/target"

deploy_module() {
  local module=$1
  echo "🔹 Enviando módulo: $module"
  scp -i "$SSH_KEY" "../shop-$module-api/Dockerfile" "$USER@$HOST:$PATH_APPS/$module/"
  scp -i "$SSH_KEY" "../shop-$module-api/target/shop-$module-api-0.0.1-SNAPSHOT.jar" "$USER@$HOST:$PATH_APPS/$module/target/"
}

if [ "$APP" = "all" ]; then
  for m in auth customer product order gateway; do
    deploy_module "$m"
  done
else
  deploy_module "$APP"
fi

# Envia docker-compose e script auxiliar
scp -i "$SSH_KEY" docker-compose.yaml "$USER@$HOST:$PATH_APPS/"
scp -i "$SSH_KEY" up-containers-in-aws.sh "$USER@$HOST:$PATH_APPS/"

# Executa remotamente
ssh -i "$SSH_KEY" $USER@$HOST "chmod +x $PATH_APPS/up-containers-in-aws.sh && $PATH_APPS/up-containers-in-aws.sh $1 $2"

echo "✅ Deploy concluído com sucesso!"
