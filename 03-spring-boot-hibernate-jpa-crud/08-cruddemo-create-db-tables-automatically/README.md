# Auto-Create DB Tables — JPA/Hibernate DDL

Hibernate can create or update database schema from your entities using `spring.jpa.hibernate.ddl-auto`.

---

## Theory

**Schema generation from entities**
- Hibernate reads entity metadata (`@Entity`, `@Table`, `@Column`, `@Id`, etc.) and generates DDL (CREATE TABLE, ALTER TABLE).
- No manual SQL scripts needed — the schema is derived from your Java classes.
- Runs at application startup, before any queries.

**ddl-auto options**

| Value | Behavior |
|-------|----------|
| **none** | No schema changes. Default for production. |
| **validate** | Validates schema matches entities; fails if mismatch. |
| **update** | Creates missing tables/columns; **keeps existing data**. Does not drop columns. |
| **create** | Drops and recreates schema on startup. **Data is lost.** |
| **create-drop** | Like `create`, but drops schema when EntityManagerFactory closes (e.g. app shutdown). |

**When to use**
- **Development:** `update` — quick iteration; add entities/columns without scripts.
- **Production:** `none` or `validate` — use migrations (Flyway, Liquibase) instead. Never use `create`/`update` in prod.

---

## Prerequisites

MySQL running. **Database `student_tracker` must exist** (create it or run `02-student-tracker.sql`). User `springstudent` must have privileges. Tables are created automatically.

---

## Configuration

```properties
spring.jpa.hibernate.ddl-auto=update
```

- On first run: Hibernate creates the `student` table from the `Student` entity.
- On later runs: Adds new columns if you change the entity; does not drop existing columns or data.

**See the generated SQL:**
```properties
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.orm.jdbc.bind=trace
```

---

## How it works

```
@Entity
@Table(name="student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="first_name")
    private String firstName;
    // ...
}
```

Hibernate generates:
```sql
CREATE TABLE IF NOT EXISTS student (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  email VARCHAR(255),
  PRIMARY KEY (id)
);
```

- `@Table(name="student")` → table name
- `@Column(name="first_name")` → column name
- Java types → DB types (String → VARCHAR, int → INT)
- `@GeneratedValue(IDENTITY)` → AUTO_INCREMENT

---

## Run

```bash
./mvnw spring-boot:run
```

Tables are created/updated on startup. No need to run `02-student-tracker.sql` for table creation.
