#!/bin/bash

echo "🚀 Iniciando setup do projeto Task Manager..."

# 1. Acessa pasta backend e verifica se Maven está instalado
echo "📁 Acessando pasta backend para verificar o Maven..."
cd backend || { echo "❌ Pasta backend não encontrada"; exit 1; }

if ! command -v mvn &> /dev/null; then
    echo "📦 Maven não encontrado. Instalando..."
    sudo apt update && sudo apt install -y maven
else
    echo "✔ Maven já instalado"
fi

# 2. Instala dependências e empacota o backend
echo "🔧 Instalando dependências e empacotando backend..."
./mvnw clean package -DskipTests || { echo "❌ Erro ao empacotar o backend"; exit 1; }

# Volta para a raiz
cd ..

# 3. Acessa pasta frontend e verifica se Node.js/NPM estão instalados
echo "📁 Acessando pasta frontend para verificar o Node.js/NPM..."
cd frontend || { echo "❌ Pasta frontend não encontrada"; exit 1; }

if ! command -v npm &> /dev/null; then
    echo "📦 Node.js/NPM não encontrados. Instalando..."
    sudo apt update && sudo apt install -y nodejs npm
else
    echo "✔ Node.js/NPM já instalados"
fi

# 4. Instala dependências do frontend
echo "🎨 Instalando dependências do frontend..."
npm install || { echo "❌ Erro ao instalar dependências do frontend"; exit 1; }

# Volta para a raiz
cd ..

# 5. Verifica se o usuário atual tem permissão no Docker
echo "🐳 Subindo containers com Docker Compose..."
if docker info > /dev/null 2>&1; then
    docker compose up --build
else
    echo "⚠️  Sem permissão para acessar o Docker. Tentando com sudo..."
    sudo docker compose up --build
fi
