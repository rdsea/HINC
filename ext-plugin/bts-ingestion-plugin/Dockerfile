FROM node:8-alpine

COPY . /ingestionadaptor
WORKDIR /ingestionadaptor
RUN npm install

ENV NODE_EVN "production"
CMD ["npm", "start"]
