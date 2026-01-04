# Bean Lifecycle Methods in Spring Boot

This project demonstrates **Bean Lifecycle Methods** — hooks that let you execute custom code during bean initialization and destruction.

---

## 🎯 What is Bean Lifecycle?

Every Spring bean goes through a lifecycle:

```
Container Started
       │
       ▼
┌─────────────────┐
│  Bean Created   │  ← Constructor called
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Dependencies    │  ← @Autowired fields/setters
│   Injected      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ @PostConstruct  │  ← Initialization hook
│    Method       │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Bean Ready    │  ← Available for use
│    for Use      │
└────────┬────────┘
         │
    (app running)
         │
         ▼
┌─────────────────┐
│  @PreDestroy    │  ← Cleanup hook
│    Method       │
└────────┬────────┘
         │
         ▼
   Container Shutdown
```

---

## 📝 Key Annotations

| Annotation | When Called | Use Case |
|------------|-------------|----------|
| `@PostConstruct` | After constructor + dependency injection | Setup, load data, open connections |
| `@PreDestroy` | Before bean is destroyed | Cleanup, close connections, release resources |

---

## 🖥️ Console Output

### On Application Startup:

```
In constructor: BaseballCoach
In constructor: CricketCoach
In doMyStartupStuff(): CricketCoach    ← @PostConstruct called
In constructor: TennisCoach
In constructor: TrackCoach
In constructor: DemoController
```

### On Application Shutdown (Ctrl+C):

```
In doMyCleanupStuff(): CricketCoach    ← @PreDestroy called
```

---

## 📋 Lifecycle Order

```
1. Constructor                     new CricketCoach()
2. Dependency Injection            @Autowired fields set
3. @PostConstruct                  doMyStartupStuff()
4. Bean Ready for Use              ✅
   ... application running ...
5. @PreDestroy                     doMyCleanupStuff()
6. Bean Destroyed                  🗑️
```

---

## 🎯 Common Use Cases

### @PostConstruct — Initialization

```java
@Component
public class DatabaseService {

    @Autowired
    private DataSource dataSource;

    private Connection connection;

    @PostConstruct
    public void init() {
        // Dependencies are available here!
        connection = dataSource.getConnection();
        System.out.println("Database connection established");
    }
}
```

**Use for:**
- Loading configuration from database
- Establishing connections
- Initializing caches
- Validating dependencies
- Starting background tasks

---

### @PreDestroy — Cleanup

```java
@Component
public class FileService {

    private FileWriter writer;

    @PostConstruct
    public void init() {
        writer = new FileWriter("log.txt");
    }

    @PreDestroy
    public void cleanup() {
        writer.flush();
        writer.close();
        System.out.println("File writer closed");
    }
}
```

**Use for:**
- Closing database connections
- Flushing buffers
- Releasing file handles
- Stopping background threads
- Saving state before shutdown

---

## ⚠️ Important Notes

---

### 1. Prototype Scope — @PreDestroy Not Called!

```java
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeBean {

    @PostConstruct
    public void init() {
        System.out.println("PostConstruct called");  // ✅ Called
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PreDestroy called");     // ❌ NOT called!
    }
}
```

**Why?** Spring doesn't track prototype beans after creation — you're responsible for cleanup.

---

## 🔄 Alternative: InitializingBean & DisposableBean

```java
@Component
public class MyBean implements InitializingBean, DisposableBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        // Same as @PostConstruct
        System.out.println("Initialization logic");
    }

    @Override
    public void destroy() throws Exception {
        // Same as @PreDestroy
        System.out.println("Cleanup logic");
    }
}
```

**Comparison:**

| Approach | Pros | Cons |
|----------|------|------|
| `@PostConstruct/@PreDestroy` | Cleaner, annotation-based | Requires jakarta import |
| `InitializingBean/DisposableBean` | No imports needed | Couples code to Spring |

**Recommendation:** Use `@PostConstruct` and `@PreDestroy` — cleaner and more flexible.

---

## 🏭 Real-World Example

```java
@Component
public class CacheManager {

    private Map<String, Object> cache;

    @Autowired
    private CacheLoader loader;

    @PostConstruct
    public void warmUpCache() {
        cache = new ConcurrentHashMap<>();
        
        // Load frequently accessed data
        List<User> users = loader.loadActiveUsers();
        users.forEach(u -> cache.put("user:" + u.getId(), u));
        
        System.out.println("Cache warmed up with " + cache.size() + " entries");
    }

    @PreDestroy
    public void persistCache() {
        // Save dirty entries to database
        loader.saveModifiedEntries(cache);
        cache.clear();
        
        System.out.println("Cache persisted and cleared");
    }

    public Object get(String key) {
        return cache.get(key);
    }
}
```

---

## 📊 Complete Lifecycle Summary

| Phase | What Happens | Hook |
|-------|--------------|------|
| 1. Instantiation | `new Bean()` | Constructor |
| 2. Populate Properties | `@Autowired` injection | — |
| 3. BeanNameAware | `setBeanName()` | Interface |
| 4. BeanFactoryAware | `setBeanFactory()` | Interface |
| 5. Pre-Initialization | `BeanPostProcessor.postProcessBeforeInitialization()` | — |
| 6. **Initialization** | **`@PostConstruct`** | ✅ |
| 7. Post-Initialization | `BeanPostProcessor.postProcessAfterInitialization()` | — |
| 8. Bean Ready | Available for injection | — |
| 9. **Destruction** | **`@PreDestroy`** | ✅ |

---

## 💡 Key Takeaways

1. **@PostConstruct** = Called after constructor + DI, before bean is used
2. **@PreDestroy** = Called before bean is destroyed (shutdown)
3. Methods must be **void** and **no parameters**
4. Use `jakarta.annotation` package (Spring Boot 3.x)
5. **@PreDestroy not called** for prototype-scoped beans
6. Great for **setup/teardown** logic (connections, caches, resources)

