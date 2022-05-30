# PInfo Back-End Project

# Domain Model
[Domain Model Diagram on nomnoml](https://nomnoml.com/image.svg?source=%23zoom%3A%201.0%0A%23edgeMargin%3A%2014%0A%23padding%3A%2015%0A%23edges%3A%20rounded%0A%23fontSize%3A%2016%0A%23arrowSize%3A%201%0A%23title%3A%20Researchado%20Domain%20Model%0A%0A%5BUser%7C%0A%20%20%20%20uuid%3A%20UUID%0A%20%20%20%20email%3A%20String%0A%20%20%20%20username%3A%20String%0A%5D%0A%5BSearch%7C%0A%20%20%20%20uuid%3A%20String%0A%20%20%20%20query%3A%20String%0A%20%20%20%20CNF_query%3A%20String%0A%5D%0A%5BJob%7C%0A%09uuid%3A%20UUID%0A%20%20%20%20status%3A%20String%0A%20%20%20%20estimate%3A%20String%0A%5D%0A%5BResult%7C%0A%20%20%20%20uuid%3A%20UUID%0A%20%20%20%20CNF_query%3A%20String%0A%09Articles%3A%20PDFS%5C%5B%5C%5D%0A%5D%0A%0A%5B%3Cdatabase%3E%20UserDatabase%5D--%5BUser%5D%0A%0A%0A%2F%2F%20CONVENTION%20%3A%20A%200..m-%3E%20B%20means%20A%20has%20knownledge%20of%200%20to%20m%20Bs.%0A%2F%2F%20CONVENTION%20%3A%20A-%3E0..n%20B%20means%20B%20is%20REFERENCED%20by%200..n%20As.%20B%20has%20no%20knowledge%20of%20A.%0A%2F%2F%20user%20has%200..n%20searches%0A%2F%2F%20a%20search%20is%20referenced%20by%201%20user%2C%20the%20search%20history%20of%20a%20user%20is%20private%0A%2F%2F%20therefore%20uniquely%20referenced%20by%20one%20user.%0A%5BUser%5D0.*-%3E1%5BSearch%5D%0A%0A%2F%2F%20A%20search%20can%20reference%200%20or%201%20jobs.%20Indeed%20if%20job%20was%20already%20%0A%2F%2F%20done%20before%20hand%2C%20it%27ll%20refer%20to%20a%20Result.%0A%2F%2F%20A%20job%20is%20referenced%20by%201..n%20searches.%20Indeed%2C%20multiple%20logically%0A%2F%2F%20equivalent%20searches%20will%20refer%20the%20same%20Job.%20%0A%5BSearch%5D0.1-%3E1.n%5BJob%5D%0A%0A%2F%2F%20If%20a%20user%20does%20a%20search%20that%20already%20exists%20with%20a%20job%20currently%0A%2F%2F%20running%2C%20we%20have%20to%20point%20towards%20the%20same%20job.%0A%5BJob%5D1.m%3C-0.n%5BUser%5D%0A%0A%2F%2F%20A%20search%20yields%20only%20ONE%20result%20or%20one%20job%2C%20their%20is%20mutual%20exclusion%0A%2F%2F%20here.%20The%20result%20is%20continually%20updated.%0A%2F%2F%20a%20single%20result%20can%20be%20refered%20by%20multiple%20searches%20since%20multiple%0A%2F%2F%20searches%20can%20be%20logically%20equivalent.%20%0A%5BResult%5D1.m%3C-0.1%5BSearch%5D%0A%0A%2F%2F%20A%20job%20is%20unique%20and%20based%20on%20the%20CNF%20form%20of%20the%20querie(s)%20referencing%20it.%0A%2F%2F%20Two%20logically%20equivalent%20searches%20yield%20a%20reference%20to%20the%20same%20Job%20object%20if%0A%2F%2F%20CNF%20search%20wasn%27t%20previously%20done.%0A%2F%2F%20A%20job%20is%20transient%20but%20a%20result%20is%20non-transient.%20%0A%2F%2F%20if%20the%20job%20is%20destroyed%20the%20result%20is%20not.%0A%5BJob%5Do-%3E0.1%5BResult%5D%0A%0A%2F%2F%20a%20result%20can%20be%20referenced%20by%20an%20infinite%20amount%20of%20users%2C%20since%20any%0A%2F%2F%20search%20created%20by%20a%20user%20which%20has%20a%20logical%20equivalent%20which%20was%20previously%0A%2F%2F%20done%20will%20yield%20a%20reference%20to%20the%20Result%20object.%0A%2F%2F%20a%20user%20has%20references%20to%200..n%20Results%2C%20each%20of%20which%20was%20yielded%20via%0A%2F%2F%20a%20search%20object.%20(There%20is%20no%20public%20catalog%20of%20searches%20in%20the%20requirements.)%20%0A%5BUser%5D0.*-%3E0.*%5BResult%5D%0A%0A%5BResult%5D--%5B%3Cdatabase%3E%20ResultDatabase%5D%0A%0A%5B%3Cdatabase%3E%20SearchDatabase%5D--%5BSearch%5D%0A%0A%0A%0A%0A%0A)

[Class Diagram on nomnoml](https://nomnoml.com/image.svg?source=%23zoom%3A%201.5%0A%0A%5BUser%7C%0Auuid%3A%20UUID%0Aemail%3A%20String%3B%20%0Ausername%3A%20String%3B%20%0Asearches%3A%20Search%3CCollection%3E%7C%0AgetEmail()%3B%20getUsername()%3B%20getSearches()%0AsetEmail()%3B%20setUsername()%3B%20createSearch()%5D%0A%0A%5BSearch%7C%0Auuid%3A%20UUID%0Auser_uuid%3A%20UUID%3B%0Aquery%3A%20String%3B%0Atimestamp%3A%20Time%3B%0Aucnf%3A%20String%0A%7Cstatic%20syntax_check()%3B%0Astatic%20cnf_calculator()%3B%0AgetResult()%5D%0A%0A%5BJobber%7C%0A%40Singleton%7C%0Ajobs%3A%20List%3CJob%3E%0Aresults%3A%20List%3CResult%3E%7C%0Asubmit(query)%0A%5D%0A%5BJob%7C%0Atimestamp%3A%20Time%0Aestimation%3A%20String%0Aucnf%3A%20String%7C%0Aquery_python_api()%0Anotify()%0AgetResult()%0A%5D%0A%0A%5BResult%7C%0Acontent%3A%20String%0A%5D%0A%5BUser%5D-%3E%5BSearch%5D%0A%0A%5BSearch%5Do-%3E%5B%3Csingleton%3E%20Jobber%5D%0A%0A%5B%3Csingleton%3E%20Jobber%5Do-%3E%5BJob%5D%0A%0A%5BJobber%5D-%3E%5BResult%5D%0A%0A%5BJob%5D-%3E%5BResult%5D%0A%0A)


[Search Activity Diagram on nomnoml](https://nomnoml.com/image.svg?source=%23zoom%3A%201.0%0A%23padding%3A%2010%0A%23edgeMargin%3A%200%0A%23spacing%3A%2080%0A%23edges%3A%20rounded%0A%23fontSize%3A%2016%0A%23arrowSize%3A%201%0A%23gravity%3A%201%0A%23title%3A%20Researchado%20Search%20Procedure%0A%0A%2F%2F%20UCNF%20%3D%20Unique%20Conjuncitve%20Normal%20Form%0A%0A%5B%3Cinput%3E%20InputQuery%5D-%3E%5BCreate%20UCNF%5D%0A%0A%5BCreate%20UCNF%5D-%3E%5BCreate%20Search%5D%0A%0A%5BCreate%20Search%5D-%3E%5B%3Cchoice%3E%20UCNF%20Exists%3F%5D%0A%0A%5B%3Cchoice%3E%20UCNF%20Exists%3F%5D-%3Eyes%5B%3Cchoice%3E%20Search%20has%20Result%3F%5D%0A%5B%3Cchoice%3E%20Search%20has%20Result%3F%5Dyes-%3E%5BReturn%20Result%5D%0A%0A%5B%3Cchoice%3E%20Search%20has%20Result%3F%5D-%3Eno%5BReturn%20Job%5D%0A%0A%5BReturn%20Result%5D-%3E%5B%3Cend%3E%20Done%5D%0A%0A%5B%3Cchoice%3E%20UCNF%20Exists%3F%5D-%3Eno%5BCreate%20Job%5D%0A%0A%5BReturn%20Job%5D-%3E%5B%3Cstate%3E%20Observe%20Status%5D%0A%0A%5BCreate%20Job%5D-%3E%5B%3Cstate%3E%20Observe%20Status%5D%0A%0A%0A%5B%3Cstate%3E%20Observe%20Status%5D-%3Estatus%20change%5B%3Cchoice%3E%20is%20not%20done%20%3F%5D%0A%0A%5B%3Cchoice%3E%20is%20not%20done%20%3F%5D-%3Eyes%5B%3Cstate%3E%20Observe%20Status%5D%0A%0A%5B%3Cchoice%3E%20is%20not%20done%20%3F%5Dno%20-%3E%5B%3Cend%3E%20Done%5D%0A%0A%0A%0A%0A)

# API Endpoints

All the bodies will be stored as JSON, and the structure of the objects are described in the [Resources types section](#resources-types).
All endpoints will only be accessible if the jwt token is valid, otherwise 403 Forbidden error will be sent back.
All endpoints authentify the user using the Subject claim of the jwt. Endpoints.http has an example in order to acquire this token
when testing in development. Production will use the Oauth2 server deployed on the vm.

| Endpoints             | Allowed Verbs                  | Implemented |
|-----------------------|--------------------------------|-------------|
| /jobs                 | GET                            | yes         |
| /jobs/:id             | GET                            | yes         |
| /searches             | GET, POST                      | yes         |
| /results              | GET                            | yes         |
| /results/:id          | GET                            | yes         |
| /results/:id/articles | GET                            | yes         |

### Semantics

| Verb | URL                     | Body                                              | Return code | Description                                                                                                                  
|------|-------------------------|---------------------------------------------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| GET  | /jobs                   | N/A                                               | 200         | Returns a list of [Job object](#job-object) that user is observing.                                                                        | 
| GET  | /jobs/:id               | N/A                                               | 200         | Returns a [Job object](#job-object) specified by id. Returns an [Error Object](error-object) if uuid is invalid (job doesn't exist, invalid format). Jobs are global. |
| GET  | /searches               | N/A                                               | 200         | Returns a list of [Search Object](#search-object) of the user. |
| POST | /searches               | {"query": "hiv OR malaria"}                       | 201         | Creates a [Search Object](#search-object) belonging to user. |
| GET  | /searches/:id           | N/A                                               | 200         | Returns a list of [Search Object](#search-object) belonging to the user. Returns an [Error Object](error-object) if search uuid is invalid. |
| GET  | /results                | N/A                                               | 200         | Returns a list of [Result Object](#result-object). List will be empty if no results exist. Results are global. |
| GET  | /results/:id            | N/A                                               | 200         | Returns a [Result Object](#result-object). Returns an [Error Object](error-object) if result_uuid is invalid. |
| GET  | /results/:id/articles   | N/A                                               | 200         | Returns a list of [Article Object](#article-object) Returns an [Error Object](error-object) if result does not exist.|

# Resources types

#### Search Object

| Field Name  | Type      | Description                                                                                                               |
|-------------|-----------|---------------------------------------------------------------------------------------------------------------------------|
| uuid        | String      | Search String.                                                                                                              |
| user_uuid   | String      | UUID of user having created the search. This is the jwt subject claim.    |
| query       | string    | Original user provided search query.                                                                                      |
| ucnf        | String      | Unique conjunctive normal form query.                                                                                     |
| timestamp   | Date      | Time of creation of the search.                                                                                           |
| result_uuid | String|""   | UUID of [Result Object](#result-object) created by a Job with the ucnf query. Will be empty string if not yet available.  |

#### Job Object

| Field Name | Type                          | Description                                               |
|------------|-------------------------------|-----------------------------------------------------------|
| uuid       | UString                         | Unique identifier of the Job. (128 hexadecimal bit key) |
| ucnf       | string                        | The ucnf job query.                                       |
| status     | ["queued", "running", "done"] | Self-explanatory.                                         |
| percentage | uint                          | Percentage of completion.                                 |
| user       | User                          | User having creating the Job.                             |

#### Result Object
| Field Name | Type                          | Description                                               |
|------------|-------------------------------|-----------------------------------------------------------|
| uuid       | String                        | Unique identifier of the result.                          |
| ucnf       | string                        | the ucnf job query.                                       |

#### ErrorReport Object

ErrorReports are generated when validation fails or requests to inexistant resources are made. 

| Field Name | Type                          | Description                                               |
|------------|-------------------------------|-----------------------------------------------------------|
| help       | String                        | A help string, for now just a jest.|
| errors     | Error\[\]                     | An array of [Error Object](#error-object).|

#### Error Object
| Field Name | Type                          | Description                                               |
|------------|-------------------------------|-----------------------------------------------------------|
| cause      | String                        | The error cause. |
| message    | String                        | a message detailing error and potential fixes.|
| status     | String                        | The http error associated with this error. |


#### Article Object
| Field Name | Type                          | Description                                                        |
|------------|-------------------------------|--------------------------------------------------------------------|
| uuid       | String                        | Unique identifier of the article result.                           |
| result_uuid| String                        | Unique identifier of the result to the which this article belongs. |
| url        | string                        | Url of the article                                                 |
| title      | String                        | article title                                                      |
| authors    | String                        | list of article authors                                            |
| abstract   | String                        | article abstract                                                   |

## Kafka Communication

Jobert and backend interact through the jobs channel.
We send a job with a single ucnf string to Jobert. 
Python du clusterer/puller will return the result with
a ucnf key and the clustered articles. 


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

