FROM node:8-alpine

COPY . /noderedadaptor
WORKDIR /noderedadaptor
RUN npm install

ENV NODE_ENV "production"
CMD ["npm", "start"]
