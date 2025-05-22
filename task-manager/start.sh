#!/bin/bash

echo "🚀 Iniciando setup do projeto Task Manager..."

# 1. Verifica se Maven está instalado, senão instala
if ! command -v mvn &> /dev/null; then
    echo "📦 Maven não encontrado. Instalando..."
    sudo apt update && sudo apt install -y maven
else
    echo "✔ Maven já instalado"
fi

# 2. Verifica se Node.js/NPM estão instalados, senão instala
if ! command -v npm &> /dev/null; then
    echo "📦 Node.js/NPM não encontrados. Instalando..."
    sudo apt update && sudo apt install -y nodejs npm
else
    echo "✔ Node.js/NPM já instalados"
fi

# 3. Instala dependências e empacota o backend
echo "🔧 Instalando dependências e empacotando backend..."
cd backend || { echo "❌ Pasta backend não encontrada"; exit 1; }
./mvnw clean package -DskipTests || { echo "❌ Erro ao empacotar o backend"; exit 1; }
cd ..

# 4. Instala dependências do frontend
echo "🎨 Instalando dependências do frontend..."
cd frontend || { echo "❌ Pasta frontend não encontrada"; exit 1; }
npm install || { echo "❌ Erro ao instalar dependências do frontend"; exit 1; }
cd ..

# 5. Sobe os containers
echo "🐳 Subindo containers com Docker Compose..."
docker compose up --build
