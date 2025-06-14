# Advertising Campaign App

## Running Locally

### Option 1: Using Docker

in main folder:
   docker build -t advertising_campaign .
   docker run -p 8080:8080 advertising_campaign

### Option 2: Manual - BE serves static frontend files

build frontend

cd client
npm install
npm run build

Copy frontend build output to backend resources from client/dist to server/src/main/resources/static (or change vite.config.js to do it automatically)

build and run the backend

cd ../server
mvn clean package
java -jar target/server-0.0.1-SNAPSHOT.jar

### App is running at http://localhost:8080