# Understanding application.properties in Spring Boot

The `application.properties` file is the **central configuration file** for Spring Boot applications. It allows you to customize your application's behavior without changing the code.

---

Spring Boot automatically loads this file at startup.

---

## 🔧 Properties Explained

### 1. Application Name

```properties
spring.application.name=secondapp
```

- Sets a human-readable name for your application
- Used in logging, monitoring tools, and Spring Cloud services
- Helpful when running multiple microservices

---

### 2. Server Port

```properties
server.port=8484
```

| Property | Default | Your Value |
|----------|---------|------------|
| `server.port` | 8080 | 8484 |

- Changes the embedded Tomcat server port
- Access your app at: `http://localhost:8484`
- Useful when running multiple apps simultaneously

---

### 3. Actuator Endpoints

```properties
management.endpoints.web.exposure.include=*
```

**Spring Boot Actuator** provides production-ready features for monitoring your app.

| Endpoint | URL | Description |
|----------|-----|-------------|
| `/actuator/health` | http://localhost:8484/actuator/health | App health status |
| `/actuator/info` | http://localhost:8484/actuator/info | App information |
| `/actuator/beans` | http://localhost:8484/actuator/beans | All Spring beans |
| `/actuator/env` | http://localhost:8484/actuator/env | Environment properties |
| `/actuator/metrics` | http://localhost:8484/actuator/metrics | App metrics |

> ⚠️ **Security Note:** Using `*` exposes ALL endpoints. In production, expose only what you need:
> ```properties
> management.endpoints.web.exposure.include=health,info,metrics
> ```

---

### 4. Custom Properties

```properties
person.name=Prateek Jha
city.name=New Delhi
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

## 🎯 Common Properties Reference

| Category | Property | Example |
|----------|----------|---------|
| **Server** | `server.port` | `8080` |
| **Server** | `server.servlet.context-path` | `/api` |
| **Logging** | `logging.level.root` | `INFO` |
| **Logging** | `logging.file.name` | `app.log` |
| **Database** | `spring.datasource.url` | `jdbc:mysql://localhost:3306/db` |
| **Database** | `spring.datasource.username` | `root` |
| **JPA** | `spring.jpa.hibernate.ddl-auto` | `update` |
| **JPA** | `spring.jpa.show-sql` | `true` |

---

## 📚 Alternative: YAML Format

You can also use `application.yml` instead:

```yaml
spring:
  application:
    name: secondapp

server:
  port: 8484

management:
  endpoints:
    web:
      exposure:
        include: "*"

person:
  name: Prateek Jha

city:
  name: New Delhi
```

---

## 🔄 Profile-Specific Properties

Create environment-specific configs:

- `application-dev.properties` → Development
- `application-prod.properties` → Production

Activate a profile:
```properties
spring.profiles.active=dev
```

---

## 💡 Tips

1. **Externalize sensitive data** – Use environment variables for passwords
   ```properties
   spring.datasource.password=${DB_PASSWORD}
   ```

2. **Default values** – Provide fallbacks in `@Value`
   ```java
   @Value("${my.property:defaultValue}")
   ```
---
