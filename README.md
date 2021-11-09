## Notable Technologies/Design Decisions
- Backend: Java 11 with Spring Boot
- Message Broker: RabbitMQ
- Database: PostgreSQL
- ORM: Hibernate
- Security: Spring Security
- Spring Controllers couple REST

## Setup
1. Install Docker: `$ sudo apt install docker-ce`
2. Install Docker Compose: `$ sudo curl -L https://github.com/docker/compose/releases/download/1.21.2/docker-compose- uname -s - uname -m -o /usr/local/bin/docker-compose`
3. Copy project from GitHub `$ git copy https://github.com/satird/pcs.git`
4. Go to the project folder `$ cd /you_path_to_project/`
5. Run docker compose file: `$  docker-compose up`
6. Visit [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/) 
7. Use postman to check functionality

## Setup 2
1. Create file with name `docker-compose.yml`
2. Add to file:
```
version: '3.8'
services: 
  rabbitmq:
    container_name: rabbitmq
    image: rabbitmq:3.9-management
    ports:
      - "15672:15672"
      - "5672:5672"
  db:
    container_name: postgres_db
    image: postgres:14.0
    restart: always
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=root
      - POSTGRES_DB=pcs_db
    ports:
      - 5434:5434
    command: -p 5434
  app:
    depends_on:
      - db
      - rabbitmq
    image: satird/final_app:latest
    restart: always
    container_name: app
    ports:
      - "8080:8080"
    environment:
      FLASK_ENV: development
    stdin_open: true
    tty: true
  sender:
    container_name: email_sender
    image: satird/final_sender:latest
    depends_on:
      - rabbitmq
    restart: always
volumes:
  db: 
```
3. previous setup from point 5 and further
 
