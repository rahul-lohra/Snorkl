package rahul.lohra.snorkl.data.local.keyvalue

import android.content.Context

class SnorklSharedPref(private val context: Context) : KeyValueStorage {

    private val PREFERENCE_NAME = "snorkl_prefs"

    private val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    enum class PrefKeys(val key: String) {
        PORT("port"),
    }

    override fun getString(key: PrefKeys, defaultValue: String): String {
        return sharedPref.getString(key.key, defaultValue) ?: ""
    }

    override fun putString(key: PrefKeys, defaultValue: String) {
        sharedPref.edit().putString(key.key, defaultValue).apply()
    }

    override fun getInt(key: PrefKeys, defaultValue: Int): Int {
        return sharedPref.getInt(key.key, defaultValue)
    }

    override fun putInt(key: PrefKeys, defaultValue: Int) {
        sharedPref.edit().putInt(key.key, defaultValue).apply()
    }

}