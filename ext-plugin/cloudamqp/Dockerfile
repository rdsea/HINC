FROM node:8.11-alpine

COPY . /adaptor
WORKDIR /adaptor
RUN npm install

ENV AMQP_KEY "2b7e1c96-5d18-44cc-8789-986d9b1e8192"
CMD ["npm", "start"]