package rahul.lohra.networkmonitor.data.local.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import rahul.lohra.networkmonitor.data.local.dao.NetworkLogDao
import rahul.lohra.networkmonitor.data.local.entities.NetworkEntity

//class NetworkLogPagingSource(
//    private val dao: NetworkLogDao
//) : PagingSource<Int, NetworkEntity>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkEntity> {
//        val page = params.key ?: 0
//        val limit = params.loadSize
//        val offset = page * limit
//
//        return try {
//            val data = dao.getPaged(offset, limit)
//            LoadResult.Page(
//                data = data,
//                prevKey = if (page == 0) null else page - 1,
//                nextKey = if (data.isEmpty()) null else page + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, NetworkEntity>): Int? = 0
//}
