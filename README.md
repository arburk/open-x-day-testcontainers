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
2. Add certain testcontainer(s) you need to test on (e.g. Postgres)
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
    import javax.transaction.TransactionScoped;@Testcontainers
    class MyDbTestClass {
      ...
      @Container
      public static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:11.2")
         .withDatabaseName("testdb")
         .withUsername("sa")
         .withPassword("sa");
      ...
      @Test
      void someTest() {
        ...
      }
    }
    ```


# <a id="Top3"></a>DB testing from scratch using [flyway](https://flywaydb.org/documentation/)
### Prerequisite
- start Postgres DB in order to run application
  ```shell
  docker run --name openxday_2022-01-27 \ 
      -p 5432:5432 \
      -e POSTGRES_USER=postgres \
      -e POSTGRES_PASSWORD=postgres \
      -e POSTGRES_DB=openx 
    postgres:12
  ```



# <a id="Top4"></a> Migration from inmem-db to oracle live demo



# <a id="Top5"></a>Limitations




# <a id="Top6"></a>Conclusion




[Testcontainers]: https://www.testcontainers.org/
