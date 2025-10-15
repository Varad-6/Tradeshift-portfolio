TradeShift Portfolio – Financial Platform Backend
A Spring Boot backend for secure financial portfolio management and trading, featuring robust authentication and authorization using JWT.
This project provides user registration, login, and basic account security flows as part of the first module.

Features Built
User registration and login REST APIs with password hashing (BCrypt) and JWT-based stateless authentication.

Role-based user management support (USER, ADMIN).

Optional two-factor authentication field framework.

Secure backend infrastructure with custom JWT token filter, security configuration, and cross-origin (CORS) setup for frontend integration.

Project Structure and Key Files
/src/main/java/com/tradeshift/portfolio/model/User.java
User entity representing each account in the system.

Includes fields for ID, name, email, hashed password, role, and 2FA status.

JPA annotations for database mapping.

Password field is write-only for security.

/src/main/java/com/tradeshift/portfolio/model/TwoFactorAuth.java
Embeddable entity storing two-factor authentication settings (enabled status, channel).

Cleanly integrated with the User entity for future upgrade to OTP/2FA.

/src/main/java/com/tradeshift/portfolio/domain/Role.java
Enum of user roles.

Used for authorization logic and mapping user access levels.

/src/main/java/com/tradeshift/portfolio/domain/VerificationType.java
Enum of channels for 2FA (Mobile, Email).

Powers flexible two-factor authentication options.

/src/main/java/com/tradeshift/portfolio/repository/UserRepository.java
Spring Data JPA repository for accessing and persisting User objects.

Includes methods like findByEmail() for efficient login and account lookup.

/src/main/java/com/tradeshift/portfolio/service/CustomUserDetailsService.java
Integrates project User entity with Spring Security’s authentication flow.

Loads users from DB for login and processes credentials securely.

/src/main/java/com/tradeshift/portfolio/security/SecurityConfig.java
Spring Security filter chain configuration.

Configures JWT stateless sessions, opens registration/login endpoints, protects others.

Integrates security filter for JWT token validation.

Sets up password encoder (BCrypt) and CORS policy for frontend access.

/src/main/java/com/tradeshift/portfolio/security/JwtConstant.java
Central store for JWT secret, token expiration time, and header names.

Keeps secrets/configs organized and easy to update.

/src/main/java/com/tradeshift/portfolio/security/JwtProvider.java
Utility/service for generating JWT tokens during login or registration.

/src/main/java/com/tradeshift/portfolio/security/JwtTokenValidator.java
Custom filter to validate JWT token for every secure request.

Extracts user info and authorities from JWT, sets authentication context for Spring Security.

/src/main/java/com/tradeshift/portfolio/controller/AuthController.java
REST controller for /auth/signup and /auth/login.

Handles input validation, password hashing, user saving, JWT token generation, and login authentication.

Returns clear API responses and handles errors securely.

/src/main/java/com/tradeshift/portfolio/response/AuthResonse.java
DTO for authentication responses sent to clients.

Contains JWT token, status, user messages, 2FA, and session info.

/src/main/resources/application.yml (or .properties)
Central configuration for DB connection, ports, JWT settings, and Hibernate.

How to Run
Clone the repo:

text
git clone https://github.com/Varad-6/Tradeshift-portfolio.git
Set up database in your local environment (application.yml).

Build and run with Maven or your IDE.

Use Postman/cURL to hit /auth/signup and /auth/login endpoints.

Sample Endpoints
POST /auth/signup – Registers new user, returns JWT.

POST /auth/login – Authenticates user, returns JWT.
