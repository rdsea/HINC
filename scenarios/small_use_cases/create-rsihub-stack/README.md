# Create and Deploy an rsiHub Stack 

In this example, we show how to create and deploy an rsiHub Stack for testing purposes. We developed a stack generator that minimises the configuration effort. The stack can either be deployed to a running docker swarm or it can be run locally using docker-compose.

### For this tutorial, we

* create a rsiHub stack, containing rsiHub's Global Management Service, Software Artefact Service, Interoperability Service, Local Management Services, Providers and Adapters. The entities of the stack are based on a scenario (*BTS*, *Valencia Seaport* or *Deploy Software Artefact*). 

## Developers/User Story

### Generate the Stack and Configuration Files

1. To generate the different scenario stack, change to the directory `HINC/examples` and install the node-dependencies.
    ```
    $ cd <HINC-path>/examples
    $ npm install
    ``` 

2. Run one of the scenario-commands mentioned below and answer the questions of the generator. The generated stack and configuration files will be in `HINC/examples/result`.

    ***BTS***
    ```
    $ node src/scenarios/bts.js
    ```

    ***Valencia***
    ```
    $ node src/scenarios/bts.js
    ```
    ***Deploy Software Artefact***
    ```
    $ node src/scenarios/bts.js
    ```

### Deploy the Stack...

#### ... to a Docker Swarm
1. Change to the results directory:
    ```
    $ cd results
    ```

2. Ensure that docker is set to the swarm-manager:
    ```
    $ docker eval $(docker env <swarm-manager-id>)
    ```

3. Deploy the stack:
    ```
    $ docker stack deploy -c docker-stack.yml
    ```

#### ... locally, with docker-dompose 

In the results directory run:
```
$ docker-compose up
```

### Check if the stack is running

In the following commands `<stack-ip>` refers to `localhost`, if the stack is deployed locally using docker-compose. Otherwise it refers to the public, external IP of the swarm manager.

#### Access the OpenAPIs (resp. Swagger APIs) of the rsiHub Services

In a browser open following URLs:

1. Global Management Service

    `http://<stack-ip>:8080/swagger-ui.html`
    
2. Interoperability Service

    `http://<stack-ip>:8081/api-docs`
    
3. Software Artefact Service

    `http://<stack-ip>:8082/swagger-ui.html`
        