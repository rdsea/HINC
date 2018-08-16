from rdsea/pizza

COPY ./slice.json /test/slice.json
COPY ./user_client.js /test/user_client.js
COPY ./docker-entrypoint.sh /test/docker-entrypoint.sh
WORKDIR /test

ENV LOCAL_COUNT 1
ENV PROVIDER_GROUP_COUNT 1
ENV USER_ID 1
ENV GLOBAL_IP 127.0.0.1
ENV RESULT_ENDPOINT http://localhost:3000

ENTRYPOINT ["/test/docker-entrypoint.sh"]