FROM node:8-alpine

COPY . /rsihubintop

WORKDIR /rsihubintop
RUN npm install
ENV NODE_ENV "production"
CMD ["npm", "start"]
