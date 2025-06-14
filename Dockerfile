FROM node:18 as client-build
WORKDIR /app/client
COPY client/package*.json ./
RUN npm install
COPY client/ .
RUN npm run build

FROM maven:3.9-eclipse-temurin-17-noble as server-build
WORKDIR /app/server
COPY server/pom.xml .
COPY server/src ./src
COPY --from=client-build /app/client/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=server-build /app/server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
