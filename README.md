# Getting Started

Snorkl is distributed through Github. To use it you need to add the following

1. Maven url dependency to the settings.gradle.kts
File: settings.gradle.kts
```groovy
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/rahul-lohra/Snorkl")
        }
    }
}
```
2. In app/build.gradle.kts file of your android app module (NOT the root file).

File: app/build.gradle.kts
```groovy
implementation("rahul.lohra.snorkl:snorkl:0.0.2-kotlin1.9")
```

## To monitor HTTP REST Requests

To start using Snorkl, just plug in a new `rahul.lohra.snorkl.network.NetworkLoggerInterceptor` to your OkHttp Client Builder:

```kotlin
val client = OkHttpClient.Builder()
    .addInterceptor(rahul.lohra.snorkl.network.NetworkLoggerInterceptor())
    .build()
```
## To monitor WEBSOCKET Network Requests

Just plug in a new `rahul.lohra.snorkl.network.NetworkWebSocketListener` to your OkHttp Client Builder:

```kotlin
    val request = Request.Builder().url(WEBSOCKET_URL).build()
    val listener = rahul.lohra.snorkl.network.NetworkWebSocketListener()
    webSocket = client.newWebSocket(request, listener)
```


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