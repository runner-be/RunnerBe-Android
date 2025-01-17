package com.applemango.runnerbe.presentation.mapperImpl

import kr.devkyu.data.dto.GetMyPageResult
import kr.devkyu.data.dto.MyPageDto
import com.applemango.runnerbe.data.mapper.MyPageMapper
import com.applemango.runnerbe.data.mapper.PostingMapper
import com.applemango.runnerbe.data.mapper.UserMapper
import com.applemango.runnerbe.entity.MyPageEntity
import javax.inject.Inject

class MyPageMapperImpl @Inject constructor(
    private val userMapper: UserMapper,
    private val postingMapper: PostingMapper
): MyPageMapper {
    override fun mapToData(input: MyPageEntity): kr.devkyu.data.dto.MyPageDto {
        return kr.devkyu.data.dto.MyPageDto(
            result = kr.devkyu.data.dto.GetMyPageResult(
                input.userInfo?.let { userMapper.mapToData(it) },
                input.myPosting.map {
                    postingMapper.mapToData(it)
                },
                input.myRunning.map {
                    postingMapper.mapToData(it)
                }
            )
        )
    }

    override fun mapToDomain(input: kr.devkyu.data.dto.MyPageDto): MyPageEntity {
        val mypage = input.result
        return MyPageEntity(
            mypage.userInfo?.let { userMapper.mapToDomain(it) },
            mypage.myPosting.map {
                postingMapper.mapToDomain(it)
            },
            mypage.myRunning.map {
                postingMapper.mapToDomain(it)
            }
        )
    }
}