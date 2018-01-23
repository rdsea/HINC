# Local HINC deployment

docker and docker-compose are required to use this example

# Usage
This folder contains what is needed to deploy a functional HINC example locally. Simple run the script `start.sh` to start the example.
DO NOT use `docker-compose up` as the different components have varying startup times that could result in data inconsistency.
This should be fixed in the future with improvements to HINC itself

However the following commands can be used to monitor the different serivces

- `$ docker-compose down` to stop all services and cleanup resources 
- `$ docker-compose logs <service name>` to monitor logs of each service
- `$ docker-compose exe <service name> <command>` to execute a command (i.e. sh) in the service

# Global management service
Is available under the url `http://localhost:9000/local-management-service-1.0`

# Local management service
Shoud be populated with OpenHab IoT Unit data and accessible through the Global management service

# Docker Maintenance
All the docker images are based off rdsea/hinc-base which is build at the root of this repository. When any changes are made to the source code
please make sure to push a new version of rdsea/hinc-base with the following commands (from the root of this repository):

- `$ docker build -t rdsea/hinc-base .`
- `$ docker push rdsea/hinc-base`
