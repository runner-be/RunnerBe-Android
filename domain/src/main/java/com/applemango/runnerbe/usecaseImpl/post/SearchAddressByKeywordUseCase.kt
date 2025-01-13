package com.applemango.runnerbe.usecaseImpl.post

import androidx.paging.PagingData
import com.applemango.runnerbe.entity.AddressEntity
import com.applemango.runnerbe.repository.PostingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

/**
 * 주소 검색 (카카오 API)
 */
class SearchAddressByKeywordUseCase @Inject constructor(
    private val repository: PostingRepository
) {
    operator fun invoke(keyword: String): Flow<PagingData<AddressEntity>> =
        repository.getAddressList(keyword)
            .catch { e ->
                e.printStackTrace()
                throw e
            }
}