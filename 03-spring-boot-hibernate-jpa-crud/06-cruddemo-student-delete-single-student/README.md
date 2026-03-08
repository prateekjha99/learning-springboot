# Student Delete — JPA/Hibernate

Deleting a single record with JPA `EntityManager.remove()`.

---

## Theory

**remove() requires a managed entity**
- `remove()` operates on the persistence context; the entity must be **managed**.
- You cannot pass a detached entity or a plain object — it must be loaded (e.g. via `find()`) in the same transaction.

**Why find() then remove()?**
- When you only have an ID, you must load the entity first so it becomes managed.
- `find()` loads it into the persistence context; `remove()` marks it for deletion. On commit, Hibernate issues `DELETE`.

**Entity states and remove()**
- **Managed** → `remove()` → **Removed** (scheduled for DELETE on commit).
- After commit, the entity is detached and the DB row is gone.
- Passing a detached entity to `remove()` throws `IllegalArgumentException`.

**Alternative: JPQL DELETE**
- For bulk delete by ID without loading entities, use `executeUpdate()` with a JPQL `DELETE` query (see demo 07).

---

## Prerequisites

MySQL + run `../00-starter-sql-scripts/`. Ensure `student` table has data.

---

## EntityManager — remove()

```java
@Override
@Transactional
public void delete(Integer id) {
    Student theStudent = entityManager.find(Student.class, id);
    entityManager.remove(theStudent);
}
```

- **`@Transactional`** — Required; delete is a write operation.
- **find()** — Loads entity into persistence context (makes it managed).
- **remove()** — Marks entity for deletion; Hibernate issues `DELETE FROM student WHERE id=?` on commit.

---

## remove() vs JPQL DELETE

| Approach | Use case |
|----------|----------|
| **find() + remove()** | Single entity; need to load first (e.g. for validation, cascades) |
| **JPQL DELETE** | Bulk delete by ID; no need to load entities |

---

## EntityManager CRUD Methods

| Method | Operation |
|--------|------------|
| `persist()` | INSERT |
| `find()` | SELECT by ID |
| `merge()` | UPDATE |
| `remove()` | DELETE (this demo) |

---

## Run

```bash
./mvnw spring-boot:run
```

Deletes student with id=3 from the database.
