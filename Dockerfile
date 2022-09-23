# syntax=docker/dockerfile:1

FROM openjdk:18
WORKDIR /smart-tumbler-manager

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src
RUN cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime
#update application timezone
RUN echo "Europe/Moscow" >> /etc/timezone

CMD ["./mvnw", "spring-boot:run"]