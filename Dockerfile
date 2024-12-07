FROM node:16-alpine

WORKDIR /app/backend

COPY package*.json ./

RUN npm install

COPY . .



CMD ["node", "app.js"]