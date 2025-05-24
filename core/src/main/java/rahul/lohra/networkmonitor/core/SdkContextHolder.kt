package rahul.lohra.networkmonitor.core

import android.content.Context

object SdkContextHolder {

    private lateinit var appContext: Context

    internal fun init(context: Context){
        appContext = context.applicationContext
    }

    fun getContext(): Context {
        if (!::appContext.isInitialized) {
            throw IllegalStateException("SdkContextHolder is not initialized. Did you forget to initialize the SDK?")
        }
        return appContext
    }

    fun isInitialized(): Boolean = ::appContext.isInitialized
}