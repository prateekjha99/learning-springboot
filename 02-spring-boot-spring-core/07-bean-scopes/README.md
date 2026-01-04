# Bean Scopes in Spring Boot

This project demonstrates **Bean Scopes** — how Spring manages the lifecycle and number of instances of beans.

---

## 🎯 What is Bean Scope?

Bean Scope defines:
- **How many instances** of a bean are created
- **When** new instances are created
- **How long** they live

---

## 📊 Available Scopes

| Scope | Description | Instances |
|-------|-------------|-----------|
| **singleton** | One instance per Spring container (DEFAULT) | 1 |
| **prototype** | New instance every time requested | Many |
| **request** | One instance per HTTP request (web only) | Per request |
| **session** | One instance per HTTP session (web only) | Per session |
| **application** | One instance per ServletContext (web only) | 1 per app |

---

## 🔍 Singleton vs Prototype (Most Common)

### Singleton (Default)

```java
@Component
// @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)  ← Implicit default
public class BaseballCoach implements Coach {
    public BaseballCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }
}
```

**Behavior:**
- Spring creates **ONE instance** at startup
- **Same instance** is shared across all injection points
- Constructor called **once**

---

### Prototype

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CricketCoach implements Coach {
    public CricketCoach() {
        System.out.println("In constructor: " + getClass().getSimpleName());
    }
}
```

**Behavior:**
- Spring creates **NEW instance** every time bean is requested
- **Different instances** at each injection point
- Constructor called **multiple times**

---

**Results:**

| Scope | `/check` Response | Explanation |
|-------|-------------------|-------------|
| Singleton | `myCoach == anotherCoach, true` | Same instance |
| Prototype | `myCoach == anotherCoach, false` | Different instances |

---

## 📝 How to Set Scope

### Option 1: Using ConfigurableBeanFactory Constants (Recommended)

```java
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CricketCoach implements Coach { }
```

### Option 2: Using String Value

```java
@Component
@Scope("prototype")
public class CricketCoach implements Coach { }
```

**Available Constants:**
```java
ConfigurableBeanFactory.SCOPE_SINGLETON   // "singleton"
ConfigurableBeanFactory.SCOPE_PROTOTYPE   // "prototype"
WebApplicationContext.SCOPE_REQUEST       // "request"
WebApplicationContext.SCOPE_SESSION       // "session"
WebApplicationContext.SCOPE_APPLICATION   // "application"
```

---

## 🔄 Startup Console Output

When the application starts:

```
In constructor: BaseballCoach      ← Singleton - created at startup
In constructor: TennisCoach        ← Singleton - created at startup
In constructor: TrackCoach         ← Singleton - created at startup
In constructor: CricketCoach       ← Prototype - created when injected
In constructor: CricketCoach       ← Prototype - created again (2nd injection)
In constructor: DemoController
```

**Notice:** `CricketCoach` constructor is called **twice** (for each injection point).

---

## ⚖️ When to Use Each Scope

### Use Singleton When:
- Bean is **stateless** (no instance-specific data)
- Bean is **shared** across application
- Bean is **expensive to create** (database connections, caches)
- You want **consistent behavior** everywhere

**Examples:** Services, Repositories, Configuration, Utilities

```java
@Service  // Singleton by default
public class UserService {
    // Stateless - safe to share
    public User findById(Long id) { ... }
}
```

---

### Use Prototype When:
- Bean is **stateful** (holds user-specific data)
- Each use needs a **fresh instance**
- Bean should **not be shared**

**Examples:** Shopping carts, Form beans, User sessions

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShoppingCart {
    private List<Item> items = new ArrayList<>();  // Stateful!
    
    public void addItem(Item item) {
        items.add(item);
    }
}
```

---

## ⚠️ Prototype Gotcha: Injection into Singleton

```java
@Service  // Singleton
public class OrderService {

    private final ShoppingCart cart;  // Prototype

    @Autowired
    public OrderService(ShoppingCart cart) {
        this.cart = cart;  // ⚠️ Same cart instance forever!
    }
}
```

**Problem:** Prototype bean injected into singleton only gets created **once** (at singleton creation time).

**Solution:** Use `ObjectFactory` or `Provider`:

```java
@Service
public class OrderService {

    private final ObjectFactory<ShoppingCart> cartFactory;

    @Autowired
    public OrderService(ObjectFactory<ShoppingCart> cartFactory) {
        this.cartFactory = cartFactory;
    }

    public void processOrder() {
        ShoppingCart cart = cartFactory.getObject();  // Fresh instance each time!
    }
}
```

---

## 🌐 Web-Specific Scopes

```java
// One per HTTP request
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedBean { }

// One per HTTP session
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedBean { }
```

**Use cases:**
- **Request scope:** Request logging, request-specific calculations
- **Session scope:** User preferences, shopping cart, login state

---

## 📊 Scope Comparison Summary

| Aspect | Singleton | Prototype |
|--------|-----------|-----------|
| **Instances** | 1 | Many |
| **Created** | At startup (eager) | On demand (lazy) |
| **Shared** | Yes | No |
| **Thread-safe** | Must be | N/A (not shared) |
| **Memory** | Low | Higher |
| **Use for** | Stateless services | Stateful objects |

---

## 🧪 Testing

**Endpoint 1:** Get daily workout
```bash
GET http://localhost:8080/dailyworkout
```
Response: `Practice fast bowling for 15 minutes`

**Endpoint 2:** Check if beans are same instance
```bash
GET http://localhost:8080/check
```
Response: `Comparing beans: myCoach == anotherCoach, false`

(Returns `false` because `CricketCoach` is prototype scope)

---

## 💡 Key Takeaways

1. **Singleton** (default) = One instance shared everywhere
2. **Prototype** = New instance every time requested
3. Use `@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)` to change scope
4. **Stateless beans** → Singleton
5. **Stateful beans** → Prototype
6. Watch out for **prototype injected into singleton** — use `ObjectFactory`

