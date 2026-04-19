# Students Exception Handling

## Main idea

This module introduces **basic REST exception handling**.

Instead of letting Spring return a raw error when a student ID is invalid, the controller throws a custom exception and returns a structured response.

This makes the API easier to understand and more user-friendly.

## What this module teaches

| Topic | Why it matters |
|-------|----------------|
| Custom exception | Lets you represent a specific problem clearly |
| `@ExceptionHandler` | Catches exceptions and converts them into a response |
| `ResponseEntity` | Returns both JSON data and HTTP status code |
| Error response object | Gives a consistent format for API errors |

## Key annotations and classes

| Item | Role |
|------|------|
| `@GetMapping("/students/{studentId}")` | Reads a student ID from the URL |
| `@PathVariable` | Passes the URL value into the method |
| `StudentNotFoundException` | Custom exception for invalid student IDs |
| `@ExceptionHandler` | Handles thrown exceptions inside the controller |
| `StudentErrorResponse` | Holds `status`, `message`, and `timeStamp` |
| `ResponseEntity` | Sends the error body with the correct HTTP status |

## How the flow works

1. Client calls `/api/students/{studentId}`
2. Controller checks whether the ID exists
3. If the ID is invalid, it throws `StudentNotFoundException`
4. `@ExceptionHandler` catches it
5. Spring returns a JSON error response with status information

## What is handled here

| Case | Result |
|------|--------|
| Student ID not found | Returns `404 Not Found` |
| Any other exception | Returns `400 Bad Request` |

## Learning point

This is the first step from "just returning data" to "returning meaningful API errors".

In this module, the exception handling lives **inside the controller**.
In the next module, the same idea is improved using `@ControllerAdvice` so the handling becomes **global and reusable**.
