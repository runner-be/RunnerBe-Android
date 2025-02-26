package com.applemango.presentation.ui.mapperImpl

import com.applemango.domain.entity.PostingDetailEntity
import com.applemango.presentation.ui.mapper.PostingDetailMapper
import com.applemango.presentation.ui.mapper.PostingMapper
import com.applemango.presentation.ui.mapper.UserMapper
import com.applemango.presentation.ui.model.PostingDetailModel
import javax.inject.Inject

class PostingDetailMapperImpl @Inject constructor(
    private val userMapper: UserMapper,
    private val postingMapper: PostingMapper
) : PostingDetailMapper {
    override fun mapToDomain(input: PostingDetailModel): PostingDetailEntity {
        return PostingDetailEntity(
            postList = input.postList.map {
                postingMapper.mapToDomain(it)
            },
            runnerInfo = input.runnerInfo?.map { userMapper.mapToDomain(it) },
            waitingRunnerInfo = input.waitingRunnerInfo?.map {
                userMapper.mapToDomain(it)
            },
            roomId = input.roomId,
            gatheringId = input.gatheringId
        )
    }

    override fun mapToPresentation(input: PostingDetailEntity): PostingDetailModel {
        return PostingDetailModel(
            postList = input.postList.map {
                postingMapper.mapToPresentation(it)
            },
            runnerInfo = input.runnerInfo?.map { userMapper.mapToPresentation(it) },
            waitingRunnerInfo = input.waitingRunnerInfo?.map {
                userMapper.mapToPresentation(it)
            },
            roomId = input.roomId,
            gatheringId = input.gatheringId
        )
    }
}