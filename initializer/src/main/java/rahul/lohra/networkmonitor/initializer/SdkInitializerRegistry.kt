package rahul.lohra.networkmonitor.initializer

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SdkInitializerRegistry {
    private val initializers = mutableListOf<SdkInitializer>()

    private val initializersState : MutableStateFlow<Unit> = MutableStateFlow(Unit)

    fun register(initializer: SdkInitializer) {
        initializers += initializer
    }

    internal fun runAll(context: Context) {
        initializers.forEach { it.initialize(context) }
    }
}
