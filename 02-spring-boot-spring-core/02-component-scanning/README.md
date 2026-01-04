# Component Scanning in Spring Boot

This project demonstrates how **Component Scanning** works and how to configure it when beans are in different packages.

---

## 🎯 What is Component Scanning?

Component Scanning is Spring's mechanism to automatically discover and register beans (classes annotated with `@Component`, `@Service`, `@Repository`, `@Controller`, etc.).

---

## 📦 The Problem

```
com/luv2code/
├── springcoredemo/                    ← @SpringBootApplication is here
│   ├── SpringcoredemoApplication.java
│   └── rest/
│       └── DemoController.java        ✅ Found (sub-package)
│
└── util/                              ← Different package tree!
    └── common/
        ├── Coach.java
        └── CricketCoach.java          ❌ NOT found by default!
```

**By default**, `@SpringBootApplication` only scans:
- The package where it's located (`com.luv2code.springcoredemo`)
- All sub-packages (`com.luv2code.springcoredemo.rest`, etc.)

`CricketCoach` is in `com.luv2code.util.common` — a **sibling package**, not a sub-package!

---

## ✅ The Solution: scanBasePackages

```java
@SpringBootApplication(
    scanBasePackages = {"com.luv2code.springcoredemo",
                        "com.luv2code.util"})
public class SpringcoredemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringcoredemoApplication.class, args);
    }
}
```

Now Spring scans **both** package trees:

| Package | What's Found |
|---------|--------------|
| `com.luv2code.springcoredemo` | `SpringcoredemoApplication`, `DemoController` |
| `com.luv2code.util` | `CricketCoach` |

---

## ⚠️ What Happens Without scanBasePackages?

```java
// ❌ This will fail!
@SpringBootApplication  // Only scans com.luv2code.springcoredemo
public class SpringcoredemoApplication { ... }
```

**Error:**
```
***************************
APPLICATION FAILED TO START
***************************

Description:
Parameter 0 of constructor in com.luv2code.springcoredemo.rest.DemoController 
required a bean of type 'com.luv2code.util.common.Coach' that could not be found.

Action:
Consider defining a bean of type 'com.luv2code.util.common.Coach' in your configuration.
```

Spring can't find `CricketCoach` because it's outside the default scan path!

---

## 🛠️ Alternative Solutions

### Option 1: scanBasePackages (Current Approach)

```java
@SpringBootApplication(
    scanBasePackages = {"com.luv2code.springcoredemo", "com.luv2code.util"})
```

### Option 2: scanBasePackageClasses

```java
@SpringBootApplication(
    scanBasePackageClasses = {SpringcoredemoApplication.class, CricketCoach.class})
```
Type-safe alternative — uses classes to determine packages.

### Option 3: @ComponentScan Annotation

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.luv2code.springcoredemo", "com.luv2code.util"})
public class SpringcoredemoApplication { ... }
```

### Option 4: Move Classes to Sub-packages (Simplest)

Restructure to keep everything under one root:
```
com.luv2code.springcoredemo/
├── SpringcoredemoApplication.java
├── rest/
│   └── DemoController.java
└── common/                    ← Move here!
    ├── Coach.java
    └── CricketCoach.java
```

---

## 🎯 Best Practices

| Practice | Recommendation |
|----------|----------------|
| **Package Structure** | Keep all code under main app package when possible |
| **Multi-module Projects** | Use `scanBasePackages` for shared libraries |
| **Performance** | Avoid scanning too many packages (slows startup) |
| **Clarity** | Use `scanBasePackageClasses` for type-safety |

---

## 📊 Stereotypes That Trigger Scanning

All these annotations make a class discoverable:

| Annotation | Purpose |
|------------|---------|
| `@Component` | Generic Spring bean |
| `@Service` | Business logic layer |
| `@Repository` | Data access layer |
| `@Controller` | MVC controller |
| `@RestController` | REST API controller |
| `@Configuration` | Configuration class |

They're all **specializations** of `@Component`.

---

## 💡 Key Takeaways

1. **Default scanning** = main app package + sub-packages only
2. **scanBasePackages** = explicitly tell Spring where to look
3. **Bean not found errors** often mean scanning misconfiguration
4. **Keep packages organized** under one root to avoid complexity
5. **Multiple packages** work fine with proper configuration

