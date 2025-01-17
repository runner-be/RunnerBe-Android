package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.dto.PostingDetail
import com.applemango.runnerbe.data.dto.PostingDetailDto
import com.applemango.runnerbe.data.mapper.PostingDetailMapper
import com.applemango.runnerbe.data.mapper.PostingMapper
import com.applemango.runnerbe.data.mapper.UserMapper
import com.applemango.runnerbe.entity.PostingDetailEntity
import javax.inject.Inject

class PostingDetailMapperImpl @Inject constructor(
    private val userMapper: UserMapper,
    private val postingMapper: PostingMapper
) : PostingDetailMapper {
    override fun mapToDomain(input: PostingDetailDto): PostingDetailEntity {
        val result = input.result
        return PostingDetailEntity(
            result.postList.map {
                postingMapper.mapToDomain(it)
            },
            result.runnerInfo?.map {
                userMapper.mapToDomain(it)
            },
            result.waitingRunnerInfo?.map {
                userMapper.mapToDomain(it)
            },
            result.roomId,
        )
    }

    override fun mapToData(input: PostingDetailEntity): PostingDetailDto {
        return PostingDetailDto(
            result = PostingDetail(
                input.postList.map {
                    postingMapper.mapToData(it)
                },
                input.runnerInfo?.map {
                    userMapper.mapToData(it)
                },
                input.waitingRunnerInfo?.map {
                    userMapper.mapToData(it)
                },
                input.roomId,
            )
        )
    }
}