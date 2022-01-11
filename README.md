<!--
 * Copyright 2022 Baloise Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
-->

# Agenda
1. [Motivation](#Motivation)
2. [What are testcontainers](#Top1)
3. [Example usage](#Top2)
4. [DB testing from scratch using flyway](#Top3)
5. [Migration from inmem-db to oracle live demo](#Top4)
6. [Limitations](#Top5)
7. [Conclusion](#Top6)



# <a id="Motivation"></a> Motivation
- DB/JPA unit tests using in-memory db like h2 or derby differ to much from real database with respect to
  - values (i.e. Inetger vs. BidDecimal) 
  - special DB functions (e.g. [Listagg](https://docs.oracle.com/cd/E11882_01/server.112/e41084/functions089.htm#SQLRF30030)) 
  - trigger, views, etc.
- Development based on local DB or any other local server vs. CI build (e.g. Jenkins, Github actions etc.) 



# <a id="Top1"></a> What are [testcontainers][Testcontainers]
> Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.
> - ___Data access layer integration tests:___
> 
>    use a containerized instance of any database to test your data access layer code
> - ___Application integration tests:___
> 
>    for running your application in a short-lived test mode with dependencies, such as databases, message queues or web servers.
> - ___UI/Acceptance tests___:
> 
>    use containerized web browsers, compatible with Selenium, for conducting automated UI tests. Each test can get a fresh instance of the browser, with no browser state, plugin variations or automated browser upgrades to worry about.
>   


### Prerequisite
- [General Docker requirements](https://www.testcontainers.org/supported_docker_environment/)
- A supported JVM testing framework like Junit(4/5), Spock or manually controlled

# <a id="Top2"></a> Example usage
1. Add maven dependency for entire lifecycle management
    ```xml
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <version>1.16.2</version>
      <scope>test</scope>
    </dependency>
    ```
2. Add [certain testcontainer(s)](https://www.testcontainers.org/modules/databases/) you need to test on (e.g. Postgres)
    ```xml
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>postgresql</artifactId>
      <version>1.16.2</version>
      <scope>test</scope>
    </dependency>
    ```
3. Configure testcontainer in your test class (e.g. JUnit5)
    ```java
    @Testcontainers
    class MyDbTestClass {
      ...
      @Container
      public static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:12")
         .withDatabaseName("openx")
         .withUsername("postgres")
         .withPassword("postgres");
      ...
      @Test
      void someTest() {
        ...
      }
    }
    ```


# <a id="Top3"></a>DB testing from scratch using [flyway](https://flywaydb.org/documentation/)
### Prerequisite
- start Postgres DB in order to run application (not testing)
  ```shell
  docker run --name openxday_2022-01-27 \ 
      -p 5432:5432 \
      -e POSTGRES_USER=postgres \
      -e POSTGRES_PASSWORD=postgres \
      -e POSTGRES_DB=openx 
    postgres:12
  ```
### Basic setup
- perform initial application loading test which succeeds `mvn test -f pom.xml`
- for testing shutdown docker container `openxday_2022-01-27` and perform maven test build again `mvn test -f pom.xml`
  
  Test will fail now, cause application does not start as db connection cannot be established any longer.
- Add Testcontainer as described in [previous chapter](#Top2).
  ```xml
  <dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.16.2</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.16.2</version>
    <scope>test</scope>
  </dependency>
  ```
  ![java example](./docs/images/spring_boot_junit5_testcontainer_example.png)
  
  1: Add Testcontainer capability
  
  2: Add and configure Postgres Container loaded from dockerhub using image [postgres:12](https://hub.docker.com/_/postgres) 

  3: Spring specific: overwrite config values commonly defined in [resources/application.yaml](./src/main/resources/application.yaml)

  4: access container (if necessary) within tests


- perform maven test build again `mvn test -f pom.xml` and monitor it suceedes again (as CI build would do).
> While executing test you will notice two docker containers started:
> ![docker tescontainers](docs/images/test_container_list.png)


# <a id="Top4"></a> Migration from inmem-db to oracle live demo



# <a id="Top5"></a>Limitations




# <a id="Top6"></a>Conclusion




[Testcontainers]: https://www.testcontainers.org/
