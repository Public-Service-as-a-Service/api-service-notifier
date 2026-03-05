# Notifier

_The service provides functionality to send notifications to employees within an organization or group. Groups can be manually created and contain employees from different organization._

## Getting Started

### Prerequisites

- **Java 25 or higher**
- **Maven**
- **MariaDB**(if applicable)
- **Git**
- **[Dependent Microservices](#dependencies)** (if applicable)

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Public-Service-as-a-Service/api-service-notifier
   cd api-service-
   ```
2. **Configure the application:**

   Before running the application, you need to set up configuration settings.
   See [Configuration](#Configuration)

   **Note:** Ensure all required configurations are set; otherwise, the application may fail to start.

3. **Ensure dependent services are running:**

   If this microservice depends on other services, make sure they are up and accessible. See [Dependencies](#dependencies) for more details.

4. **Build and run the application:**

   - Using Maven:

     ```bash
     mvn spring-boot:run
     ```
   - Using Gradle:

     ```bash
     gradle bootRun
     ```

## Dependencies

This microservice depends on the following services:

- **Sms Sender**
  - **Purpose:**  Is used to send text messages to recipients
  - **Repository:** [Link to the repository](https://github.com/Sundsvallskommun/api-service-sms-sender)
  - **Setup Instructions:** Refer to its documentation for installation and configuration steps.

- **Teams Sender**
    - **Purpose:** Is used to send teams messages to recipients
    - **Repository:** [Link to the repository](https://github.com/Sundsvallskommun/api-service-teams-sender)
    - **Setup Instructions:** Refer to its documentation for installation and configuration steps.
Ensure that these services are running and properly configured before starting this microservice.

## API Documentation

Access the API documentation via Swagger UI:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

Alternatively, refer to the `openapi.yml` file located in the project's root directory for the OpenAPI specification.

## Usage

### API Endpoints

Refer to the [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X GET http://localhost:8080/api/notifier/employee/employees/123
```

## Configuration

Configuration is crucial for the application to run successfully. Ensure all necessary settings are configured in `application.yml`.

### Key Configuration Parameters

- **Server Port:**

  ```yaml
  server:
    port: 8080
  ```
- **Database Settings:**

  ```yaml
  spring:
    datasource:
      url: jdbc:mysql://localhost:3306/your_database
      username: your_db_username
      password: your_db_password
  ```
- **External Service URLs:**

  ```yaml
  integration:
  sms-sender:
    base-url: <service-url>
    sender: <sender>
  teams-sender:
    base-url: <service-url>
    
    spring:
    security:
      oauth2:
        client:
          provider:
            sms-sender:
              token-uri: <token-url>
            teams-sender:
              token-uri: <token-url>
          registration:
            sms-sender:
              client-id: <client-id>
              client-secret: <client-secret>           
            teams-sender:
              client-id: <client-id>
              client-secret: <client-secret>
            
  ```

### Database Initialization

The project is set up with [Flyway](https://github.com/flyway/flyway) for database migrations. Flyway is disabled by default so you will have to enable it to automatically populate the database schema upon application startup.

```yaml
spring:
  flyway:
    enabled: true
```

- **Additional setup**
  - **Purpose:** To populate the database it's recommended to use the accompanying CSV-file loader. 
  - **Repository:** [Link to the repository](https://github.com/Public-Service-as-a-Service/cvs-filereader)
  - **Setup Instructions:** Refer to its documentation for installation and configuration steps.
      
### Additional Notes

- **Application Profiles:**

  Use Spring profiles (`dev`, `prod`, etc.) to manage different configurations for different environments.

- **Logging Configuration:**

  Adjust logging levels if necessary.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](https://github.com/Sundsvallskommun/.github/blob/main/.github/CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the [MIT License](LICENSE).

## Code status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Public-Service-as-a-Service_api-service-notifier&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Public-Service-as-a-Service_api-service-notifier)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Public-Service-as-a-Service_api-service-notifier&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Public-Service-as-a-Service_api-service-notifier)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Public-Service-as-a-Service_api-service-notifier&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Public-Service-as-a-Service_api-service-notifier)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Public-Service-as-a-Service_api-service-notifier&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Public-Service-as-a-Service_api-service-notifier)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Public-Service-as-a-Service_api-service-notifier&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Public-Service-as-a-Service_api-service-notifier)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Public-Service-as-a-Service_api-service-notifier&metric=bugs)](https://sonarcloud.io/summary/overall?id=Public-Service-as-a-Service_api-service-notifier)

---

© 2024 Sundsvalls kommun
