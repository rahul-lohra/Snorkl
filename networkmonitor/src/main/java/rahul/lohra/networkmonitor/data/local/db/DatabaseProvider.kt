package rahul.lohra.networkmonitor.data.local.db

import android.content.Context
import androidx.room.Room
import rahul.lohra.networkmonitor.core.SdkContextHolder

object DatabaseProvider {
    private lateinit var networkDatabase: NetworkDatabase

    fun getDatabase(context: Context): NetworkDatabase {
        if (!::networkDatabase.isInitialized) {
            networkDatabase =
                Room.databaseBuilder(context, NetworkDatabase::class.java, "network_log_db")
                    .fallbackToDestructiveMigration()
                    .build()
        }
        return networkDatabase
    }

    fun getDatabase(): NetworkDatabase {
        if (!::networkDatabase.isInitialized) {
            networkDatabase = getDatabase(SdkContextHolder.getContext())
        }
        return networkDatabase
    }

    fun getDatabaseOrNull(): NetworkDatabase? {
        return if (::networkDatabase.isInitialized) {
            networkDatabase
        } else null
    }
}