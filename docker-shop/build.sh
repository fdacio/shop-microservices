#!/bin/bash
set -euo pipefail

APP="${1:-""}"
OPTIONS="${2:-"--with-dependencies"}"

validate_input() {

    local valid_apps=("all" "auth" "customer" "product" "order" "gateway" )
    local valid_deps=("--with-dependencies" "--without-dependencies" )

    # shellcheck disable=SC2076
    if [[ ! " ${valid_apps[*]} " =~ " $APP " ]]; then
        echo "❌ Aplicação inválida: $APP"
        echo "📋 Aplicações válidas: ${valid_apps[*]}"
        exit 1
    fi

    # shellcheck disable=SC2076
    if [[ ! " ${valid_deps[*]} " =~ " $OPTIONS " ]]; then
        echo "❌ Opção inválida: $OPTIONS"
        echo "📋 Opções válidas:  ${valid_deps[*]}"
        exit 1
    fi
}

validate_input

# Função para compilar um módulo do repositório
# Aceita nomes curtos: models, exceptions, auth-keys, auth, customer, order, product, gateway
build_module() {
    local module="$1"
    local dir
    local image_name

    case "$module" in
        models)   dir="../shop-models";  image_name= ;;
        exceptions) dir="../shop-exceptions"; image_name= ;;
        auth-keys) dir="../shop-auth-keys"; image_name= ;;
        auth)     dir="../shop-auth-api";     image_name="daciosoftware/shop-auth-api:latest" ;;
        customer) dir="../shop-customer-api"; image_name="daciosoftware/shop-customer-api:latest" ;;
        order)    dir="../shop-order-api";    image_name="daciosoftware/shop-order-api:latest" ;;
        product)  dir="../shop-product-api";  image_name="daciosoftware/shop-product-api:latest" ;;
        gateway)  dir="../shop-gateway-api";  image_name="daciosoftware/shop-gateway-api:latest" ;;
    esac

    # Verifica se o diretório existe
    if [[ ! -d "$dir" ]]; then
        echo "❌ Diretório não encontrado: $dir"
        return 1
    fi

    # Verifica se tem pom.xml
    if [[ ! -f "$dir/pom.xml" ]]; then
        echo "❌ Arquivo pom.xml não encontrado em: $dir"
        return 1
    fi

    echo "🔧 Compilando módulo: $module (dir: $dir)"

    # Maven gera o jar
    (cd "$dir" && mvn --update-snapshots -DskipTests=true clean package)

    # Docker builda a imagem (só para os módulos que têm Dockerfile)
    if [[ -n "$image_name" ]]; then
        echo "🐳 Buildando imagem: $image_name"
        docker build -t "$image_name" "$dir"
        echo "✅ Imagem $image_name criada com sucesso"
    fi


    echo "✅ Módulo $module compilado com sucesso"
}

# Build os módulos necessários
if [ "$OPTIONS" = "--with-dependencies" ]; then
  build_module models
  build_module exceptions
  build_module auth-keys
fi

if [ "$APP" = "all" ]; then
  for m in auth customer product order gateway; do
     build_module "$m"
  done
else
  build_module "$APP"
fi

echo "✅ Build finished!!!";