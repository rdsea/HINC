FROM rdsea/hinc-base

WORKDIR ./local-management-service

WORKDIR ./target

WORKDIR /HINC/local-management-service/target

CMD ["/bin/sh", "-c", "./wait-for-it.sh amqp:5672 && java -cp local-management-service-1.0.jar sinc.hinc.local.Main"]
