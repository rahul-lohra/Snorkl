package rahul.lohra.snorkl.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rahul.lohra.snorkl.data.local.entities.NetworkEntity

@Dao
interface NetworkLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NetworkEntity)

    @Query("SELECT * FROM network_logs ORDER BY timestamp DESC")
    fun getAllAsFlow(): Flow<List<NetworkEntity>>

    @Query("SELECT * FROM network_logs ORDER BY timestamp DESC")
    fun getAll(): List<NetworkEntity>

    @Query("DELETE FROM network_logs")
    suspend fun clear()

    @Query("DELETE FROM network_logs WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM network_logs ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    fun getPaged(offset: Int, limit: Int): PagingSource<Int, NetworkEntity>

    @Query("SELECT * FROM network_logs ORDER BY timestamp DESC")
    fun getPaged(): PagingSource<Int, NetworkEntity>

    @Query("SELECT * FROM network_logs ORDER BY timestamp DESC LIMIT 1")
    fun getLatestNetworkLog(): Flow<NetworkEntity?>

    @Query("SELECT * FROM network_logs WHERE id = :id LIMIT 1")
    fun getNetworkLog(id: String): Flow<NetworkEntity>

}
