# Java Configuration Bean in Spring Boot

This project demonstrates **Java-based Configuration** — an alternative to component scanning using `@Configuration` and `@Bean` annotations.

---

## 🎯 Two Ways to Create Beans

| Approach | Annotation | Where |
|----------|------------|-------|
| **Component Scanning** | `@Component` | On the class itself |
| **Java Configuration** | `@Bean` | In a `@Configuration` class |

---

## 📦 When to Use Each

### Use @Component (Component Scanning) When:
- You **own the source code**
- Class is part of your application
- Simple, automatic bean registration

### Use @Bean (Java Config) When:
- You **don't own the source code** (3rd party library)
- You need **custom instantiation logic**
- You want **explicit control** over bean creation
- Class comes from external JAR (can't add @Component to it)


| Element | Purpose |
|---------|---------|
| `@Configuration` | Marks class as source of bean definitions |
| `@Bean` | Method return value becomes a Spring bean |
| `@Bean("aquatic")` | Custom bean name (default would be method name: `swimCoach`) |

---

## 🏷️ Bean Naming

### Default Name = Method Name

```java
@Bean
public Coach swimCoach() {  // Bean name: "swimCoach"
    return new SwimCoach();
}
```

### Custom Name

```java
@Bean("aquatic")  // Bean name: "aquatic"
public Coach swimCoach() {
    return new SwimCoach();
}
```

### Multiple Names (Aliases)

```java
@Bean({"aquatic", "waterCoach", "swimmerCoach"})
public Coach swimCoach() {
    return new SwimCoach();
}
```

---

## 🏭 Real-World Use Cases

### 1. Third-Party Library Classes

```java
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        // Can't add @Component to HikariDataSource — it's from a JAR!
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        ds.setUsername("admin");
        ds.setPassword("password");
        return ds;
    }
}
```

---

### 2. Conditional Bean Creation

```java
@Configuration
public class ServiceConfig {

    @Bean
    public PaymentService paymentService() {
        if (isProduction()) {
            return new StripePaymentService();
        } else {
            return new MockPaymentService();
        }
    }
}
```


---

## ⚖️ @Component vs @Bean Comparison

| Aspect | @Component | @Bean |
|--------|------------|-------|
| **Where** | On the class | In @Configuration method |
| **Auto-detection** | Yes (component scan) | No (explicit) |
| **3rd party classes** | ❌ Can't modify | ✅ Works perfectly |
| **Custom logic** | Limited | Full control |
| **Verbosity** | Less code | More code |
| **Default name** | Class name (lowercase) | Method name |


---

## 💡 Key Takeaways

1. **@Configuration** = Class that contains bean definitions
2. **@Bean** = Method that creates and returns a bean
3. **Use Java Config** when you can't modify the class (3rd party)
4. **Bean name** defaults to method name, or use `@Bean("customName")`
5. **@Qualifier** references the bean by name
6. **Both approaches** work together in the same application

