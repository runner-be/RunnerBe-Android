package com.applemango.runnerbe.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.applemango.runnerbe.data.network.api.GetAddressResultListApi
import com.applemango.runnerbe.presentation.screen.fragment.map.address.AddressResult
import java.io.IOException
import javax.inject.Inject

class AddressSearchPagingSource @Inject constructor(
    private val getAddressResultListApi: GetAddressResultListApi
) : PagingSource<Int, AddressResult>() {
    lateinit var query: String
    private var isLastResult: Boolean = false

    override fun getRefreshKey(state: PagingState<Int, AddressResult>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AddressResult> {
        val currPage = params.key ?: START_PAGE_NUMBER

        return try {
            val response = getAddressResultListApi.getAddressList(query = query)

            val body = response.body()
            if (response.isSuccessful && body != null) {
                val addressList = body.documents
                    .filter { it.roadAddressName.isNotEmpty() }
                    .map { document ->
                    AddressResult(
                        document.placeName,
                        document.roadAddressName,
                        document.x,
                        document.y,
                        currPage
                    )
                }
                val isEnd = body.meta.isEnd

                LoadResult.Page(
                    data = addressList,
                    prevKey = if (currPage > START_PAGE_NUMBER) currPage - 1 else null,
                    nextKey = if (!isEnd) currPage + 1 else null
                )
            } else {
                LoadResult.Error(IOException("API 응답 오류: ${response.code()}"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val START_PAGE_NUMBER = 1
    }
}