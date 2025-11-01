# 🧭 Plano de Estudos AWS para Desenvolvedores

**Duração:** 4 semanas  
**Tempo estimado:** 5 a 8h por semana  
**Foco:** aprender a usar a AWS para desenvolver, testar e publicar aplicações web/backend  

---

## 🗓️ Semana 1 – Fundamentos e Acesso

### 🎯 Objetivo
Entender como funciona a AWS, gerenciar usuários e interagir via console e linha de comando.

### 🧩 Tópicos
1. **Introdução à AWS**
   - O que é região, zona de disponibilidade e conta raiz.
   - Painel do Billing e limite gratuito (Free Tier).
2. **IAM (Identity and Access Management)**
   - Criar usuário IAM com acesso programático e console.
   - Criar grupo e política simples (AmazonS3FullAccess, por exemplo).
   - Configurar Access Key localmente.
3. **AWS CLI**
   - Instalar a CLI (`aws configure`).
   - Testar comandos básicos:
     ```bash
     aws s3 ls
     aws sts get-caller-identity
     ```

### ✅ Exercício prático
- Criar usuário IAM “developer” e testar comandos CLI.
- Fazer login com ele no console e confirmar permissões.

🔗 **Links úteis**  
- [AWS IAM Docs](https://docs.aws.amazon.com/IAM/latest/UserGuide/introduction.html)  
- [AWS CLI Docs](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-welcome.html)

---

## 🗓️ Semana 2 – Armazenamento e Banco de Dados

### 🎯 Objetivo
Aprender a usar o S3 e o RDS na prática.

### 🧩 Tópicos
1. **S3 (Simple Storage Service)**
   - Criar um bucket.
   - Fazer upload e download de arquivos pelo console e via SDK.
   - Tornar um arquivo público.
   - Configurar versionamento.
2. **RDS (Relational Database Service)**
   - Criar um banco PostgreSQL ou MySQL (Free Tier).
   - Conectar via DBeaver ou cliente SQL.
   - Criar tabelas e inserir dados.

### ✅ Exercício prático
- Criar bucket S3 `meuapp-dev-files`.
- Fazer upload de imagem e listar via SDK (ex: Spring Boot ou Node).
- Criar banco RDS e conectar o app localmente.

🔗 **Links úteis**  
- [AWS S3 Docs](https://docs.aws.amazon.com/s3/index.html)  
- [AWS RDS Docs](https://docs.aws.amazon.com/rds/index.html)

---

## 🗓️ Semana 3 – Deploy da Aplicação

### 🎯 Objetivo
Colocar sua aplicação rodando na AWS.

### 🧩 Tópicos
1. **EC2**
   - Criar instância Linux (Amazon Linux 2).
   - Conectar via SSH.
   - Instalar Java ou Node.
   - Copiar o app (scp ou git clone).
   - Abrir porta 8080 no Security Group.
   - Testar acesso público (http://ec2-endereco:8080).
2. **Elastic Beanstalk (opcional, automatizado)**
   - Criar ambiente Java ou Node.
   - Fazer deploy de um `.jar` ou `.zip`.
   - Entender variáveis de ambiente e logs.
3. **CloudWatch**
   - Ver logs do Beanstalk ou EC2.
   - Criar alarme simples (CPU > 80%).

### ✅ Exercício prático
- Fazer deploy manual via EC2.
- Repetir usando Elastic Beanstalk.
- Consultar logs no CloudWatch.

🔗 **Links úteis**  
- [AWS EC2 Docs](https://docs.aws.amazon.com/ec2/index.html)  
- [Elastic Beanstalk Docs](https://docs.aws.amazon.com/elasticbeanstalk/index.html)  
- [CloudWatch Docs](https://docs.aws.amazon.com/cloudwatch/index.html)

---

## 🗓️ Semana 4 – Integrações e Boas Práticas

### 🎯 Objetivo
Integrar serviços, proteger dados e preparar o app para produção.

### 🧩 Tópicos
1. **Secrets Manager**
   - Armazenar credenciais (DB_PASSWORD, API_KEY).
   - Ler secrets via SDK.
2. **Lambda + API Gateway (serverless)**
   - Criar função simples que retorna JSON.
   - Publicar via API Gateway.
   - Testar endpoint público.
3. **ECR + ECS / Fargate (containers)**
   - Criar imagem Docker e subir para ECR.
   - Rodar container via ECS Fargate.
4. **Monitoramento e Custos**
   - Usar Cost Explorer.
   - Criar Budget alert por e-mail.

### ✅ Exercício prático
- Subir uma função Lambda “Hello World”.
- Configurar Budget alert com limite de US$5.
- Fazer deploy do app via container (opcional).

🔗 **Links úteis**  
- [AWS Lambda Docs](https://docs.aws.amazon.com/lambda/index.html)  
- [AWS API Gateway Docs](https://docs.aws.amazon.com/apigateway/index.html)  
- [AWS Budgets Docs](https://docs.aws.amazon.com/cost-management/latest/userguide/budgets-managing-costs.html)

---

## 🧰 Ferramentas úteis
- AWS CLI – linha de comando oficial  
- AWS SDK (Java, Node, Python...)  
- AWS Toolkit (IntelliJ, VS Code ou Eclipse)  
- DBeaver – acessar RDS  
- Postman – testar endpoints Lambda/API Gateway  

---

## 🧠 Dica final
Depois dessas 4 semanas você já estará pronto para:
- Fazer deploy completo de um backend na AWS.  
- Usar banco, storage e autenticação com segurança.  
- Entender custos e monitoramento.  

A partir daí, você pode se especializar em:
- **Serverless (Lambda + DynamoDB)**  
- **Containers (ECS, Fargate, ECR)**  
- **Infraestrutura como código (CloudFormation/CDK)**
