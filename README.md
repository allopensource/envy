<p align="center">
  <img src="assets/logo.png" alt="envy logo" width="512" />
</p>
<h1 align="center">Type-safe, reflection-free environment configuration for Kotlin, powered by KSP.</h1>

**Website:** [allopensource.github.io/envy](https://allopensource.github.io/envy)

### Usage: Super simple and easy to use.

Assuming the following environment variables.
```bash
export databaseUrl="postgres://localhost/mydb"
export port=5432
export apiKey="my-api-key"
```

1. Declare a configuration class with the environment variables and annotate it with envy annotations.
```kotlin
@Envied
class AppConfig(
    val databaseUrl: String,
    val port: Int,
    @EnviedDefault("false")
    val debug: Boolean,
    val apiKey: String?,
)
```

2. Load the configuration class with envy.
```kotlin
val config = Envy.load<AppConfig>()
// databaseUrl = "postgres://localhost/mydb"    (from env)
// port = 5432                                  (from env)
// debug = false                                (from @EnviedDefault)
// apiKey = null                                (nullable, no env var)
```

### Dependencies

```kotlin
plugins {
    id("com.google.devtools.ksp") // to enable KSP
}

dependencies {
    implementation("io.github.allopensource:envy:0.1.0") // runtime component
    ksp("io.github.allopensource:envy-ksp:0.1.0")        // compiletime component
}
```

## Features

- Compile-time code generation via KSP — no reflection at runtime
- Supports Kotlin primitives and nullable types
- Supports default values with `@EnviedDefault`
- Singleton caching — repeated `load()` calls return the same instance

## Resolution order

For each property, envy resolves the value in this order:

1. **Environment variable** — `System.getenv(propertyName)` when set
2. **`@EnviedDefault`** — compile-time default from the annotation when the variable is unset
3. **`null`** — for nullable properties with no environment variable set and no default value

⚠️ Non-null properties without an environment variable or `@EnviedDefault` cause `EnvyConfigurationException` at runtime. Generated loaders do not use Kotlin constructor default values. If a property may or may not be present in environment variables, declare it as nullable.

## Supported types

| Type |
|------|
| `String` |
| `Int` |
| `Long` |
| `Double` |
| `Float` |
| `Boolean`
| `Byte` |
| `Short` |
| `Char` |

Environment variable names match property names exactly. Invalid values (for example, a non-numeric string for an `Int` property) also throw `EnvyConfigurationException`.

## Usage

The class must have a primary constructor whose parameters are declared as `val` or `var` properties.

Use `@EnviedDefault` on a property to supply a fallback when the corresponding environment variable is not set. The `defaultValue` is always a string and is parsed to the property type at compile time (for example `"5432"` for an `Int`, `"false"` for a `Boolean`).

### 3. Load at runtime

```kotlin
import io.github.allopensource.envy.Envy

val config = Envy.load<AppConfig>()
println(config.databaseUrl)
```

`Envy.load()` can be called multiple times; it returns the same cached instance.


## How it works

1. **Compile time** — [KSP](https://kotlinlang.org/docs/ksp-overview.html) scans classes annotated with `@Envied` and generates an `EnvyLoader` implementation per class. The loader is a source code file (not class file) generated in `build/generated/ksp`, the corresponding class files are generated under `build/classes`. 
The loader contains the code which reads the environment variables and returns the instance of the annotated class. An entry for the loader is added to the  `META-INF/services/io.github.allopensource.envy.EnvyLoader` file.
2. **Runtime** — `Envy` discovers loaders via [ServiceLoader](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ServiceLoader.html) on startup. When `Envy.load..` is called, the relevant loader is identified by the config type and used to construct the instance.
The instance is cached so that .load() can be called multiple times in the application.

Classes without a generated loader, missing required values, or invalid env values cause `EnvyConfigurationException` when you call `Envy.load()`.

## Project structure

| Module | Description |
|--------|-------------|
| `envy` | Runtime library (`@Envied`, `@EnviedDefault`, `Envy`, `EnvyLoader`) |
| `envy-ksp` | KSP processor that generates loaders |
| `envy-tests` | Integration tests |

## Development
Requires JDK 17+.

```bash
# Build all modules
./gradlew build

# Run tests
./gradlew test

# Format Kotlin sources
./gradlew ktlintFormat

# Coverage report
./gradlew jacocoRootReport
```

Test reports are written to `envy-tests/build/reports/tests/test/index.html`. JaCoCo output is in `build/reports/jacoco/jacocoRootReport/html/`.

## Community & feedback

If you're using envy — or trying it out — I'd love to hear from you. Feedback helps prioritize fixes, new features, and documentation.

- **[Issues](https://github.com/allopensource/envy/issues)** — report bugs, request features, or ask questions
- **[Discussions](https://github.com/allopensource/envy/discussions)** — share how you're using envy, suggest improvements, or say hello

See [CONTRIBUTING.md](CONTRIBUTING.md) for development setup, coding guidelines, and how to submit pull requests.

## License

This project is licensed under the [MIT License](LICENSE).
