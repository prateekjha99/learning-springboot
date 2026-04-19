# Path Variable: Get Single Student

## Main idea

A **path variable** lets Spring read a value directly from the URL.

Example:

`/api/students/1`

Here, `1` is the value taken from the path and used inside the controller method.

This is a common REST pattern when you want to fetch **one specific resource**.

## What this module teaches

| Topic | Why it matters |
|-------|----------------|
| `@GetMapping("/students/{studentId}")` | Defines a URL with a dynamic value |
| `@PathVariable` | Binds the value from the URL to a method parameter |
| `List<Student>` | Keeps sample data simple so the focus stays on REST mapping |
| `theStudents.get(studentId)` | Uses the path value to return one student |

## Key classes

| Class | Role |
|------|------|
| `DemoApplication` | Starts the Spring Boot app |
| `StudentRestController` | Exposes REST endpoints under `/api` |
| `Student` | Simple data object with `firstName` and `lastName` |

## Endpoint flow

1. Client calls `/api/students/{studentId}`
2. Spring reads `{studentId}` from the URL
3. `@PathVariable` passes it to `getStudent(int studentId)`
4. The controller returns the matching student from the list

## Small example

| Request | Meaning |
|--------|---------|
| `GET /api/students` | Return all students |
| `GET /api/students/0` | Return the first student |
| `GET /api/students/1` | Return the second student |
