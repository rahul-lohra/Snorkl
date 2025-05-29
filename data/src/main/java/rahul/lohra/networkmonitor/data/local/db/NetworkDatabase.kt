package rahul.lohra.snorkl.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rahul.lohra.snorkl.data.local.dao.NetworkLogDao
import rahul.lohra.snorkl.data.local.entities.NetworkEntity

@Database(entities = [NetworkEntity::class], version = 1, exportSchema = false)
abstract class NetworkDatabase: RoomDatabase() {
    abstract fun networkLogDao(): NetworkLogDao
}
