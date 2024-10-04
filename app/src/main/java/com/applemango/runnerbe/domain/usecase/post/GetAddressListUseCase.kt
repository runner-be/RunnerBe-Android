package com.applemango.runnerbe.domain.usecase.post

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.applemango.runnerbe.data.network.response.BaseResponse
import com.applemango.runnerbe.data.network.response.KakaoLocalResponse
import com.applemango.runnerbe.data.paging.AddressSearchPagingSource
import com.applemango.runnerbe.domain.repository.PostRepository
import com.applemango.runnerbe.presentation.state.CommonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAddressListUseCase @Inject constructor(
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
                Log.e("GetAddressListUseCase", "onSuccess, $it")
                emit(
                    CommonResponse.Success(
                        200,
                        it
                    )
                )
            }
        }.onFailure { e ->
            Log.e("GetAddressListUseCase", "onFailure")
            e.printStackTrace()
            emit(CommonResponse.Failed(999, e.message ?: "GetAddressListUseCase ERROR"))
        }
    }
}