The repository aim to provide API for CRUD operations for the HINC models. The models this repository will support are:

1. Cloud model: support configuration tool like SALSA. Cloud service can be artifact (static) or runtime service (dynamic)
2. IoT Unit: support low level information of sensing resource, e.g. sensor, artifact to deploy sensor.
3. Virtual IoT resource: support fog computing (Software-defined-gateway) and its concepts including DataPoint, ControlPoint, Connectivity, ExecutionEnv
4. Network functions: support network function service, network slice.

The underlying of the repository is graph database, tentatively used Neo4J but it was too heavy when combinign with with Spring + JPA, so OrientDB + DTO layer + DAO interface to persist is the current choice.
