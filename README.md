SchoolApp – REST API with JWT \& Hibernate



Backend REST API for a school management system built using Java + Jakarta EE (JAX-RS), Hibernate, MySQL, JWT authentication and HikariCP connection pooling. Supports login, registration, role-based access control and CRUD operations for multiple entities.



Features



• Registration \& login

• JWT authentication

• Role-based authorization (ADMIN, TEACHER, STUDENT)

• CRUD for Students and Teachers

• Layered architecture (DAO + Service + REST)

• Bean validation + custom validators

• MySQL with HikariCP connection pool

• Pagination support for listing resources

• Uses environment variables for sensitive data (PASS\_DB6, SECRET\_KEY)

• Secrets are not hardcoded in the persistence configuration



Roles



• ADMIN → full access to Users, Teachers, Students

• TEACHER → can view Students, update Courses they teach

• STUDENT → can only view their own data



Requirements



• JDK 17 or newer

• Maven 3+

• MySQL server

• IntelliJ IDEA (recommended)

• Postman or any REST client (optional)



Database Setup



Before running the project, you need to create an empty MySQL database.

Hibernate will automatically generate the tables on first run.



Steps:



Open your MySQL client 



Create a new empty database with the same name used in your persistence.xml 



Do not create tables manually — Hibernate will handle that automatically



Make sure MySQL is running before starting the app



Environment Variables



The application requires two environment variables for security:



• PASS\_DB6 → password for the database connection (used by HikariCP)

• SECRET\_KEY → secret used for signing and validating JWT tokens



You must set them in your system environment variables or IntelliJ Run Configuration.



Example values (do NOT hardcode them in code):



• PASS\_DB6 = your\_mysql\_password

• SECRET\_KEY = any 32+ character random string



How to Run



1\. Clone the repository



2\. Import the project in IntelliJ as a Maven project



3\. Make sure the environment variables PASS\_DB6 and SECRET\_KEY are set



4\. Make sure MySQL is running and the empty database exists



5\. Build the project with Maven (mvn clean install)



6\. Run the application from IntelliJ 



When the server starts, Hibernate will automatically create all necessary tables.



Available Functionality



• User registration

• User login (JWT token is returned)

• Role-based access depending on ADMIN, TEACHER, STUDENT

• CRUD operations for Students and Teachers

• Pagination support

• Full Hibernate ORM integration

• HikariCP connection pooling

• Request validation with Jakarta Bean Validation



REST Endpoints



Authentication

• POST /auth/register — register new user

• POST /auth/login — authenticate user \& return JWT token



Students (requires JWT)

• GET /students — list students

• GET /students/{id} — get student by id

• POST /students — create student (ADMIN, TEACHER)

• PUT /students/{id} — update student (ADMIN, TEACHER)

• DELETE /students/{id} — delete student (ADMIN)



Teachers (requires JWT)

• GET /teachers

• GET /teachers/{id}

• POST /teachers (ADMIN)

• PUT /teachers/{id} (ADMIN)

• DELETE /teachers/{id} (ADMIN)



Users (requires JWT)

• GET /users — only ADMIN

• GET /users/{id} — only ADMIN

• DELETE /users/{id} — only ADMIN



Notes:

• JWT token must be sent in Authorization header: Authorization: Bearer <token>

• Unauthorized requests return HTTP 401

• Forbidden requests return HTTP 403



Architecture Overview



The project follows a layered architecture:



• Resource Layer (REST endpoints using JAX-RS)

• Service Layer (business logic + validation)

• DAO Layer (Hibernate + transactions)

• Entity Layer (JPA entities with annotations)



Technologies used:



• Java 17

• Jakarta EE (JAX-RS, Validation, JSON-B)

• Hibernate ORM + HikariCP

• MySQL

• JWT authentication

• Maven

• IntelliJ IDEA



Responsibilities per layer:



• Resource Layer: request handling, response serialization, status codes

• Service Layer: authorization checks, CRUD logic, input validation

• DAO Layer: database interaction using Hibernate Session

• Entity Layer: domain models mapped to tables



License



This project is licensed under the MIT License.



You are free to use, modify, and distribute the code as long as you include the original license file.

