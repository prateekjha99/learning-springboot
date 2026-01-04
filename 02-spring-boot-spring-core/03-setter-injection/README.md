# Setter Injection in Spring Boot

This project demonstrates **Setter Injection** — an alternative to Constructor Injection for injecting dependencies.

---

## 🎯 What is Setter Injection?

Setter Injection uses a **setter method** (or any method) to inject dependencies instead of a constructor.

**Setter Injection:**
```java
@Autowired
public void setCoach(Coach theCoach) {
    myCoach = theCoach;
}
```

---

**How it works:**
1. Spring creates `DemoController` instance (with default constructor)
2. Spring looks for `@Autowired` methods
3. Spring calls `setCoach()` and passes in the `Coach` bean
4. Dependency is now available to use

---

## 💡 Method Name Can Be Anything!

The method doesn't have to follow the `setXxx` naming convention:

```java
// ✅ This works too!
@Autowired
public void doSomeStuff(Coach theCoach) {
    myCoach = theCoach;
}
```

Spring only cares about `@Autowired` — the method name is just for readability.

---

## ⚖️ Constructor vs Setter Injection

| Aspect | Constructor | Setter |
|--------|-------------|--------|
| **When injected** | During object creation | After object creation |
| **Required deps** | ✅ Enforced | ❌ Can be null |
| **Immutability** | ✅ Fields can be `final` | ❌ Fields must be mutable |
| **Testability** | ✅ Easy to mock | ✅ Easy to mock |
| **Optional deps** | ❌ Not ideal | ✅ Good fit |
| **Circular deps** | ❌ Fails fast | ⚠️ May work (not recommended) |

---

## 🎯 When to Use Each

### Use Constructor Injection When:
```java
// Required dependency — app can't work without it
private final Coach myCoach;  // Can be final!

@Autowired
public DemoController(Coach theCoach) {
    this.myCoach = theCoach;
}
```
- Dependency is **required**
- You want **immutability** (`final` fields)
- You want to **fail fast** if dependency missing

### Use Setter Injection When:
```java
// Optional dependency — app works without it
private Coach myCoach;

@Autowired(required = false)  // Won't fail if no bean found
public void setCoach(Coach theCoach) {
    this.myCoach = theCoach;
}
```
- Dependency is **optional**
- You need to **change** dependency at runtime
- You have **circular dependencies** (avoid if possible)

---

## 🏭 Real-World Example

```java
@Service
public class NotificationService {

    private EmailSender emailSender;      // Required
    private SmsSender smsSender;          // Optional

    // Constructor for required dependency
    @Autowired
    public NotificationService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    // Setter for optional dependency
    @Autowired(required = false)
    public void setSmsSender(SmsSender smsSender) {
        this.smsSender = smsSender;
    }

    public void notify(String message) {
        emailSender.send(message);           // Always works
        if (smsSender != null) {
            smsSender.send(message);         // Only if available
        }
    }
}
```

---

### 3. Expecting Final Fields

```java
// ❌ Won't compile — can't set final field in setter
private final Coach myCoach;

@Autowired
public void setCoach(Coach theCoach) {
    this.myCoach = theCoach;  // Error: cannot assign to final
}
```

---

## 📊 Spring Team Recommendation

> **Use Constructor Injection for mandatory dependencies.**  
> **Use Setter Injection for optional dependencies.**

The Spring team recommends **Constructor Injection** as the default because:
- Dependencies are explicit
- Objects are immutable after creation
- Required dependencies are enforced
- Easier to test

---

## 🧪 Testing

```bash
GET http://localhost:8080/dailyworkout
```

**Response:**
```
Practice fast bowling for 15 minutes
```

---

## 💡 Key Takeaways

1. **Setter Injection** uses `@Autowired` on a method (not constructor)
2. **Method name** doesn't matter — `@Autowired` is what counts
3. **Constructor = required deps**, **Setter = optional deps**
4. Setter-injected fields **cannot be `final`**
5. **Spring recommends** Constructor Injection as the default

