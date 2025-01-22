package kr.devkyu.data.mapperImpl

import kr.devkyu.data.mapper.MyPageMapper
import kr.devkyu.data.mapper.PostingMapper
import kr.devkyu.data.mapper.UserMapper
import com.applemango.runnerbe.entity.MyPageEntity
import kr.devkyu.data.dto.GetMyPageResult
import kr.devkyu.data.dto.MyPageDto
import javax.inject.Inject

class MyPageMapperImpl @Inject constructor(
    private val userMapper: UserMapper,
    private val postingMapper: PostingMapper
) : MyPageMapper {
    override fun mapToData(input: MyPageEntity): MyPageDto {
        return MyPageDto(
            result = GetMyPageResult(
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

    override fun mapToDomain(input: MyPageDto): MyPageEntity {
        val data = input.result
        return MyPageEntity(
            data.userInfo?.let { userMapper.mapToDomain(it) },
            data.myPosting.map {
                postingMapper.mapToDomain(it)
            },
            data.myRunning.map {
                postingMapper.mapToDomain(it)
            }
        )
    }
}