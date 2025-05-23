# To release for kotlin 1.9.+
1. Comment out the usage of alias(libs.plugins.kotlin.compose)
2. In libs.versions.toml set these values
```
kotlin = "1.9.25"
kotlinxSerializationJson = "1.6.3"
ktorServerCore = "2.3.13"
```
3. Use this code snippet
```kotlin
//File app/build.gradle.kts
composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
```

```kotlin
//File networkmonitor/WebSocketServerManager.kt
import io.ktor.server.application.call
import kotlinx.serialization.encodeToString

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    pingPeriod = java.time.Duration.ofSeconds(15)
}
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    timeout = java.time.Duration.ofSeconds(30)
}
```

```kotlin
// /File networkmonitor/build.gradle.kts
   configurations.all {
        resolutionStrategy.force(
            "org.jetbrains.kotlin:kotlin-stdlib-common:1.9.25", // Replace with your desired 1.9.x version
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.25",   // If you are using JDK 8 stdlib
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.25"      // If you are using the platform stdlib
        )
    }
```

# To release for kotlin 2.+
1. Uncomment out the usage of alias(libs.plugins.kotlin.compose)
2. In libs.versions.toml set these values kotlin = "2.0.20" and ktorServerCore = "3.1.2"
3. Code snippets to use
```kotlin
//File networkmonitor/WebSocketServerManager.kt
pingPeriod = 15.seconds
timeout = 30.seconds
```

```kotlin
//Remove these
configurations.all {
    resolutionStrategy.force(
        "org.jetbrains.kotlin:kotlin-stdlib-common:1.9.25", // Replace with your desired 1.9.x version
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.25",   // If you are using JDK 8 stdlib
        "org.jetbrains.kotlin:kotlin-stdlib:1.9.25"      // If you are using the platform stdlib
    )
}
```

```kotlin
kotlin = "2.0.20"
kotlinxSerializationJson = "1.8.1"
ktorServerCore = "3.1.2"
```