# KanbanBoard backend

Backend for KanbanBoard application


## How to run
1. Make sure you have installed JRE and JDK for Java 8 (https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
2. Make sure you have installed Maven (https://maven.apache.org/install.html)
3. Go to project directory containing `pom.xml` file and run the following command: `mvn spring-boot:run`. Maven will download required dependencies. It may take a while.
4. After application starts, API will be available at `http://localhost:8080/kanbanboard/api`

In order to use API you need some kind of client. Preffered application is available at https://github.com/pszczolkowski/KanbanBoard-backend

## How to run tests
Go to project directory containing `pom.xml` file and run the following command: `mvn install -PTESTS`


## REST API documentation
REST API documentation as JSON is available at `http://localhost:8080/kanbanboard/api/v2/api-docs?group=Business-API`