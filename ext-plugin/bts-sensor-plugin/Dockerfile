FROM node:8.11-alpine

COPY . /btssensoradaptor
WORKDIR /btssensoradaptor
RUN npm install

ENV NODE_ENV "production"
CMD ["npm", "start"]
