FROM node:18-alpine

WORKDIR /app

COPY package*.json ./

# Instala dependências, incluindo jwt-decode
RUN npm install \
    && npm install jwt-decode \
    && npm install --save-dev @types/jwt-decode

COPY . .

EXPOSE 3000

CMD ["npm", "start"]
