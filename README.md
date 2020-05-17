# trg-consumer project

This project implements the consumer of the queue.
It consumes the sensor measurements from the queue, does some basic validations and business logic and persists to a database.
In case the measurements received are out of predefined ranges, some warnings are logged.

All configuration (kafka, database, business logic, etc) is defined in the application.properties file and can be overwritten by specifying system properties or environment variables.

There are also some basic tests implemented that use the H2 in-memory database to persist data.
They can be executed by running the command below. When executed, code coverage results are generated in file target/jacoco.exec

```
./mvnw clean test
```

In addition, there are 2 Dockerfiles that can be used to build the Docker image, 1 for jvm build and 1 for native build. Both can be found under src/main/docker.

To access the live metrics, you can use the /metrics/application endpoint, for example http://localhost:8081/metrics/application .

Currently there is a limitation in Quarkus and you cannot create multiple consumer threads within the same application instance - see issue below.

https://github.com/quarkusio/quarkus/issues/7215

To do that in Spring Boot, you can use the ConcurrentKafkaListenerContainerFactory.setConcurrency() method to specify the number of consumer threads to be created.

Therefore, the application can be dynamically scaled horizontally by running multiple instances/containers of it, all of them will join the same consumer group with a result to consume messages from different partitions of the kafka topic.


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```
./mvnw quarkus:dev -Dquarkus.http.port=8081 -Ddebug=5006
```

For each additional instance run on the same machine, the port numbers should be increased to avoid port conflicts.
During startup, the application registers 2 sensors with ids 1 and 2 using the import.sql file. These sensor ids can be used to test the application.

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `trg-consumer-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/trg-consumer-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/trg-consumer-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.