# envy

Type-safe environment variable loading for Kotlin, powered by KSP.

Annotate a configuration class with `@Envied` and envy generates loaders at compile time that read `System.getenv()` and construct your config. At runtime, call `Envy.load<YourConfig>()` to get a cached instance.

## Features

- Compile-time code generation via KSP — no reflection at runtime
- Supports Kotlin primitives and nullable types
- `@EnviedDefault` for fallback values when an environment variable is unset
- Singleton caching — repeated `load()` calls return the same instance
- Works with primary-constructor properties (`val` / `var`)

## Resolution order

For each property, envy resolves the value in this order:

1. **Environment variable** — `System.getenv(propertyName)` when set
2. **`@EnviedDefault`** — compile-time default from the annotation when the variable is unset
3. **`null`** — for nullable properties with no env var and no default

Non-null properties without an environment variable or `@EnviedDefault` cause `EnvyConfigurationException` at load time. Kotlin constructor default values are not used by generated loaders.

If a property may or may not be present in environment variables, declare it as nullable.

## Supported types

| Type | Non-null | Nullable |
|------|----------|----------|
| `String` | `System.getenv("name")!!` | `System.getenv("name")` |
| `Int` | `toInt()` | `?.toInt()` |
| `Long` | `toLong()` | `?.toLong()` |
| `Double` | `toDouble()` | `?.toDouble()` |
| `Float` | `toFloat()` | `?.toFloat()` |
| `Boolean` | `toBoolean()` | `?.toBoolean()` |
| `Byte` | `toByte()` | `?.toByte()` |
| `Short` | `toShort()` | `?.toShort()` |
| `Char` | `toCharArray().first()` | `?.toCharArray()?.firstOrNull()` |

Environment variable names match property names exactly. Invalid values (for example, a non-numeric string for an `Int` property) also throw `EnvyConfigurationException`.

## Usage

### 1. Add dependencies

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}

dependencies {
    implementation("io.github.allopensource:envy:0.1.0")
    ksp("io.github.allopensource:envy-ksp:0.1.0")
}
```

For local development in this repository, use project dependencies:

```kotlin
dependencies {
    implementation(project(":envy"))
    ksp(project(":envy-ksp"))
}
```

### 2. Define a configuration class

```kotlin
import io.github.allopensource.envy.Envied
import io.github.allopensource.envy.EnviedDefault

@Envied
class AppConfig(
    val databaseUrl: String,
    val port: Int,
    @EnviedDefault("false")
    val debug: Boolean,
    val apiKey: String?,
)
```

The class must have a primary constructor whose parameters are declared as `val` or `var` properties.

Use `@EnviedDefault` on a property to supply a fallback when the corresponding environment variable is not set. The `defaultValue` is always a string and is parsed to the property type at compile time (for example `"5432"` for an `Int`, `"false"` for a `Boolean`).

### 3. Load at runtime

```kotlin
import io.github.allopensource.envy.Envy

val config = Envy.load<AppConfig>()
println(config.databaseUrl)
```

`Envy.load()` can be called multiple times; it returns the same cached instance.

### Example

Given:

```bash
export databaseUrl="postgres://localhost/mydb"
export port=5432
```

```kotlin
@Envied
class AppConfig(
    val databaseUrl: String,
    val port: Int,
    @EnviedDefault("false")
    val debug: Boolean,
    val apiKey: String?,
)

val config = Envy.load<AppConfig>()
// databaseUrl = "postgres://localhost/mydb"  (from env)
// port = 5432                                  (from env)
// debug = false                                (from @EnviedDefault)
// apiKey = null                                (nullable, no env var)
```

## How it works

1. **Compile time** — KSP scans classes annotated with `@Envied` and generates an `EnvyLoader` implementation per class (e.g. `EnvyLoaderForAppConfig`). A `META-INF/services/io.github.allopensource.envy.EnvyLoader` file registers all loaders.
2. **Runtime** — `Envy` discovers loaders via `ServiceLoader`, maps them by config type, and caches constructed instances.

Classes without a generated loader, missing required values, or invalid env values cause `EnvyConfigurationException` when you call `Envy.load()`.

## Project structure

| Module | Description |
|--------|-------------|
| `envy` | Runtime library (`@Envied`, `@EnviedDefault`, `Envy`, `EnvyLoader`) |
| `envy-ksp` | KSP processor that generates loaders |
| `envy-tests` | Integration tests |

## Development

Requires JDK 11+.

```bash
# Build all modules
./gradlew build

# Run tests
./gradlew check

# Format Kotlin sources
./gradlew ktlintFormat

# Coverage report
./gradlew jacocoRootReport
```

Test reports are written to `envy-tests/build/reports/tests/test/index.html`. JaCoCo output is in `build/reports/jacoco/jacocoRootReport/html/`.

## License

This project is licensed under the [MIT License](LICENSE).
