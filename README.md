# Autentication
This repository contains a Spring Boot-based microservice designed to handle user authentication through multiple mechanisms. The service supports login via fixed credentials, Google authentication, and Facebook authentication. It also logs all invalid login attempts in an in-memory database. 

Running Locally :

Clone the repository:

git clone https://github.com/yourusername/authentication-microservice.git
cd authentication-microservice

Build the application:

./mvnw clean install

Run the application:

./mvnw spring-boot:run

Running with Docker :

Build the Docker image:

docker build -t authentication-microservice .

Run the Docker container:

docker run -p 8080:8080 authentication-microservice

Deploying to Kubernetes:

Package the Helm chart:

helm package helm-charts/authentication-microservice
Deploy using Helm:

helm install authentication-microservice ./authentication-microservice-0.1.0.tgz
API Usage

Admin Login:

POST /api/v1/auth/login
Content-Type: application/json
{ "username": "admin", "password": "admin" }

Google Login:

POST /api/v1/auth/login
Content-Type: application/json
{ "username": "googleUser", "password": "password", "authProvider": "google" }

Facebook Login:

POST /api/v1/auth/login
Content-Type: application/json
{ "username": "facebookUser", "password": "password", "authProvider": "facebook" }
Get Login Attempts:

GET /api/v1/auth/login-attempts

Running Tests

Unit Tests:
./mvnw test
