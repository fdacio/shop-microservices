#!/bin/bash
set -e

source config.sh

PATH_APPS="/home/ubuntu/shop"
APP="${1:-all}"

# Função para compilar um módulo do repositório
# Aceita nomes curtos: models, exceptions, auth-keys, auth, customer, order, product, gateway
build_module() {
    local module="$1"
    local dir

    case "$module" in
        models)
            dir="../shop-models" ;;
        exceptions)
            dir="../shop-exceptions" ;;
        auth-keys)
            dir="../shop-auth-keys" ;;
        auth)
            dir="../shop-auth-api" ;;
        customer)
            dir="../shop-customer-api" ;;
        order)
            dir="../shop-order-api" ;;
        product)
            dir="../shop-product-api" ;;
        gateway)
            dir="../shop-gateway-api" ;;
        *)
            # se foi fornecido um caminho/custom, usa direto
            #dir="$module" ;;
    esac

    echo "🔧 Building módulo: $module (dir: $dir)"
    # roda mvn em um subshell para não alterar o diretório do script
    (cd "$dir" && mvn --offline --update-snapshots -DskipTests=true clean install)
}

# Build os módulos necessários
build_module models
build_module exceptions
build_module auth-keys
if [ "$APP" = "all" ]; then
  for m in auth customer product order gateway; do
    build_module "$m"
  done
else
  build_module "$APP"
fi

cd ../aws

# Cria estrutura de diretórios remota
ssh -i "$SSH_KEY" "$USER"@"$HOST" "mkdir -p $PATH_APPS/{auth,gateway,order,product,customer}/target"

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
ssh -i "$SSH_KEY" "$USER"@"$HOST" "chmod +x $PATH_APPS/up-containers-in-aws.sh && $PATH_APPS/up-containers-in-aws.sh $1 $2"

echo "✅ Deploy concluído com sucesso!"
