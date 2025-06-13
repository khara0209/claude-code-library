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

### Data Layer
- **MyBatis**: Uses annotation-based SQL mapping instead of XML files
- **H2 Database**: In-memory database with schema/data initialization on startup
- **Entity mapping**: Database `is_available` column maps to Kotlin `available` property

### Key Architecture Patterns

#### Entity Property Mapping
- Kotlin Boolean properties avoid `is` prefix (use `available` not `isAvailable`) 
- This prevents Spring Bean property binding conflicts with getter/setter naming
- Database columns use snake_case (`is_available`) while Kotlin properties use camelCase (`available`)

#### Transaction Management
- `LoanService.loanBook()` coordinates book availability updates with loan creation
- `LoanService.returnBook()` handles atomic book return and availability restoration
- All service methods are `@Transactional`

#### Controller Error Handling
- Controllers use structured logging with operation context
- Flash attributes provide user feedback through redirects
- Error messages include exception details for debugging

### Database Schema Design
- **books**: Core book information with availability tracking
- **users**: User registration data
- **loans**: Loan records with status tracking (ACTIVE/RETURNED)
- Foreign key relationships ensure data integrity

### Template Structure
- Thymeleaf templates with shared navigation and Bootstrap styling
- Form binding uses `th:field` for automatic value/error handling
- List views display relational data by joining entities in controllers

## Development Considerations

### Kotlin-Spring Integration
- Data classes use `var` properties for Spring property binding
- MyBatis parameter binding requires exact property name matches
- Default values in entity constructors support form initialization

### MyBatis Specifics
- `@Options(useGeneratedKeys = true, keyProperty = "id")` for auto-increment IDs
- Parameter names in SQL must match entity property names exactly
- Boolean database columns require explicit mapping in queries