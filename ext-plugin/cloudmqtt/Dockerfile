FROM node:8.11-alpine

COPY . /cloudmqttadaptor
WORKDIR /cloudmqttadaptor
RUN npm install

ENV CLOUDMQTT_KEY "6138d38d-0778-4c46-92b2-3519100a9d85"
CMD ["npm", "start"]
