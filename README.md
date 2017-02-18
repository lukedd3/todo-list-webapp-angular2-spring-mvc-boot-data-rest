# todo-list-webapp-angular2-spring-mvc-boot-data-rest
Todo app. Frontend is made in Angular 2 and is based on angular-quickstart project. Backend with REST API is made in Spring framework (Spring MVC, Spring Boot, Spring Data JPA)

## Running backend

`mvn clean spring-boot:run` <- Runs app with dev profile. Uses integrated Tomcat and H2 database in file mode. Database file is saved in project directory. Running app url: http://localhost:8080/
`mvn clean tomcat7:run -Ptestenv -DskipTests` <- Runs app with test profile. Uses Tomcat that is installed on system. Uses installed on sysyem PosgreSQL database. Information about database connection is in "application-testenv.properties" file. Running app url: http://localhost:8080/
`mvn clean test` <- Runs unit tests
`mvn clean integration-test` <- Runs unit tests and integration tests


## Running frontend in development mode

You need to have NodeJS and Npm installed before you can do things described below.

Open cmd and navigate to project main folder. Then use commands mentioned below.

If you run this app for the first time you have to install all node modules that are used. To do so type in cmd:
```
npm install
```

To run app in development mode type:
```
npm start
```

## License

This software is distributed under the MIT License (MIT).

More info could be found in the **LICENSE** file.
