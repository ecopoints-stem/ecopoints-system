package br.edu.uea.ecopoints.screen.home.admin.fragment

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.domain.entity.PickUpRequest

class PickUpRequestPagingSource (
    private val ecoApi: EcoApi,
    private val requesterId: Long
) : PagingSource<Int, PickUpRequest>()  {

    override fun getRefreshKey(state: PagingState<Int, PickUpRequest>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PickUpRequest> {
        val page = params.key ?: 0
        return try {
            val response = ecoApi.getAllRequestsByRequesterId(requesterId, page, params.loadSize)
            val data = response.body()?.content ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}