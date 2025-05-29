package rahul.lohra.networkmonitor.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rahul.lohra.networkmonitor.data.local.dao.NetworkLogDao
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity

@Database(entities = [NetworkEntity::class], version = 2, exportSchema = false)
abstract class NetworkDatabase: RoomDatabase() {
    abstract fun networkLogDao(): NetworkLogDao
}
