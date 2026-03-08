# Student Query — findByLastName with JPQL

Filtering records using JPQL `WHERE` and **named parameters**.

---

## Theory

**Named Parameters (`:paramName`)**
- Placeholders in JPQL; bind values with `setParameter("paramName", value)`.
- **Prevents SQL injection** — never concatenate user input into the query string.
- Hibernate escapes and parameterizes the value.

**JPQL WHERE**
- Uses **entity field names** (`lastName`), not column names (`last_name`).
- Hibernate maps to the correct column.

---

## EntityManager — createQuery() with WHERE

```java
@Override
public List<Student> findByLastName(String theLastName) {
    TypedQuery<Student> theQuery = entityManager.createQuery(
        "FROM Student WHERE lastName=:theData", Student.class);

    theQuery.setParameter("theData", theLastName);

    return theQuery.getResultList();
}
```

- **`:theData`** — Named parameter; replaced by bound value.
- **setParameter("theData", theLastName)** — Binds safely; Hibernate generates `WHERE last_name = ?` with prepared statement.

---

## Named vs Positional Parameters

| Style | JPQL | Binding |
|-------|------|---------|
| **Named** | `WHERE lastName=:name` | `setParameter("name", value)` |
| **Positional** | `WHERE lastName=?1` | `setParameter(1, value)` |

Named parameters are clearer when a query has multiple parameters.

---

## Run

```bash
./mvnw spring-boot:run
```

Queries students with `lastName = "Doe"` and prints them.
