FROM node:8.11-alpine

COPY . /dockerizedserviceadaptor
WORKDIR /dockerizedserviceadaptor
RUN npm install

ENV NODE_ENV "production"
CMD ["npm", "start"]
