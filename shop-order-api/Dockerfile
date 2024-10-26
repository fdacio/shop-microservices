FROM openjdk:17-alpine
RUN apk add curl
VOLUME /tmp
ARG JAR_FILE=target/shop-order-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java","-jar","/app.jar"]