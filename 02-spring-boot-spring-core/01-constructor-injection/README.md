# Constructor Injection in Spring Boot

This project demonstrates **Constructor Injection** — the recommended way to inject dependencies in Spring.

---

**Without DI:**
```java
public class DemoController {
    private Coach myCoach = new CricketCoach();  // ❌ Tightly coupled
}
```

**With DI:**
```java
public class DemoController {
    private Coach myCoach;
    
    public DemoController(Coach theCoach) {      // ✅ Loosely coupled
        myCoach = theCoach;
    }
}
```

---

## 📝 

| Annotation | Purpose |
|------------|---------|
| `@Component` | Marks class as a Spring bean (auto-detected during component scanning) |
| `@Primary` | Makes this the **default** choice when multiple beans of same type exist |

---

### 3. Constructor Injection

```java
@RestController
public class DemoController {

    // Step 1: Define a private field for the dependency
    private Coach myCoach;

    // Step 2: Define a constructor for dependency injection
    @Autowired
    public DemoController(Coach theCoach) {
        myCoach = theCoach;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return myCoach.getDailyWorkout();
    }
}
```

**How it works:**
1. Spring scans for `@Component` classes → finds `CricketCoach` and `FootballCoach`
2. When creating `DemoController`, Spring sees constructor needs a `Coach`
3. Two candidates exist, but `FootballCoach` has `@Primary` → Spring injects it
4. `myCoach.getDailyWorkout()` returns `"Practice shooting for 15 minutes"`

---

## 🔄 Injection Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                     Spring Container                             │
│                                                                  │
│   ┌──────────────┐     ┌──────────────┐                         │
│   │ CricketCoach │     │ FootballCoach│ ← @Primary              │
│   │  @Component  │     │  @Component  │                         │
│   └──────────────┘     └──────┬───────┘                         │
│                               │                                  │
│                               ▼                                  │
│                    ┌──────────────────┐                         │
│                    │  DemoController  │                         │
│                    │   @RestController│                         │
│                    │                  │                         │
│                    │  Coach myCoach ◄─┼── Injected via          │
│                    │                  │   constructor           │
│                    └──────────────────┘                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🧪 Testing

**Start the application and access:**
```
GET http://localhost:8080/dailyworkout
```

**Response:**
```
Practice shooting for 15 minutes
```

(FootballCoach is used because of `@Primary`)

---

## 💡 Why Constructor Injection?

| Type | Pros | Cons |
|------|------|------|
| **Constructor** ✅ | Immutable, testable, required deps enforced | More verbose |
| **Setter** | Optional deps, readable | Mutable, can forget to set |
| **Field** | Less code | Hard to test, hides deps |

**Spring Team Recommendation:** Use Constructor Injection for required dependencies.

---

## 🔧 Key Annotations

| Annotation | Location | Purpose |
|------------|----------|---------|
| `@Component` | Class | Marks as Spring-managed bean |
| `@Autowired` | Constructor | Tells Spring to inject dependency |
| `@Primary` | Class | Default bean when multiple candidates |
| `@RestController` | Class | Handles HTTP requests, returns data |
| `@GetMapping` | Method | Maps GET requests to method |

> 💡 **Note:** `@Autowired` is optional on constructors if there's only ONE constructor. Spring auto-detects it.

---

## ❓ What if No @Primary?

Without `@Primary`, Spring throws an error:

```
NoUniqueBeanDefinitionException: No qualifying bean of type 'Coach' available: 
expected single matching bean but found 2: cricketCoach, footballCoach
```

**Solutions:**

1. **@Primary** — Mark one as default (current approach)

2. **@Qualifier** — Specify which bean to inject:
   ```java
   @Autowired
   public DemoController(@Qualifier("cricketCoach") Coach theCoach) {
       myCoach = theCoach;
   }
   ```

---

