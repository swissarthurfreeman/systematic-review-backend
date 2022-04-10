# PInfo Back-End Project

# Domain Model
[Domain Model Diagram on nomnoml](https://nomnoml.com/#view/%23zoom%3A%201.0%0A%23edgeMargin%3A%2012%0A%23padding%3A%2015%0A%23edges%3A%20rounded%0A%23fontSize%3A%2016%0A%23arrowSize%3A%201%0A%23title%3A%20Researchado%20Domain%20Model%0A%0A%5BUser%7C%0A%20%20%20%20uuid%3A%20UUID%0A%20%20%20%20email%3A%20String%0A%20%20%20%20username%3A%20String%0A%5D%0A%5BSearch%7C%0A%20%20%20%20uuid%3A%20String%0A%20%20%20%20query%3A%20String%0A%20%20%20%20CNF_query%3A%20String%0A%5D%0A%5BJob%7C%0A%09uuid%3A%20UUID%0A%20%20%20%20status%3A%20String%0A%20%20%20%20estimate%3A%20String%0A%5D%0A%5BResult%7C%0A%20%20%20%20uuid%3A%20UUID%0A%20%20%20%20CNF_query%3A%20String%0A%09Articles%3A%20PDFS%5C%5B%5C%5D%0A%5D%0A%0A%5B%3Cdatabase%3E%20UserDatabase%5D--%5BUser%5D%0A%0A%0A%2F%2F%20CONVENTION%20%3A%20A%200..m-%3E%20B%20means%20A%20has%20knownledge%20of%200%20to%20m%20Bs.%0A%2F%2F%20CONVENTION%20%3A%20A-%3E0..n%20B%20means%20B%20is%20REFERENCED%20by%200..n%20As.%20B%20has%20no%20knowledge%20of%20A.%0A%2F%2F%20user%20has%200..n%20searches%0A%2F%2F%20a%20search%20is%20referenced%20by%201%20user%2C%20the%20search%20history%20of%20a%20user%20is%20private%0A%2F%2F%20therefore%20uniquely%20referenced%20by%20one%20user.%0A%5BUser%5D0.*-%3E1%5BSearch%5D%0A%0A%2F%2F%20A%20search%20can%20reference%200%20or%201%20jobs.%20Indeed%20if%20job%20was%20already%20%0A%2F%2F%20done%20before%20hand%2C%20it%27ll%20refer%20to%20a%20Result.%0A%2F%2F%20A%20job%20is%20referenced%20by%201..n%20searches.%20Indeed%2C%20multiple%20logically%0A%2F%2F%20equivalent%20searches%20will%20refer%20the%20same%20Job.%20%0A%5BSearch%5D0.1-%3E1.n%5BJob%5D%0A%0A%2F%2F%20If%20a%20user%20does%20a%20search%20that%20already%20exists%20with%20a%20job%20currently%0A%2F%2F%20running%2C%20we%20have%20to%20point%20towards%20the%20same%20job.%0A%5BJob%5D1.m%3C-0.n%5BUser%5D%0A%0A%2F%2F%20A%20search%20yields%20only%20ONE%20result%20or%20one%20job%2C%20their%20is%20mutual%20exclusion%0A%2F%2F%20here.%20The%20result%20is%20continually%20updated.%0A%2F%2F%20a%20single%20result%20can%20be%20refered%20by%20multiple%20searches%20since%20multiple%0A%2F%2F%20searches%20can%20be%20logically%20equivalent.%20%0A%5BResult%5D1.m%3C-0.1%5BSearch%5D%0A%0A%2F%2F%20A%20job%20is%20unique%20and%20based%20on%20the%20CNF%20form%20of%20the%20querie(s)%20referencing%20it.%0A%2F%2F%20Two%20logically%20equivalent%20searches%20yield%20a%20reference%20to%20the%20same%20Job%20object%20if%0A%2F%2F%20CNF%20search%20wasn%27t%20previously%20done.%0A%2F%2F%20A%20job%20is%20transient%20but%20a%20result%20is%20non-transient.%20%0A%2F%2F%20if%20the%20job%20is%20destroyed%20the%20result%20is%20not.%0A%5BJob%5Do-%3E0.1%5BResult%5D%0A%0A%2F%2F%20a%20result%20can%20be%20referenced%20by%20an%20infinite%20amount%20of%20users%2C%20since%20any%0A%2F%2F%20search%20created%20by%20a%20user%20which%20has%20a%20logical%20equivalent%20which%20was%20previously%0A%2F%2F%20done%20will%20yield%20a%20reference%20to%20the%20Result%20object.%0A%2F%2F%20a%20user%20has%20references%20to%200..n%20Results%2C%20each%20of%20which%20was%20yielded%20via%0A%2F%2F%20a%20search%20object.%20(There%20is%20no%20public%20catalog%20of%20searches%20in%20the%20requirements.)%20%0A%5BUser%5D0.*-%3E0.*%5BResult%5D%0A%0A%5BResult%5D--%5B%3Cdatabase%3E%20ResultDatabase%5D%0A%0A%5B%3Cdatabase%3E%20SearchDatabase%5D--%5BSearch%5D%0A%0A%0A%0A%0A%0A%0A%0A)

# API Endpoints

All the bodies will be stored as JSON, and the structure of the objects are described in the [Resources types section](#resources-types).

| Verb | URL        | Body             | Return code | Description                                             |
|------|------------|------------------|-------------|---------------------------------------------------------|
| GET  | /jobs     | N/A              | 200         | Gives a list of [Job object](#job-object)               |
| GET  | /searches     | N/A              | 200         | Gives a list of [Search object](#job-object)        |
| GET  | /users/ | N/A              | 200         | Returns a list of [User object](#user-object)                            |
| GET  | /username | N/A              | 200         | Returns a [User object](#user-object) with specified username     |
| POST | /searches/     | [String](#query) | 201         | Attempts to create a new [Search Object](#search-object) and returns it. Returns an error if query has issues.|
| GET  | /searches/ | N/A | 

# Resources types

#### User Object

| Field Name | Type   | Description                                       |
|------------|--------|---------------------------------------------------|
| uuid       | string | User unique identifier. (128 hexadecimal bit key) |
| username   | string | The username of the user (unique and public)      |
| email      | string | email of the user (unique and private)            |
| jobs       | Job[]  | Returns all jobs belonging to user                |


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

