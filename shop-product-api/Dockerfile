FROM openjdk:17-alpine
VOLUME /tmp
RUN mkdir -p /app/photos
ENV PRODUCT_API_DIR_PHOTO=/app/photos
ARG JAR_FILE=target/shop-product-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java","-jar","/app.jar"]