package br.edu.uea.ecopoints.screen.home.admin.fragment.recyclerview

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import java.io.IOException

class PickUpRequestPagingSource (
    private val ecoApi: EcoApi,
    private val requesterId: Long
) : PagingSource<Int, PickUpRequest>()  {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PickUpRequest> {
        val page = params.key ?: 0
        return try {
            val response = ecoApi.getAllRequestsByCooperativeAdminId(
                requesterId,
                page = page,
                size = params.loadSize
            )
            if (response.isSuccessful && response.body() != null) {
                Log.i("ECO","Recuperou \n ${response.body()}")
                val responseBody = response.body()!!
                LoadResult.Page(
                    data = responseBody.content,
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (responseBody.last) null else page + 1
                )
            } else {
                Log.i("ECO","Erro ao recupear lista \n ${response.code()}")
                LoadResult.Error(Throwable("Erro na resposta da API"))
            }
        } catch (e: IOException) {
            Log.i("ECO","ERRO de IO ${e.message}")
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, PickUpRequest>): Int?{
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}