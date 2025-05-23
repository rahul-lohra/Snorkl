package rahul.lohra.networkmonitor

import rahul.lohra.networkmonitor.network.NetworkLoggerInterceptor
import rahul.lohra.networkmonitor.network.NetworkWebSocketListener

object AutoInterceptorInstaller {

        init {
            try {
                // --- Interceptor Registration ---
                val interceptorRegistryClass = Class.forName("io.getstream.video.android.core.interceptor.StreamOkhttpInterceptorRegistry")
                val interceptorRegisterMethod = interceptorRegistryClass.getMethod(
                    "register", okhttp3.Interceptor::class.java
                )
                interceptorRegisterMethod.invoke(null, NetworkLoggerInterceptor())

            } catch (e: ClassNotFoundException) {
                println("StreamOkhttpInterceptorRegistry not found, skipping interceptor registration")
            } catch (e: Exception) {
                println("Failed to register MonitoringInterceptor: ${e.message}")
            }

            try {
                // --- WebSocket Listener Registration ---
                val wsRegistryClass = Class.forName("io.getstream.video.android.core.interceptor.StreamWebSocketListenerRegistry")
                val wsRegisterMethod = wsRegistryClass.getMethod(
                    "register", okhttp3.WebSocketListener::class.java
                )
                wsRegisterMethod.invoke(null, NetworkWebSocketListener())

            } catch (e: ClassNotFoundException) {
                println("StreamWebSocketListenerRegistry not found, skipping WebSocket listener registration")
            } catch (e: Exception) {
                println("Failed to register MonitoringWebSocketListener: ${e.message}")
            }
        }
    }

