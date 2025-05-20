package rahul.lohra.networkinspector.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rahul.lohra.networkinspector.data.local.entities.NetworkEntity

@Dao
interface NetworkLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NetworkEntity)

    @Query("SELECT * FROM network_logs ORDER BY timestamp DESC")
    fun getAll(): Flow<List<NetworkEntity>>

    @Query("DELETE FROM network_logs")
    suspend fun clear()

    @Query("SELECT * FROM network_logs ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getPaged(offset: Int, limit: Int): List<NetworkEntity>

}
