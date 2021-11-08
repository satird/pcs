FROM maven:3.8.1-jdk-11

WORKDIR /pcs
COPY . .
RUN mvn clean install -DskipTests

CMD mvn spring-boot:run
