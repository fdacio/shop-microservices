#!/bin/bash
set -euo pipefail  # Melhor que apenas set -e

# Carrega configurações com validação
if [[ ! -f "config.sh" ]]; then
    echo "❌ Arquivo config.sh não encontrado!"
    exit 1
fi

source config.sh

# Validação das variáveis de configuração
if [[ -z "${HOST:-}" || -z "${USER:-}" || -z "${SSH_KEY:-}" ]]; then
    echo "❌ Variáveis HOST, USER ou SSH_KEY não definidas em config.sh"
    exit 1
fi

# Verifica se chave SSH existe
if [[ ! -f "$SSH_KEY" ]]; then
    echo "❌ Chave SSH não encontrada: $SSH_KEY"
    exit 1
fi

PATH_APPS="/home/ubuntu/shop"

APP="${1:-all}"
DELAY="${2:-60}"
OPTIONS="${3:-"--with-dependencies"}"

#if [ "$OPTIONS" = "--dependencies" ]; then
#  OPTIONS="--dependencies"
#  if [ "$3" != "" ]; then
#    DELAY="$3"
#  fi
#else
#  DELAY="$2"
#fi


# Validação dos parâmetros de entrada
validate_input() {
    local valid_apps=("all" "auth" "customer" "product" "order" "gateway" "models" "exceptions" "auth-keys")

    # shellcheck disable=SC2076
    if [[ ! " ${valid_apps[*]} " =~ " $APP " ]]; then
        echo "❌ Aplicação inválida: $APP"
        echo "📋 Aplicações válidas: ${valid_apps[*]}"
        exit 1
    fi

    if [[ "$OPTIONS" != "--with-dependencies" && "$OPTIONS" != "" ]]; then
        echo "❌ Opção inválida: $OPTIONS"
        echo "📋 Opções válidas: --with-dependencies ou vazio"
        exit 1
    fi

    if ! [[ "$DELAY" =~ ^[0-9]+$ ]] || [[ "$DELAY" -lt 0 ]]; then
        echo "❌ Delay inválido: $DELAY (deve ser um número >= 0)"
        exit 1
    fi
}


# Valida entrada após definir as variáveis
validate_input

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
            echo "❌ Módulo inválido: $module"
            return 1 ;;
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

    # Executa Maven com melhor tratamento de erro
    if ! (cd "$dir" && mvn --batch-mode --offline -DskipTests=true clean install); then
        echo "❌ Falha ao compilar módulo: $module"
        return 1
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

cd ../aws

echo "🔗 Conectando ao servidor AWS: $HOST"

# Testa conexão SSH antes de prosseguir
if ! ssh -i "$SSH_KEY" -o ConnectTimeout=10 -o BatchMode=yes "$USER@$HOST" "echo '✅ Conexão SSH estabelecida'" 2>/dev/null; then
    echo "❌ Falha ao conectar via SSH ao servidor: $HOST"
    exit 1
fi

# Cria estrutura de diretórios remota com melhor tratamento
echo "📁 Criando estrutura de diretórios no servidor..."
if ! ssh -i "$SSH_KEY" "$USER@$HOST" "mkdir -p $PATH_APPS/{auth,gateway,order,product,customer}/target"; then
    echo "❌ Falha ao criar diretórios no servidor"
    exit 1
fi

echo "✅ Estrutura de diretórios criada com sucesso"

# Função para fazer deploy de um módulo
deploy_module() {
    local module="$1"
    local jar_file="../shop-${module}-api/target/shop-${module}-api-0.0.1-SNAPSHOT.jar"
    local dockerfile="../shop-${module}-api/Dockerfile"

    # Verifica se o arquivo JAR existe
    if [[ ! -f "$jar_file" ]]; then
        echo "❌ Arquivo JAR não encontrado: $jar_file"
        return 1
    fi

    # Verifica se o arquivo Dockerfile existe
    if [[ ! -f "$jar_file" ]]; then
        echo "❌ Arquivo Dockerfile não encontrado: $dockerfile"
        return 1
    fi

    echo "📤 Fazendo deploy do módulo: $module"

    # Envia arquivo via SCP com opções otimizadas
    if ! scp -i "$SSH_KEY" -o ConnectTimeout=30 -o ServerAliveInterval=60 "$jar_file" "$USER@$HOST:$PATH_APPS/${module}/target/"; then
        echo "❌ Falha ao enviar arquivo JAR para o servidor: $module"
        return 1
    fi

    # Envia arquivos via SCP com opções otimizadas
    if ! scp -i "$SSH_KEY" -o ConnectTimeout=30 -o ServerAliveInterval=60 "$dockerfile" "$USER@$HOST:$PATH_APPS/$module/"; then
        echo "❌ Falha ao enviar arquivo Dockerfile para o servidor: $module"
        return 1
    fi
    echo "✅ Módulo $module enviado com sucesso"
}

# Faz deploy dos módulos na ordem correta
if [ "$APP" = "all" ]; then
  for m in auth customer product order gateway; do
    deploy_module "$m"
  done
else
  deploy_module "$APP"
fi

echo "🚀 Deploy concluído com sucesso!"

# Função de limpeza (opcional)
cleanup() {
    echo "🧹 Limpando arquivos temporários..."
    # Adicione comandos de limpeza se necessário
}

# Registra função de cleanup para ser executada em caso de erro
trap cleanup ERR

echo "📤 Enviando arquivo docker-compose.yaml"
scp -i "$SSH_KEY" docker-compose.yaml "$USER@$HOST:$PATH_APPS/"
echo "📤 Enviando arquivo up-containers-in-aws.sh"
scp -i "$SSH_KEY" up-containers-in-aws.sh "$USER@$HOST:$PATH_APPS/"

# Executa remotamente
echo "⚙️ Executando script de startup no servidor..."
ssh -i "$SSH_KEY" "$USER"@"$HOST" "chmod +x $PATH_APPS/up-containers-in-aws.sh && $PATH_APPS/up-containers-in-aws.sh $APP $DELAY"

# Exibe resumo final
echo ""
echo "📊 RESUMO DO DEPLOY:"
echo "   📍 Servidor: $HOST"
echo "   👤 Usuário: $USER"
echo "   📦 Aplicações: $APP"
echo "   ⚙️  Opções: ${OPTIONS:-nenhuma}"
echo "   ⏱️  Delay: ${DELAY}s"
echo ""
echo "✅ Script executado com sucesso!"
