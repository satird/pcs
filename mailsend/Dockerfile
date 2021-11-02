FROM maven:3.8.2-jdk-11

WORKDIR /mailsend
COPY . .
RUN mvn clean install -DskipTests

CMD mvn exec:java
