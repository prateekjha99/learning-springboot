# Student Query — findAll with JPQL

Fetching all records using JPA `createQuery()` and **JPQL**.

---

## Theory

**JPQL (Java Persistence Query Language)**
- Query language for JPA; similar to SQL but uses **entity names** and **field names**, not table/column names.
- Database-agnostic — Hibernate translates JPQL to the target DB dialect.
- `FROM Student` → entity class; Hibernate maps to `student` table.

**createQuery() vs find()**
- **find()** — Primary key lookup only; single entity.
- **createQuery()** — Custom JPQL; supports `WHERE`, `ORDER BY`, joins, etc.; returns list or single result.

---

## EntityManager — createQuery() + JPQL

```java
@Override
public List<Student> findAll() {
    TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student", Student.class);
    return theQuery.getResultList();
}
```

- **JPQL:** `FROM Student` — uses entity name; Hibernate generates `SELECT * FROM student`.
- **TypedQuery&lt;Student&gt;** — Type-safe; returns `List<Student>` from `getResultList()`.
- **getResultList()** — Returns list; empty list if no results. (Use `getSingleResult()` for exactly one row — throws if 0 or many.)

---

## JPQL vs SQL

| JPQL | SQL |
|------|-----|
| `FROM Student` | `SELECT * FROM student` |
| Entity/field names | Table/column names |
| `WHERE s.lastName = :name` | `WHERE last_name = ?` |

---

## Run

```bash
./mvnw spring-boot:run
```

Prints all students from the database.
