# Student Read — JPA/Hibernate

Reading records with JPA `EntityManager.find()`.

---

## Theory

**find() — Primary Key Lookup**
- Fetches an entity by its primary key.
- Returns the entity if found, `null` if not.
- The returned entity is **managed** (tracked in persistence context); changes can be auto-persisted.

**Read vs Write — @Transactional**
- **Read operations** (`find`) — `@Transactional` is optional. EntityManager can perform reads without an explicit transaction.
- **Write operations** (`persist`, `merge`, `remove`) — `@Transactional` is required.

---

## Prerequisites

MySQL + run `../00-starter-sql-scripts/`. Ensure `student` table has data (run 01-cruddemo-student-create first, or insert manually).

---

## EntityManager — find()

```java
@Override
public Student findById(Integer id) {
    return entityManager.find(Student.class, id);
}
```

- **Signature:** `find(EntityClass, primaryKey)`
- **SQL:** `SELECT * FROM student WHERE id = ?`
- No `@Transactional` needed for read-only.

---

## EntityManager CRUD Methods

| Method | Operation |
|--------|------------|
| `persist()` | INSERT |
| `find()` | SELECT by ID (this demo) |
| `merge()` | UPDATE |
| `remove()` | DELETE |

---

## Run

```bash
./mvnw spring-boot:run
```

Flow: creates student "Daffy Duck" → saves → retrieves by ID → prints result.
