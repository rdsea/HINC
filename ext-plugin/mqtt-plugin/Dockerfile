FROM node:8-alpine

COPY . /mqttadaptor
WORKDIR /mqttadaptor
RUN npm install

ENV NODE_ENV "production"
CMD ["npm", "start"]
