# Understanding application.yml in Spring Boot

The `application.yml` (or `application.properties`) file is the **central configuration file** for Spring Boot applications. It allows you to customize your application's behavior without changing the code.

---

Spring Boot automatically loads this file at startup.

---

## 🔧 Properties Explained

### 1. Application Name

```yaml
spring:
  application:
    name: secondapp
```

- Sets a human-readable name for your application
- Used in logging, monitoring tools, and Spring Cloud services
- Helpful when running multiple microservices

---

### 2. Spring Security

```yaml
spring:
  security:
    user:
      name: admin
      password: password
```

| Property | Description |
|----------|-------------|
| `spring.security.user.name` | Default username for Basic Auth |
| `spring.security.user.password` | Default password for Basic Auth |

**What this does:**
- Enables HTTP Basic Authentication on all endpoints
- Without this, Spring Security auto-generates a random password (printed in console)
- Access protected endpoints with: `admin:password`

> ⚠️ **Security Note:** Never hardcode credentials in production! Use environment variables:
> ```yaml
> spring:
>   security:
>     user:
>       name: ${SECURITY_USER}
>       password: ${SECURITY_PASSWORD}
> ```

---

### 3. Datasource (Database Connection)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/secondapp
    username: admin
    password: password
```

**Common Database URLs:**

| Database | URL Example |
|----------|-------------|
| MySQL | `jdbc:mysql://localhost:3306/mydb` |
| PostgreSQL | `jdbc:postgresql://localhost:5432/mydb` |
| H2 (in-memory) | `jdbc:h2:mem:testdb` |
| Oracle | `jdbc:oracle:thin:@localhost:1521:xe` |

---

### 4. Server Port

```yaml
server:
  port: 8484
```

| Property | Default | Your Value |
|----------|---------|------------|
| `server.port` | 8080 | 8484 |

- Changes the embedded Tomcat server port
- Access your app at: `http://localhost:8484`
- Useful when running multiple apps simultaneously

---

### 5. Custom Properties

```yaml
person:
  name: Prateek Jha

city:
  name: New Delhi
```

You can define your own properties and inject them into your code:

#### Option A: Using `@Value` annotation

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyService {
    
    @Value("${person.name}")
    private String personName;
    
    @Value("${city.name}")
    private String cityName;
    
    public void printDetails() {
        System.out.println("Hello, " + personName + " from " + cityName);
    }
}
```

#### Option B: Using `@ConfigurationProperties` (Recommended for grouped properties)

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "person")
public class PersonProperties {
    private String name;
    
    // getters and setters
}
```

---

### 6. Actuator Endpoints

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
        exclude: beans,mapping
      base-path: /actuator
```

| Property | Description | Your Value |
|----------|-------------|------------|
| `include` | Endpoints to expose | `*` (all) |
| `exclude` | Endpoints to hide | `beans`, `mapping` |
| `base-path` | URL prefix for actuator | `/actuator` |

**Spring Boot Actuator** provides production-ready features for monitoring your app.

**Available Endpoints:**

| Endpoint | URL | Description |
|----------|-----|-------------|
| `/actuator/health` | http://localhost:8484/actuator/health | App health status |
| `/actuator/info` | http://localhost:8484/actuator/info | App information |
| `/actuator/env` | http://localhost:8484/actuator/env | Environment properties |
| `/actuator/metrics` | http://localhost:8484/actuator/metrics | App metrics |
| `/actuator/loggers` | http://localhost:8484/actuator/loggers | Logger configuration |

**Excluded Endpoints (not accessible):**
- `/actuator/beans` — Lists all Spring beans
- `/actuator/mappings` — Shows all request mappings

> ⚠️ **Security Note:** With Spring Security enabled, actuator endpoints are also protected. You'll need to authenticate with `admin:password`.

---

## 🎯 Common Properties Reference

| Category | Property | Example |
|----------|----------|---------|
| **Server** | `server.port` | `8080` |
| **Server** | `server.servlet.context-path` | `/api` |
| **Security** | `spring.security.user.name` | `admin` |
| **Security** | `spring.security.user.password` | `secret` |
| **Database** | `spring.datasource.url` | `jdbc:mysql://localhost:3306/db` |
| **Database** | `spring.datasource.username` | `root` |
| **JPA** | `spring.jpa.hibernate.ddl-auto` | `update` |
| **JPA** | `spring.jpa.show-sql` | `true` |
| **Logging** | `logging.level.root` | `INFO` |
| **Logging** | `logging.file.name` | `app.log` |

---

## 📊 YAML vs Properties Format

| YAML | Properties |
|------|------------|
| Hierarchical structure | Flat key-value pairs |
| Less repetition | More verbose |
| Supports lists natively | Lists use `[0]`, `[1]` syntax |

**Same config in `.properties` format:**
```properties
spring.application.name=secondapp
spring.security.user.name=admin
spring.security.user.password=password
spring.datasource.url=jdbc:mysql://localhost:3306/secondapp
spring.datasource.username=admin
spring.datasource.password=password
server.port=8484
person.name=Prateek Jha
city.name=New Delhi
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.exclude=beans,mapping
management.endpoints.web.base-path=/actuator
```

---

## 🔄 Profile-Specific Properties

Create environment-specific configs:

- `application-dev.yml` → Development
- `application-prod.yml` → Production

Activate a profile:
```yaml
spring:
  profiles:
    active: dev
```

Or via command line:
```bash
java -jar app.jar --spring.profiles.active=prod
```

---

## 💡 Tips

1. **Externalize sensitive data** – Use environment variables for passwords
   ```yaml
   spring:
     datasource:
       password: ${DB_PASSWORD}
   ```

2. **Default values** – Provide fallbacks in `@Value`
   ```java
   @Value("${my.property:defaultValue}")
   ```

3. **Property precedence** (highest to lowest):
   - Command line arguments
   - Environment variables
   - `application-{profile}.yml`
   - `application.yml`

---
