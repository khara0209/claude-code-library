# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Application Lifecycle
- **Run application**: `./gradlew bootRun` (starts on http://localhost:8080)
- **Build**: `./gradlew build`
- **Test**: `./gradlew test`
- **Clean**: `./gradlew clean`

### Database Access
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:librarydb`
  - Username: `sa`
  - Password: (empty)

### Logging
- **Application logs**: `logs/library-app.log`
- **Real-time monitoring**: `tail -f logs/library-app.log`
- **Error filtering**: `grep "ERROR" logs/library-app.log`

## Architecture Overview

This is a Spring Boot 3 library management system using Kotlin with a layered architecture:

### Technology Stack
- **Framework**: Spring Boot 3.2.0
- **Language**: Kotlin 1.9.20 
- **Java Version**: Java 17
- **Database**: H2 (in-memory)
- **ORM**: MyBatis 3.0.3 (annotation-based)
- **Template Engine**: Thymeleaf
- **Frontend**: Bootstrap 5.3.0 + Bootstrap Icons
- **Testing**: JUnit 5 + Mockito Kotlin

### Data Layer
- **MyBatis**: Uses annotation-based SQL mapping instead of XML files
- **H2 Database**: In-memory database with schema/data initialization on startup
- **Entity mapping**: Database `is_available` column maps to Kotlin `available` property
- **Database tables**: `books`, `users`, `loans` (note: renamed from Loan to Lend in code but table remains `loans`)

### Core Entities

#### Book Entity
- Properties: `id`, `title`, `author`, `isbn`, `publisher`, `publishedYear`, `category`, `available`, `createdAt`, `updatedAt`
- Manages book information and availability status

#### User Entity  
- Properties: `id`, `name`, `email`, `phone`, `createdAt`, `updatedAt`
- Manages library user information

#### Lend Entity (formerly Loan)
- Properties: `id`, `bookId`, `userId`, `lendDate`, `dueDate`, `returnDate`, `status`, `createdAt`, `updatedAt`
- Manages book lending transactions
- Status: "ACTIVE" (currently lent) or "RETURNED"
- Default lending period: 14 days

### Key Architecture Patterns

#### Entity Property Mapping
- Kotlin Boolean properties avoid `is` prefix (use `available` not `isAvailable`) 
- This prevents Spring Bean property binding conflicts with getter/setter naming
- Database columns use snake_case (`is_available`) while Kotlin properties use camelCase (`available`)

#### Transaction Management
- `LendService.lendBook()` coordinates book availability updates with lend creation
- `LendService.returnBook()` handles atomic book return and availability restoration
- All service methods are `@Transactional`

#### Controller Error Handling
- Controllers use structured logging with operation context
- Flash attributes provide user feedback through redirects
- Error messages include exception details for debugging

### Database Schema Design
- **books**: Core book information with availability tracking
- **users**: User registration data
- **loans**: Lending records with status tracking (ACTIVE/RETURNED)
- Foreign key relationships ensure data integrity

### Service Layer Architecture

#### BookService
- CRUD operations for books
- Availability management (`updateAvailability`)
- Finding available books for lending

#### UserService  
- CRUD operations for users
- Email-based user lookup
- User registration and management

#### LendService
- Book lending workflow (`lendBook`)
- Book return processing (`returnBook`)
- Active lending tracking (`findActiveLends`)
- User and book lending history

### Controller Layer Architecture

#### BookController (`/books`)
- Book management interface
- CRUD operations with form handling
- Book availability display

#### UserController (`/users`)
- User management interface  
- User registration and profile management
- User detail views

#### LendController (`/lends`)
- Lending management interface
- New lending form with book/user selection
- Return processing workflow
- Lending history display

#### HomeController (`/`)
- Dashboard with system statistics
- Quick action navigation
- System overview metrics

### Template Structure
- **Thymeleaf templates** with shared navigation and Bootstrap styling
- **Form binding** uses `th:field` for automatic value/error handling
- **List views** display relational data by joining entities in controllers
- **Navigation**: Consistent navbar across all pages
- **URL structure**: 
  - `/` - Dashboard
  - `/books` - Book management
  - `/users` - User management  
  - `/lends` - Lending management

### Testing Strategy

#### Unit Tests
- **Service layer tests** using Mockito and mockito-kotlin
- **Repository layer mocking** with `@Mock` annotations
- **Test coverage** for all CRUD operations and business logic
- **Edge case testing** including error conditions

#### Integration Tests
- **Controller tests** using `@WebMvcTest`
- **MockMvc** for HTTP request/response testing
- **Service layer integration** with mocked dependencies

#### Test Configuration
- **JUnit 5** as the testing framework
- **Mockito Kotlin** for Kotlin-friendly mocking
- **Spring Boot Test** for integration testing

## Development Considerations

### Kotlin-Spring Integration
- Data classes use `var` properties for Spring property binding
- MyBatis parameter binding requires exact property name matches
- Default values in entity constructors support form initialization

### MyBatis Specifics
- `@Options(useGeneratedKeys = true, keyProperty = "id")` for auto-increment IDs
- Parameter names in SQL must match entity property names exactly
- Boolean database columns require explicit mapping in queries
- Column name mapping: `loan_date` maps to `lendDate` property

### Naming Conventions
- **Entities**: PascalCase (Book, User, Lend)
- **Properties**: camelCase (bookId, lendDate, returnDate)
- **Database columns**: snake_case (book_id, loan_date, return_date)
- **URLs**: kebab-case (/lends, /books, /users)
- **Template files**: kebab-case (lends/list.html, books/form.html)

### Business Rules
- **Default lending period**: 14 days
- **Book availability**: Automatically managed during lend/return operations
- **Lend status**: ACTIVE during lending, RETURNED after return
- **Data integrity**: Foreign key constraints prevent orphaned records

### Error Handling
- **Service exceptions**: Thrown for business rule violations
- **Controller error handling**: Flash messages for user feedback
- **Logging**: Structured logging with context information
- **Validation**: Form validation with user-friendly error messages

## Configuration Notes

### Database Configuration
- **Auto-initialization**: Schema and data loaded on startup
- **Console access**: H2 console available for development
- **Logging**: MyBatis SQL logging enabled for debugging

### Application Properties
- **Logging levels**: DEBUG for application packages, INFO for root
- **File logging**: Rotated log files in `logs/` directory
- **MyBatis configuration**: Camel case mapping enabled

### Security Considerations
- No authentication/authorization implemented (development system)
- H2 console enabled for development only
- SQL injection prevention through parameterized queries