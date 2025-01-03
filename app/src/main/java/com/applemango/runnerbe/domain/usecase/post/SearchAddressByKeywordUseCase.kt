package com.applemango.runnerbe.domain.usecase.post

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.applemango.runnerbe.data.paging.AddressSearchPagingSource
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 주소 검색 (카카오 API)
 */
class SearchAddressByKeywordUseCase @Inject constructor(
    private val addressSearchPagingSource: AddressSearchPagingSource
) {
    operator fun invoke(keyword: String): Flow<CommonResponse> = flow {
        runCatching {
            emit(CommonResponse.Loading)
            Pager(PagingConfig(pageSize = 10)) {
                addressSearchPagingSource.apply {
                    this.query = keyword
                }
            }.flow
        }.onSuccess { result ->
            result.collect {
                emit(
                    CommonResponse.Success(
                        200,
                        it
                    )
                )
            }
        }.onFailure { e ->
            e.printStackTrace()
            emit(CommonResponse.Failed.getDefaultFailed(e.message))
        }
    }
}