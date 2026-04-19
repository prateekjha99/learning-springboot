# Global Exception Handling

## Main idea

This module shows how to handle REST errors in **one central place** instead of writing exception logic inside each controller.

The key idea is `@ControllerAdvice`.
It tells Spring:

"If a controller throws an exception, use this shared class to handle it."

That makes the code cleaner, easier to reuse, and easier to extend as your API grows.

## Why `@ControllerAdvice` matters

| Without it | With it |
|-----------|---------|
| Each controller may repeat exception-handling code | One global class can handle exceptions for many controllers |
| Harder to maintain | Cleaner and more reusable |
| Error format can become inconsistent | Error responses stay consistent |

## Key annotations and classes

| Item | Role |
|------|------|
| `@ControllerAdvice` | Global exception handler for controllers |
| `@ExceptionHandler` | Marks a method that handles a specific exception |
| `StudentRestExceptionHandler` | Shared class that builds error responses |
| `StudentNotFoundException` | Custom exception thrown when a student ID is invalid |
| `StudentErrorResponse` | Simple response body with status, message, and timestamp |
| `ResponseEntity` | Sends both the error body and HTTP status code |

## How the flow works

1. Client calls `/api/students/{studentId}`
2. Controller checks whether the ID is valid
3. If not, it throws `StudentNotFoundException`
4. Spring sees `@ControllerAdvice`
5. The matching `@ExceptionHandler` method runs
6. A structured JSON error response is returned

## What is handled here

| Case | Result |
|------|--------|
| Student ID not found | Returns `404 Not Found` |
| Any other unexpected exception | Returns `400 Bad Request` |

## Learning point

In the previous module, exception handling was moving into the controller flow.
In this module, that logic is extracted into a **global handler**, which is a better pattern for real Spring applications.
