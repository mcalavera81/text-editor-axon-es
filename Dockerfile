FROM maven:3.5.2-jdk-8  AS build
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY src $APP_HOME/src
COPY pom.xml $APP_HOME
RUN mvn clean package

FROM openjdk:8-jre
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/target/app.jar .
EXPOSE 8080
ENTRYPOINT ["/usr/bin/java","-jar", "app.jar"]
