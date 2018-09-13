FROM node:8.11-alpine

COPY . /adaptor
WORKDIR /adaptor
RUN npm install

ENV MQTT_KEY "6138d38d-0778-4c46-92b2-3519100a9d85"
CMD ["npm", "start"]