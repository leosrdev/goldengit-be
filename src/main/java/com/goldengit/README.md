## Clean Architecture

This folder demonstrates a clear adherence to the principles of Onion Architecture (also known as Clean Architecture), with strict separation of concerns and a unidirectional flow of dependencies.

![image-removebg-preview](https://github.com/user-attachments/assets/4bda130a-21db-4204-803a-b22099bae2ab)


## Folder Structure Overview

- **domain/**  
  Contains the core business logic, entities, and interfaces (contracts).  
  - **No dependencies on other subfolders.**
- **application/**  
  Contains use cases, service interfaces, and application-specific business rules.  
  - **Depends only on `domain/`** (never on infrastructure or adapters).
- **infrastructure/**  
  Contains implementations of interfaces defined in `domain/` or `application/` (e.g., database, messaging, external services).
  - **Depends on `domain/` and/or `application/` but is never depended upon by them.**
- **adapter/** or **web/** (or similar names for delivery mechanisms)  
  Contains controllers, REST endpoints, or code that interfaces with external frameworks or clients.
  - **Depends on `application/` and/or `domain/` but never the reverse.**

## Dependency Direction

The core principle followed is **"dependency inversion"**:
- **Inner layers (domain, application) know nothing about outer layers (infrastructure, adapters).**
- **Outer layers depend on abstractions (interfaces) from inner layers and provide concrete implementations.**

This ensures:
- **Business logic is isolated and testable.**
- **Infrastructure and frameworks can be swapped with minimal impact.**
- **High maintainability and flexibility.**

## Summary

- **Domain**: The center, completely independent.
- **Application**: Coordinates use cases, depends only on domain.
- **Infrastructure/Adapters**: Implement technical details, depend on everything inward but are never depended upon.

By following this structure, the codebase achieves a robust, maintainable, and scalable architecture, fully embracing the Onion/Clean Architecture philosophy and ensuring a single direction of dependencies.
