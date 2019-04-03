## Stage 1 - Builder
FROM gradle:4.10.2-jdk8-slim as builder

ADD build.gradle .
ADD settings.gradle .
ADD src ./src/

RUN gradle build -x test

## Stage 2 - Create docker image
FROM java:8-jdk-alpine

COPY --from=builder /home/gradle/build/libs/ /opt/kafka4test/
RUN chmod 777 -R /opt/kafka4test
WORKDIR /opt/kafka4test
RUN apk add --no-cache bash


