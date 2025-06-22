package rahul.lohra.snorkl.data.local.keyvalue

interface KeyValueStorage {

    fun getString(key: SnorklSharedPref.PrefKeys, defaultValue: String = ""): String

    fun putString(key: SnorklSharedPref.PrefKeys, defaultValue: String = "")

    fun getInt(key: SnorklSharedPref.PrefKeys, defaultValue: Int = 0): Int

    fun putInt(key: SnorklSharedPref.PrefKeys, defaultValue: Int = 0)
}