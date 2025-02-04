package com.applemango.domain.usecaseImpl.post

import com.applemango.domain.entity.PostingDetailEntity
import com.applemango.domain.repository.PostingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val repository: PostingRepository
) {

    suspend operator fun invoke(postId: Int): Flow<PostingDetailEntity> = flow {
        repository.getPostDetail(postId)
    }
}