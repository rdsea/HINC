FROM docker.elastic.co/logstash/logstash-oss:6.1.2
RUN rm -f /usr/share/logstash/pipeline/logstash.conf


COPY logstash.conf pipeline/
