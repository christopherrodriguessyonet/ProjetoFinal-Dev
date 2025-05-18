# Etapa base com Node.js
FROM node:18-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos de dependências
COPY package*.json ./

# Instala as dependências
RUN npm install

# Copia o restante do código da aplicação
COPY . .

# Expõe a porta padrão do React
EXPOSE 3000

# Comando padrão para iniciar em modo de desenvolvimento
CMD ["npm", "start"]
