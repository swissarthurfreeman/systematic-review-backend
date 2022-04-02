# PInfo Back-End Project

# API Endpoints

All the bodies will be stored as JSON, and the structure of the objects are described in the [Resources types section](#resources-types).

| Verb | URL        | Body                | Return code | Description                                |
|------|------------|---------------------|-------------|--------------------------------------------|
| GET  | /jobs/     | N/A                 | 200         | Gives a list of [User object](#job-object) |
| GET  | /jobs/:id  | N/A                 | 200         | Returns Job with specific ID               |
| POST | /jobs/     | [Job](#user-object) | 201         | Creates a new user and returns its id      |
| GET  | /users/    | N/A                 | 200         | Returns list of all users                  |
| GET  | /users/:id | N/A                 | 200         | Returns a [User object](#user-object)      |

# Resources types

#### User Object

| Field Name | Type     | Description                                       |
|------------|----------|---------------------------------------------------|
 | uuid       | string   | User unique identifier. (128 hexadecimal bit key) |
| username   | string   | The username of the user (unique and public)      |
| email      | string   | email of the user (unique and private)            |
| queries    | string[] | Returns all queries ran by user                   |


#### Job Object

| Field Name | Type                         | Description                                              |
|------------|------------------------------|----------------------------------------------------------|
| uuid       | string                       | Unique identifier of the Job. (128 hexadecimal bit key)  |
| query      | string                       | the job query                                            |
| status     | ["queued", running", "done"] | self-explanatory                                         |
| estimation | uint                         | The estimated number of articles send back by the query. |
| user       | User                         | User having creating the Job.                            |

## Quarkus Usage

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/backend-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

