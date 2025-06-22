# Introduction

Its an android network monitor library which currently monitors HTTP and Websocket events.
It exposes 2 interfaces to monitor the network
1. Android based application
2. Web based application

## Android based application

The activity named `<packagename>.presentation.ui.NetworkMonitorActivity` is responsible for rendering
for rendering the UI to monitor network activity

## Web based application
The sdk also create a webserver such that the user can monitor the network over desktop
It uses localhost as host 

The port is decided by available ports on Android device:

```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val availablePort = findAvailablePort()
        WebSocketServerManager.startServer(this, availablePort)
    }
}
```

The available paths are as follows:

1. /
2. /logs
3. /assets/{path...}
4. /new
5. /inspector
6. /inspector1

# Usage

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
