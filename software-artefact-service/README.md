# Software Artefact Service

## Preconditions

The latest versions of the modules `common` and `repository` are installed.
To install the latest versions of `common` and `repository`, either run `mvn clean install` for the respective modules or for the whole project. 

## Build

1. `mvn clean install`
2. `docker build -t software-artefact-service .`

## Run
`docker run -it --rm -p 8082:8082 software-artefact-service`


## Swagger Interface
http://localhost:8082/swagger-ui.html