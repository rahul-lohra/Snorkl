package rahul.lohra.snorkl

import rahul.lohra.snorkl.network.NetworkLoggerInterceptor
import rahul.lohra.snorkl.network.NetworkWebSocketListener

object AutoInterceptorInstaller {

    fun inject() {
        try {
            // --- Interceptor Registration ---
            val interceptorRegistryClass =
                Class.forName("io.getstream.video.android.core.interceptor.StreamOkhttpInterceptorRegistry")
            val interceptorRegisterMethod = interceptorRegistryClass.getMethod(
                "register", okhttp3.Interceptor::class.java
            )
            val instance = interceptorRegistryClass.getDeclaredField("INSTANCE")
                .get(null) // Get the Kotlin object instance
            interceptorRegisterMethod.invoke(instance, NetworkLoggerInterceptor())

        } catch (e: ClassNotFoundException) {
            println("StreamOkhttpInterceptorRegistry not found, skipping interceptor registration")
        } catch (e: Exception) {
            println("Failed to register MonitoringInterceptor: ${e.message}")
        }

        try {
            // --- WebSocket Listener Registration ---
            val wsRegistryClass =
                Class.forName("io.getstream.video.android.core.interceptor.StreamWebSocketListenerRegistry")
            val wsRegisterMethod = wsRegistryClass.getMethod(
                "register", okhttp3.WebSocketListener::class.java
            )
            val instance = wsRegistryClass.getDeclaredField("INSTANCE")
                .get(null) // Get the Kotlin object instance
            wsRegisterMethod.invoke(instance, NetworkWebSocketListener())

        } catch (e: ClassNotFoundException) {
            println("StreamWebSocketListenerRegistry not found, skipping WebSocket listener registration")
        } catch (e: Exception) {
            println("Failed to register MonitoringWebSocketListener: ${e.message}")
        }
    }
}

