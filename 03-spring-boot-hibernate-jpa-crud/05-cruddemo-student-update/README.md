# Student Update — JPA/Hibernate

Updating records with JPA `EntityManager.merge()`.

---

## Theory

**Managed vs Detached Entities**
- **Managed** — Entity is in the persistence context; Hibernate tracks it. Changes (setters) are detected automatically and synced to DB on transaction commit (**dirty checking**).
- **Detached** — Entity was loaded but is no longer in the persistence context (e.g. from a previous `find()` after that operation ended). Changes are **not** auto-synced.

**When to use merge()**
- Use `merge()` when the entity is **detached** — e.g. loaded in one DAO call, modified, then passed to another.
- `merge()` copies the detached entity's state into the persistence context and returns a managed entity. On commit, Hibernate generates `UPDATE`.

**merge() vs modifying a managed entity**
- **Managed:** Just call setters; no `merge()` needed. Dirty checking persists changes.
- **Detached:** Must call `merge()` to re-attach and persist changes.

---

## EntityManager — merge()

```java
@Override
@Transactional
public void update(Student theStudent) {
    entityManager.merge(theStudent);
}
```

- **`@Transactional`** — Required; updates are write operations.
- **merge(theStudent)** — Takes detached entity; copies state into persistence context; Hibernate issues `UPDATE` on commit.
- **Note:** `merge()` returns the managed entity. The passed-in `theStudent` stays detached; use the return value if you need the managed reference.

---

## Typical Update Flow

```java
// 1. Load (entity becomes detached when findById returns)
Student myStudent = studentDAO.findById(1);

// 2. Modify
myStudent.setFirstName("John");

// 3. Persist changes (merge re-attaches and syncs)
studentDAO.update(myStudent);
```

---

## EntityManager CRUD Methods

| Method | Operation |
|--------|------------|
| `persist()` | INSERT |
| `find()` | SELECT by ID |
| `merge()` | UPDATE (this demo) |
| `remove()` | DELETE |

---

## Run

```bash
./mvnw spring-boot:run
```

Loads student id=1, changes firstName to "John", updates DB.
