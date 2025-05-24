package rahul.lohra.networkmonitor.core

import android.content.Context

object SdkInitializerRegistry {
    private val initializers = mutableListOf<SdkInitializer>()

    fun register(initializer: SdkInitializer) {
        initializers += initializer
    }

    internal fun runAll(context: Context) {
        initializers.forEach { it.initialize(context) }
    }
}
