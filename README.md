# Learning Spring Boot

---

## 📁 01-spring-boot-overview

Getting started with Spring Boot basics.

| Project | Description |
|---------|-------------|
| `01-firstapp` | Basic Spring Boot app with REST controller and `@SpringBootApplication` |
| `02-properties-actuators-security` | `application.yml`, custom properties, Actuator endpoints, Spring Security basics |

---

## 📁 02-spring-boot-spring-core

Deep dive into Spring's Inversion of Control (IoC) and Dependency Injection (DI).

| Project | Description |
|---------|-------------|
| `01-constructor-injection` | Inject dependencies via constructor using `@Autowired` |
| `02-component-scanning` | Configure `scanBasePackages` for beans in different packages |
| `03-setter-injection` | Inject dependencies via setter methods |
| `04-qualifiers` | Use `@Qualifier` to select specific bean when multiple exist |
| `05-primary` | Use `@Primary` to set default bean among multiple candidates |
| `06-lazy-initialization` | Use `@Lazy` to defer bean creation until first use |
| `07-bean-scopes` | `singleton` vs `prototype` scope with `@Scope` |
| `08-bean-lifecycle-methods` | `@PostConstruct` and `@PreDestroy` hooks |
| `09-java-config-bean` | Create beans with `@Configuration` and `@Bean` for 3rd party classes |

---

## 📁 03-spring-boot-hibernate-jpa-crud

JPA/Hibernate with MySQL — EntityManager, entities, JPQL, CRUD.

| Project | Description |
|---------|-------------|
| `01-cruddemo-student-create` | `persist()` — INSERT |
| `02-cruddemo-student-read` | `find()` — SELECT by ID |
| `03-cruddemo-student-query-findAll` | JPQL — SELECT all |
| `04-cruddemo-student-query-findByLastName` | JPQL WHERE + named params |
| `05-cruddemo-student-update` | `merge()` — UPDATE |
| `06-cruddemo-student-delete-single-student` | `remove()` — DELETE single |
| `07-cruddemo-student-query-delete-all-students` | JPQL DELETE — bulk delete |
| `08-cruddemo-create-db-tables-automatically` | `ddl-auto` — schema from entities |

---
