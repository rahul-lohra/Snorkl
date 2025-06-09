# Getting Started

Snorkl is distributed through Github. To use it you need to add the following

1. Maven url dependency to the `settings.gradle.kts`
File: `settings.gradle.kts`
```groovy
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/rahul-lohra/Snorkl")
        }
    }
}
```
2. In `app/build.gradle.kts` file of your android app module (NOT the root file).

File: `app/build.gradle.kts`
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
