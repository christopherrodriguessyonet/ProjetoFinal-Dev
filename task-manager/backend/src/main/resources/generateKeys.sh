#!/bin/bash

# Gerar chave privada
openssl genpkey -algorithm RSA -out privateKey.pem -pkeyopt rsa_keygen_bits:2048

# Gerar chave pública
openssl rsa -pubout -in privateKey.pem -out publicKey.pem

# Mover as chaves para o diretório correto
mv privateKey.pem src/main/resources/
mv publicKey.pem src/main/resources/