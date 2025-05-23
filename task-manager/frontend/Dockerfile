FROM node:18-alpine

WORKDIR /app

# Copia apenas os arquivos de dependência para instalar primeiro
COPY package.json package-lock.json* ./

# Instala todas as dependências do projeto
RUN npm install

# Copia o restante dos arquivos da aplicação
COPY . .

EXPOSE 3000

CMD ["npm", "start"]
