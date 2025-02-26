package com.applemango.domain.usecaseImpl.post

import com.applemango.domain.entity.CommonEntity
import com.applemango.domain.repository.PostingRepository
import javax.inject.Inject

class ReportPostUseCase @Inject constructor(
    private val repository: PostingRepository
) {
    suspend operator fun invoke(postId: Int): CommonEntity {
        return repository.reportPost(postId)
    }
}