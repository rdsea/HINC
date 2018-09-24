FROM node:8-alpine

COPY . /bigqueryadaptor
WORKDIR /bigqueryadaptor
RUN npm install

ENV NODE_ENV "production"

CMD ["npm", "start"]
