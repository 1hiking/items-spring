FROM gradle:8-jdk19-alpine AS builder
WORKDIR /app

COPY gradlew build.gradle /app/

RUN chmod +x gradlew

COPY . /app

RUN ./gradlew bootJar --no-daemon --stacktrace

FROM eclipse-temurin:19-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV DB_URL=${DB_URL} \
    DB_USER=${DB_USER} \
    DB_PASSWORD=${DB_PASSWORD} \
    OKTA_OAUTH2_ISSUER=${OKTA_OAUTH2_ISSUER} \
    OKTA_OAUTH2_CLIENT_ID=${OKTA_OAUTH2_CLIENT_ID} \
    OKTA_OAUTH2_CLIENT_SECRET=${OKTA_OAUTH2_CLIENT_SECRET}


EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]
