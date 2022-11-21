FROM gradle:jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM eclipse-temurin:17-ubi9-minimal
EXPOSE 9001
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*all*.jar /app/fit-server.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/fit-server.jar"]
