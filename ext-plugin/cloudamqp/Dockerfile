FROM node:8.11-alpine

COPY . /cloudamqpadaptor
WORKDIR /cloudampqadaptor
RUN npm install

ENV CLOUDAMQP_KEY "2b7e1c96-5d18-44cc-8789-986d9b1e8192"
CMD ["npm", "start"]
