FROM node:8.11-alpine

COPY . /cloudmqttadaptor
WORKDIR /cloudmqttadaptor
RUN npm install

ENV NODE_ENV "production"
CMD ["npm", "start"]
