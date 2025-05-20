package rahul.lohra.networkinspector.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rahul.lohra.networkinspector.data.local.dao.NetworkLogDao
import rahul.lohra.networkinspector.data.local.entities.NetworkEntity

@Database(entities = [NetworkEntity::class], version = 1)
abstract class NetworkDatabase: RoomDatabase() {
    abstract fun networkLogDao(): NetworkLogDao
}