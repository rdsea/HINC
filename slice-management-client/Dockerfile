FROM rdsea/provider-base

COPY . /pizza
WORKDIR /pizza

RUN npm install
RUN npm install -g

CMD ["/auth.sh"]
